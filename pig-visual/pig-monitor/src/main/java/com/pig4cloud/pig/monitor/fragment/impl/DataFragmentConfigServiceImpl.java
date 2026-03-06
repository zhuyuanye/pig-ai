package com.pig4cloud.pig.monitor.fragment.impl;


import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import com.pig4cloud.pig.common.core.entity.fragment.DataFragmentConfig;
import com.pig4cloud.pig.monitor.config.FragmentSQLConstants;
import com.pig4cloud.pig.monitor.fragment.DataFragmentConfigService;
import com.pig4cloud.pig.monitor.fragment.dao.DataFragmentConfigDao;
import com.pig4cloud.pig.monitor.pojo.dto.DataFragmentVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * 数据碎片配置 Service 实现类
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DataFragmentConfigServiceImpl implements DataFragmentConfigService {

    @Resource
    private DataFragmentConfigDao dataFragmentConfigDao;

    /**
     * 新增数据碎片配置
     *
     * @param config
     * @return
     */
    @Override
    public DataFragmentConfig save(DataFragmentConfig config) {
        String sqlType = config.getSqlType();
        switch (sqlType) {
            case "0":
                config.setSqlInfo(FragmentSQLConstants.MYSQL_FRAGMENT_SQL);
                break;
            case "1":
                config.setSqlInfo(FragmentSQLConstants.PGSQL_FRAGMENT_SQL);
                break;
            case "2":
                config.setSqlInfo(FragmentSQLConstants.HGDB_FRAGMENT_SQL);
                break;
        }
        return dataFragmentConfigDao.save(config);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        dataFragmentConfigDao.deleteById(id);
    }

    /**
     * 查询根据ID
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DataFragmentConfig> findById(Long id) {
        return dataFragmentConfigDao.findById(id);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<DataFragmentConfig> findAll() {
        return dataFragmentConfigDao.findAll();
    }

    /**
     * 根据ID 获取数据碎片信息
     *
     * @param id
     * @return
     */
    @Override
    public List<DataFragmentVO> getDataFragmentInfo(Long id) {
        List<DataFragmentVO> dataFragmentVOS = new ArrayList<>();
        Optional<DataFragmentConfig> dataFragmentConfigOpt = findById(id);

        if (!dataFragmentConfigOpt.isPresent()) {
            // 处理找不到配置情况，抛异常或返回空列表
            return dataFragmentVOS;
        }

        DataFragmentConfig fragmentConfig = dataFragmentConfigOpt.get();
        Properties props = new Properties();
        String url;
        try {
            switch (fragmentConfig.getSqlType()) {
                case "0":
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
                            fragmentConfig.getIp(), fragmentConfig.getSqlPort(), fragmentConfig.getDataBase());
                    props.setProperty("user", fragmentConfig.getUserName());
                    props.setProperty("password", fragmentConfig.getPassWord());
                    break;
                //瀚高PG用一套sql语句
                case "1":
                    Class.forName("org.postgresql.Driver");
                    url = String.format("jdbc:postgresql://%s:%s/%s",
                            fragmentConfig.getIp(), fragmentConfig.getSqlPort(), fragmentConfig.getDataBase());
                    props.setProperty("user", fragmentConfig.getUserName());
                    props.setProperty("password", fragmentConfig.getPassWord());
                    break;
                case "2":
                    Class.forName("com.highgo.jdbc.Driver");
                    url = String.format("jdbc:highgo://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&ssl=false&sslmode=disable",
                            fragmentConfig.getIp(), fragmentConfig.getSqlPort(), fragmentConfig.getDataBase());
                    props.setProperty("user", fragmentConfig.getUserName());
                    props.setProperty("password", fragmentConfig.getPassWord());

                    break;
                default:
                    throw new IllegalArgumentException("Unsupported database type: " + fragmentConfig.getSqlType());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found", e);
        }

        String sql = fragmentConfig.getSqlInfo() + " " + fragmentConfig.getTopNumber();
        try (Connection connection = DriverManager.getConnection(url, fragmentConfig.getUserName(), fragmentConfig.getPassWord());
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                String schemaName = rs.getString("schemaName");
                String tableName = rs.getString("tableName");
                long fragmentBytes = rs.getLong("fragmentBytes");
                double fragmentPercent = rs.getDouble("fragmentPercent");

                DataFragmentVO vo = DataFragmentVO.builder()
                        .schemaName(schemaName)
                        .tableName(tableName)
                        .fragmentBytes(String.valueOf(fragmentBytes))
                        .fragmentPercent(String.format("%.2f%%", fragmentPercent))
                        .build();

                dataFragmentVOS.add(vo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying data fragment", e);
        }
        return dataFragmentVOS;
    }
}

