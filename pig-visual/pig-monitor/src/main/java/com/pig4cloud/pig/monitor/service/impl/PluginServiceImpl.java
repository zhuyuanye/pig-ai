/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.monitor.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import com.pig4cloud.pig.common.core.constants.PluginType;
import com.pig4cloud.pig.common.core.entity.dto.PluginUpload;
import com.pig4cloud.pig.common.core.entity.job.Configmap;
import com.pig4cloud.pig.common.core.entity.manager.PluginItem;
import com.pig4cloud.pig.common.core.entity.manager.PluginMetadata;
import com.pig4cloud.pig.common.core.entity.plugin.PluginConfig;
import com.pig4cloud.pig.common.core.entity.plugin.PluginContext;
import com.pig4cloud.pig.common.core.support.exception.CommonException;
import com.pig4cloud.pig.monitor.mapper.PluginItemMapper;
import com.pig4cloud.pig.monitor.mapper.PluginMetadataMapper;
import com.pig4cloud.pig.monitor.mapper.PluginParamMapper;
import com.pig4cloud.pig.monitor.pojo.dto.PluginParam;
import com.pig4cloud.pig.monitor.pojo.dto.PluginParametersVO;
import com.pig4cloud.pig.monitor.service.PluginService;
import com.pig4cloud.pig.common.plugin.Plugin;
import com.pig4cloud.pig.common.plugin.PostAlertPlugin;
import com.pig4cloud.pig.common.plugin.PostCollectPlugin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * plugin service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PluginServiceImpl implements PluginService {

    private final PluginMetadataMapper metadataMapper;

    private final PluginItemMapper itemMapper;

    private final PluginParamMapper pluginParamMapper;

    public static Map<Class<?>, PluginType> PLUGIN_TYPE_MAPPING = new HashMap<>();

    /**
     * plugin status
     */
    private static final Map<String, Boolean> PLUGIN_ENABLE_STATUS = new ConcurrentHashMap<>();

    /**
     * plugin param define
     */
    private static final Map<Long, PluginConfig> PARAMS_CONFIG_MAP = new ConcurrentHashMap<>();

    /**
     * plugin params
     */
    private static final Map<Long, List<Configmap>> PARAMS_MAP = new ConcurrentHashMap<>();

    /**
     * pluginItem Mapping pluginId
     */
    private static final Map<String, Long> ITEM_TO_PLUGINMETADATAID_MAP = new ConcurrentHashMap<>();

    private final List<URLClassLoader> pluginClassLoaders = new ArrayList<>();

    @Override
    @Transactional
    public void deletePlugins(Set<Long> ids) {
        List<PluginMetadata> plugins = metadataMapper.selectBatchIds(ids);
        // disable the plugins that need to be removed
        for (PluginMetadata plugin : plugins) {
            plugin.setEnableStatus(false);
            updateStatus(plugin);
        }
        // reload classloader
        loadJarToClassLoader();
        for (PluginMetadata plugin : plugins) {
            try {
                // delete jar file
                File jarFile = new File(plugin.getJarFilePath());
                if (jarFile.exists()) {
                    FileUtils.delete(jarFile);
                }
                // removing jar files that are dependencies for the plugin
                File otherLibDir = new File(getOtherLibDir(plugin.getJarFilePath()));
                if (otherLibDir.exists()) {
                    FileUtils.deleteDirectory(otherLibDir);
                }
                // delete metadata
                metadataMapper.deleteById(plugin.getId());
                syncPluginParamMap(plugin.getId(), null, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        pluginParamMapper.delete(new QueryWrapper<com.pig4cloud.pig.monitor.pojo.dto.PluginParam>()
                .in("plugin_metadata_id", ids));
        syncPluginStatus();

    }

    /**
     * get the directory where the JAR files dependent on the plugin are saved
     *
     * @param pluginJarPath jar file path
     * @return lib dir
     */
    private String getOtherLibDir(String pluginJarPath) {
        return pluginJarPath.substring(0, pluginJarPath.lastIndexOf("."));
    }

    @Override
    public void updateStatus(PluginMetadata plugin) {
        PluginMetadata metadata = metadataMapper.selectById(plugin.getId());
        if (metadata != null) {
            metadata.setEnableStatus(plugin.getEnableStatus());
            metadataMapper.updateById(metadata);
            syncSinglePluginStatus(metadata);
        } else {
            throw new IllegalArgumentException("The plugin is not existed");
        }
    }

    @Override
    public PluginParametersVO getParamDefine(Long pluginMetadataId) {

        PluginParametersVO pluginParametersVO = new PluginParametersVO();
        if (PARAMS_CONFIG_MAP.containsKey(pluginMetadataId)) {
            PluginConfig config = PARAMS_CONFIG_MAP.get(pluginMetadataId);
            List<PluginParam> paramsByPluginMetadataId = pluginParamMapper.selectList(
                    new QueryWrapper<PluginParam>().eq("plugin_metadata_id", pluginMetadataId));
            pluginParametersVO.setParamDefines(Optional.ofNullable(config).map(PluginConfig::getParams).orElse(new ArrayList<>()));
            pluginParametersVO.setPluginParams(paramsByPluginMetadataId);
            return pluginParametersVO;
        }
        return pluginParametersVO;
    }

    @Override
    @Transactional
    public void savePluginParam(List<PluginParam> params) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        pluginParamMapper.delete(new QueryWrapper<PluginParam>()
                .eq("plugin_metadata_id", params.get(0).getPluginMetadataId()));
        params.forEach(param -> {
            if (param.getId() == null) {
                pluginParamMapper.insert(param);
            } else {
                pluginParamMapper.updateById(param);
            }
        });
        syncPluginParamMap(params.get(0).getPluginMetadataId(), params, false);
    }

    private void syncPluginParamMap(Long pluginMetadataId, List<PluginParam> params, boolean isDelete) {
        if (isDelete) {
            PARAMS_MAP.remove(pluginMetadataId);
            return;
        }
        List<Configmap> configmapList = params.stream().map(item -> new Configmap(item.getField(), item.getParamValue(), item.getType())).toList();
        PARAMS_MAP.put(pluginMetadataId, configmapList);
    }

    static {
        PLUGIN_TYPE_MAPPING.put(Plugin.class, PluginType.POST_ALERT);
        PLUGIN_TYPE_MAPPING.put(PostAlertPlugin.class, PluginType.POST_ALERT);
        PLUGIN_TYPE_MAPPING.put(PostCollectPlugin.class, PluginType.POST_COLLECT);
    }

    /**
     * verify the type of the jar package
     *
     * @param jarFile jar file
     * @return return the result of jar package parsed
     */
    public PluginMetadata validateJarFile(File jarFile) {
        PluginMetadata metadata = new PluginMetadata();
        List<PluginItem> pluginItems = new ArrayList<>();
        AtomicInteger pluginImplementationCount = new AtomicInteger(0);
        try {
            validateFilePath(jarFile);
            URL jarUrl = new URL("file:" + jarFile.getAbsolutePath());
            validateJarUrl(jarUrl);
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, this.getClass().getClassLoader());
                JarFile jar = new JarFile(jarFile)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace("/", ".").replace(".class", "");
                        try {
                            Class<?> cls = classLoader.loadClass(className);
                            if (cls.isInterface()) {
                                continue;
                            }
                            if (pluginImplementationCount.get() >= 1) {
                                throw new CommonException("A plugin package can only contain one plugin implementation class");
                            }
                            PLUGIN_TYPE_MAPPING.forEach((clazz, type) -> {
                                if (clazz.isAssignableFrom(cls)) {
                                    pluginItems.add(new PluginItem(className, type));
                                    pluginImplementationCount.incrementAndGet();
                                }
                            });
                        } catch (ClassNotFoundException e) {
                            System.err.println("Failed to load class: " + className);
                        }
                    }
                    if ((entry.getName().contains("define")) && (entry.getName().endsWith(".yml") || entry.getName().endsWith(".yaml"))) {
                        PluginConfig config = readPluginConfig(jar, entry);
                        metadata.setParamCount(CollectionUtils.size(config.getParams()));
                    }
                }
                if (pluginItems.isEmpty()) {
                    throw new CommonException("Illegal plug-ins, please refer to https://hertzbeat.apache.org/docs/help/plugin/");
                }
            } catch (IOException e) {
                log.error("Error reading JAR file:{}", jarFile.getAbsoluteFile(), e);
                throw new CommonException("Error reading JAR file: " + jarFile.getAbsolutePath());
            }
        } catch (MalformedURLException e) {
            log.error("Invalid JAR file URL: {}", jarFile.getAbsoluteFile(), e);
            throw new CommonException("Invalid JAR file URL: " + jarFile.getAbsolutePath());
        } catch (YAMLException e) {
            throw new CommonException("YAML the file format is incorrect");
        }
        metadata.setItems(pluginItems);
        return metadata;
    }

    /**
     * Validate that the file resides within the expected directory.
     *
     * @param file the file to validate
     */
    private void validateFilePath(File file) {
        try {
            String canonicalPath = file.getCanonicalPath();
            String expectedDir = new File("plugin-lib").getCanonicalPath();
            if (!canonicalPath.startsWith(expectedDir)) {
                throw new CommonException("File is outside the allowed directory: " + canonicalPath);
            }
        } catch (IOException e) {
            log.error("Error validating file path: {}", file.getAbsolutePath(), e);
            throw new CommonException("Error validating file path: " + file.getAbsolutePath());
        }
    }

    /**
     * Validate that the URL uses the 'file:' protocol and does not point to an external resource.
     *
     * @param url the URL to validate
     */
    private void validateJarUrl(URL url) {
        if (!"file".equals(url.getProtocol())) {
            throw new CommonException("Invalid URL protocol: " + url.getProtocol());
        }
    }

    private void validateMetadata(PluginMetadata metadata) {
        Long count = metadataMapper.selectCount(new QueryWrapper<PluginMetadata>()
                .eq("name", metadata.getName()));
        if (count != null && count != 0) {
            throw new CommonException("A plugin named " + metadata.getName() + " already exists");
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void savePlugin(PluginUpload pluginUpload) {
        String jarPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
        Path extLibPath = Paths.get(new File(jarPath).getParent(), "plugin-lib");
        File extLibDir = extLibPath.toFile();
        String fileName = pluginUpload.getJarFile().getOriginalFilename();
        validateFileName(fileName);
        fileName = UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
        File destFile = new File(extLibDir, fileName);
        FileUtils.createParentDirectories(destFile);
        pluginUpload.getJarFile().transferTo(destFile);
        List<PluginItem> pluginItems;
        PluginMetadata pluginMetadata;
        try {
            PluginMetadata parsed = validateJarFile(destFile);
            pluginItems = parsed.getItems();
            pluginMetadata = PluginMetadata.builder()
                .name(pluginUpload.getName())
                .enableStatus(true)
                .paramCount(parsed.getParamCount())
                .items(pluginItems).jarFilePath(destFile.getAbsolutePath())
                .gmtCreate(LocalDateTime.now())
                .build();
            validateMetadata(pluginMetadata);
        } catch (Exception e) {
            // verification failed, delete file
            FileUtils.delete(destFile);
            throw e;
        }
        // save plugin metadata
        metadataMapper.insert(pluginMetadata);
        pluginItems.forEach(item -> itemMapper.insert(item));
        // load jar to classloader
        loadJarToClassLoader();
        // sync enabled status
        syncPluginStatus();
    }

    /**
     * validate file name if file name is invalid, throw exception
     *
     * @param fileName file name
     */
    private void validateFileName(String fileName) {
        if (fileName == null) {
            throw new CommonException("Failed to upload plugin");
        }
        if (fileName.matches(".*(\\.\\.|[\n\t\r/\\\\]).*")) {
            throw new CommonException("Invalid plugin file name: " + fileName);
        }
    }

    @Override
    public boolean pluginIsEnable(Class<?> clazz) {
        return Boolean.TRUE.equals(PLUGIN_ENABLE_STATUS.get(clazz.getName()));
    }

    @Override
    public IPage<PluginMetadata> getPlugins(String search, int pageIndex, int pageSize) {
        // Get tag information
        LambdaQueryWrapper<PluginMetadata> queryWrapper = new LambdaQueryWrapper<>();
        if (search != null && !search.isEmpty()) {
            queryWrapper.like(PluginMetadata::getName, search);
        }
        Page<PluginMetadata> page = new Page<>(pageIndex, pageSize);
        return metadataMapper.selectPage(page, queryWrapper);
    }

    /**
     * Load all plugin enabled states into memory
     */
    @PostConstruct
    private void syncPluginStatus() {
        List<PluginMetadata> plugins = metadataMapper.selectList(null);
        Map<String, Boolean> statusMap = new HashMap<>();
        Map<String, Long> itemToPluginMetadataIdMap = new HashMap<>();
        for (PluginMetadata plugin : plugins) {
            for (PluginItem item : plugin.getItems()) {
                statusMap.put(item.getClassIdentifier(), plugin.getEnableStatus());
                itemToPluginMetadataIdMap.put(item.getClassIdentifier(), plugin.getId());
            }
        }
        PLUGIN_ENABLE_STATUS.clear();
        PLUGIN_ENABLE_STATUS.putAll(statusMap);
        ITEM_TO_PLUGINMETADATAID_MAP.clear();
        ITEM_TO_PLUGINMETADATAID_MAP.putAll(itemToPluginMetadataIdMap);
    }

    private void syncSinglePluginStatus(PluginMetadata plugin) {
        if (plugin == null || CollectionUtils.isEmpty(plugin.getItems())) {
            return;
        }
        for (PluginItem item : plugin.getItems()) {
            PLUGIN_ENABLE_STATUS.put(item.getClassIdentifier(), plugin.getEnableStatus());
            ITEM_TO_PLUGINMETADATAID_MAP.put(item.getClassIdentifier(), plugin.getId());
        }
    }

    @PostConstruct
    private void initParams() {
        try {
            List<PluginParam> params = pluginParamMapper.selectList(null);
            Map<Long, List<PluginParam>> content = params.stream()
                .collect(Collectors.groupingBy(PluginParam::getPluginMetadataId));

            for (Map.Entry<Long, List<PluginParam>> entry : content.entrySet()) {
                syncPluginParamMap(entry.getKey(), entry.getValue(), false);
            }
        } catch (Exception e) {
            log.error("Failed to init params:{}", e.getMessage());
            throw new CommonException("Failed to init params:" + e.getMessage());
        }
    }

    /**
     * load jar to classloader
     */
    @PostConstruct
    private void loadJarToClassLoader() {
        try {
            for (URLClassLoader pluginClassLoader : pluginClassLoaders) {
                if (pluginClassLoader != null) {
                    pluginClassLoader.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!pluginClassLoaders.isEmpty()) {
            pluginClassLoaders.clear();
            System.gc();
        }
        PARAMS_CONFIG_MAP.clear();
        List<PluginMetadata> plugins = metadataMapper.selectList(
                new QueryWrapper<PluginMetadata>().eq("enable_status", true));
        for (PluginMetadata metadata : plugins) {
            try {
                List<URL> urls = loadLibInPlugin(metadata.getJarFilePath(), metadata.getId());
                urls.add(new File(metadata.getJarFilePath()).toURI().toURL());
                pluginClassLoaders.add(new URLClassLoader(urls.toArray(new URL[0]), Plugin.class.getClassLoader()));
            } catch (MalformedURLException e) {
                log.error("Failed to load plugin:{}", e.getMessage());
                throw new CommonException("Failed to load plugin:" + e.getMessage());
            } catch (IOException exception) {
                log.error("{} plugin file is missing, please delete the plugin and upload it again", metadata.getName());
            }
        }
    }

    /**
     * loading other JAR files that are dependencies for the plugin
     *
     * @param pluginJarPath    jar file path
     * @param pluginMetadataId plugin id
     * @return urls
     */

    private List<URL> loadLibInPlugin(String pluginJarPath, Long pluginMetadataId) throws IOException {
        File libDir = new File(getOtherLibDir(pluginJarPath));
        FileUtils.forceMkdir(libDir);
        List<URL> libUrls = new ArrayList<>();
        try (JarFile jarFile = new JarFile(pluginJarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                File file = new File(libDir, entry.getName());
                if (entry.isDirectory()) {
                    continue;
                }
                if (entry.getName().endsWith(".jar")) {
                    if (!file.getParentFile().exists()) {
                        FileUtils.createParentDirectories(file);
                    }
                    try (InputStream in = jarFile.getInputStream(entry);
                        OutputStream out = new FileOutputStream(file)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        libUrls.add(file.toURI().toURL());
                        out.flush();
                    }
                }
                if ((entry.getName().contains("define")) && (entry.getName().endsWith(".yml") || entry.getName().endsWith(".yaml"))) {
                    PluginConfig config = readPluginConfig(jarFile, entry);
                    PARAMS_CONFIG_MAP.put(pluginMetadataId, config);
                }
            }
        }
        return libUrls;
    }

    /**
     * Read the plugin configuration file from the jar package
     *
     * @return plugin config
     */
    private PluginConfig readPluginConfig(JarFile jarFile, JarEntry entry) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream ymlInputStream = jarFile.getInputStream(entry)) {
            PluginConfig config = yaml.loadAs(ymlInputStream, PluginConfig.class);
            if (config == null) {
                return new PluginConfig();
            }
            return config;
        }
    }

    @Override
    public <T> void pluginExecute(Class<T> clazz, Consumer<T> execute) {
        for (URLClassLoader pluginClassLoader : pluginClassLoaders) {
            ServiceLoader<T> load = ServiceLoader.load(clazz, pluginClassLoader);
            for (T t : load) {
                if (pluginIsEnable(t.getClass())) {
                    execute.accept(t);
                }
            }
        }
    }

    @Override
    public <T> void pluginExecute(Class<T> clazz, BiConsumer<T, PluginContext> execute) {
        for (URLClassLoader pluginClassLoader : pluginClassLoaders) {
            ServiceLoader<T> load = ServiceLoader.load(clazz, pluginClassLoader);
            for (T t : load) {
                if (!pluginIsEnable(t.getClass())) {
                    continue;
                }
                Long pluginId = ITEM_TO_PLUGINMETADATAID_MAP.get(t.getClass().getName());
                List<Configmap> configmapList = PARAMS_MAP.get(pluginId);
                PluginContext context = PluginContext.builder().params(configmapList).build();
                execute.accept(t, context);
            }
        }
    }
}
