package com.pig4cloud.pig.monitor.controller;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;
import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NetworkTimingTopology {

    @Autowired
    private SnmpConfigService snmpConfigService;

    /**
     * 路由器/防火墙拓扑任务
     */
    @Autowired
    private NetworkRouterTopologyService networkRouterService;
    /**
     * 交换机拓扑任务
     */
    @Autowired
    private NetworkSwitchTopologyDiscoverService networkSwitchTopologyDiscoverService;

    /**
     * 设备拓扑任务
     */
    @Autowired
    private NetworkEquipmentService networkEquipmentService;

    /**
     * VLAN拓扑
     */
    @Autowired
    private NetworkVLANService networkVLANService;


    /**
     * 数据信息保存
     */
    @Autowired
    private NetworkTopologyService networkTopologyService;

    /**
     * 3分钟执行网络拓扑任务
     */
    @Scheduled(fixedRate = 1000 * 60 * 3)
    @Transactional
    public void networkTopology() {
        log.info("开始执行网络拓扑定时任务");
        try {
            //获取正在运行的配置集合
            List<SnmpConfigInfo> runningConfigs = snmpConfigService.getRunningConfigs();
            log.info("获取到运行中的 SNMP 配置数量：{}", runningConfigs.size());
            for (SnmpConfigInfo snmpConfig : runningConfigs) {
                log.info("处理配置 ID={}，IP={}，设备类型={}", snmpConfig.getId(), snmpConfig.getIp(), snmpConfig.getDeviceType());
                //删除旧数据
                networkTopologyService.deleteAllByConfigId(String.valueOf(snmpConfig.getId()));
                switch (snmpConfig.getDeviceType()) {
                    case "0"://路由器
                    case "1"://防火墙
                    case "6"://三层交换机
                        log.info("执行路由器/防火墙/三层交换机拓扑发现，configId={}", snmpConfig.getId());
                        saveNetworkInfo(snmpConfig, networkRouterService.discover(snmpConfig));
                        break;
                    case "2":
                        log.info("执行两层交换机拓扑发现，configId={}", snmpConfig.getId());
                        networkSwitchTopologyDiscoverService.discoverSwitchMacTable(snmpConfig);
                        break;
                    case "3":
                        log.info("执行终端设备扫描，IP段：{}.*", snmpConfig.getIp());
                        saveEquipmentInfo(snmpConfig, networkEquipmentService.scanSubnet(snmpConfig.getIp(), 1, 255));
                        break;
                    case "4":
                        log.info("保留：处理 VPN 拓扑逻辑");
                        break;
                }
            }

            //最后处理两层交换机逻辑 （两层交换机需要通过mac地址获取IP 必须需要配置路由器）
            for (SnmpConfigInfo snmpConfig : runningConfigs) {

                if (snmpConfig.getDeviceType().equals("2") || snmpConfig.getDeviceType().equals("5")) {
                    if (snmpConfig.getDeviceType().equals("5")) {
                        log.info("执行 VLAN 交换机拓扑发现，configId={}", snmpConfig.getId());
                        // 处理 VLAN 逻辑
                        List<Map<String, Object>> vlanList = networkVLANService.discover(snmpConfig);
                        for (Map<String, Object> vlanMap : vlanList) {
                            Object listInfo = vlanMap.get("devices");
                            if (listInfo instanceof List<?> deviceList) {
                                log.info("vlanDeviceList:size:{}", deviceList.size());
                                for (Object listInfoMap : deviceList) {
                                    if (listInfoMap instanceof Map<?, ?> map) {
                                        NetworkTopologyInfo networkTopologyInfo = new NetworkTopologyInfo();
                                        networkTopologyInfo.setConfigId(String.valueOf(snmpConfig.getId()));
                                        networkTopologyInfo.setIp("未知IP");
                                        networkTopologyInfo.setMac(String.valueOf(map.get("mac")));
                                        networkTopologyInfo.setPort(String.valueOf(map.get("port")));

                                        networkTopologyInfo.setVlanId(String.valueOf(vlanMap.get("vlanId")));
                                        networkTopologyInfo.setVlanName(String.valueOf(vlanMap.get("vlanName")));
                                        networkTopologyInfo.setDeviceType(snmpConfig.getDeviceType());

                                        log.info("发现 VLAN 设备：{}", networkTopologyInfo);
                                        saveByMacNetworkInfo(String.valueOf(map.get("mac")), networkTopologyInfo);
                                    }
                                }
                            }
                        }

                    } else {
                        log.info("执行两层交换机MAC发现，configId={}", snmpConfig.getId());
                        List<Map<String, String>> maps = networkSwitchTopologyDiscoverService.discoverSwitchMacTable(snmpConfig);
                        log.info("switch:size:{}", null == maps ? 0 : maps.size());
                        //交换机连接信息
                        for (Map<String, String> switchMap : maps) {
                            NetworkTopologyInfo networkTopologyInfo = new NetworkTopologyInfo();
                            networkTopologyInfo.setConfigId(String.valueOf(snmpConfig.getId()));
                            networkTopologyInfo.setIp("未知IP");

                            networkTopologyInfo.setMac(switchMap.get("mac"));
                            networkTopologyInfo.setInterfaceName(switchMap.get("interface"));
                            networkTopologyInfo.setDeviceType(snmpConfig.getDeviceType());

                            log.info("发现交换机连接信息：{}", networkTopologyInfo);
                            saveByMacNetworkInfo(switchMap.get("mac"), networkTopologyInfo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("处理网络拓扑信息异常", e);
        }
        log.info("网络拓扑定时任务执行完成");
    }


    /**
     * 5分钟执行vpn拓扑任务
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    @Transactional
    public void networkVPN() {
        log.info("（VPN）网络拓扑任务开始执行");
        List<SnmpConfigInfo> runningConfigs = snmpConfigService.getRunningConfigs();
        //过滤掉不是VPN的配置
        runningConfigs.removeIf(config -> !config.getDeviceType().equals("4"));
        //解析VPN
        for (SnmpConfigInfo snmpConfig : runningConfigs) {
            log.info("（VPN）网络拓扑:开始获取配置数量{}", runningConfigs.size());
            //vpn存入 用户名 密码 !#! 号拼接
            String userAndPassword = snmpConfig.getCommunity();
            String[] user = userAndPassword.split("!#!");
            String vpnIP = snmpConfig.getIp();
            String[] VPNIP = vpnIP.split(":");
            int port = Integer.parseInt(VPNIP[1]);
            String ip = VPNIP[0];
            getVPNInfo(user[0], user[1], ip, port, snmpConfig);
        }
        log.info("（VPN）网络拓扑:完成");
    }





    /**
     * 处理数据 合并创建关系 入库 （三层交换机 路由器 防火墙 通用 ）
     *
     * @param config
     * @param networkInfo
     */
    private void saveNetworkInfo(SnmpConfigInfo config, List<Map<String, String>> networkInfo) {
        log.info("saveNetworkInfo:size:{}", null == networkInfo ? 0 : networkInfo.size());
        for (Map<String, String> netInfo : networkInfo) {
            NetworkTopologyInfo networkTopologyInfo = new NetworkTopologyInfo();
            networkTopologyInfo.setConfigId(config.getId().toString());
            networkTopologyInfo.setIp(netInfo.get("ip"));
            networkTopologyInfo.setMac(netInfo.get("mac"));
            networkTopologyInfo.setInterfaceName(netInfo.get("interface"));
            networkTopologyInfo.setDeviceType(config.getDeviceType());

            log.info("保存网络拓扑信息(类型{})：{}", config.getDeviceType(), networkTopologyInfo);
            networkTopologyService.save(networkTopologyInfo);
        }

    }


    /**
     * 处理数据 合并创建关系 入库 设备
     *
     * @param config
     * @param networkInfo
     */
    private void saveEquipmentInfo(SnmpConfigInfo config, Map<String, String> networkInfo) {
        log.info("saveEquipmentInfo:size:{}", null == networkInfo ? 0 : networkInfo.size());
        String configId = config.getId().toString();
        String deviceType = config.getDeviceType();
        networkInfo.forEach((ip, mac) -> {
            NetworkTopologyInfo networkTopologyInfo = new NetworkTopologyInfo();
            networkTopologyInfo.setConfigId(configId);
            networkTopologyInfo.setIp(ip);
            networkTopologyInfo.setMac(mac);
            networkTopologyInfo.setInterfaceName("");
            networkTopologyInfo.setDeviceType(deviceType);

            log.info("保存终端设备拓扑信息(类型{})：{}", deviceType, networkTopologyInfo);
            networkTopologyService.save(networkTopologyInfo);
        });
    }

    /**
     * 保存网络拓扑信息 根据MAC地址匹配IP地址
     *
     * @param mac
     * @param networkTopologyInfo
     */
    private void saveByMacNetworkInfo(String mac, NetworkTopologyInfo networkTopologyInfo) {
        //匹配IP地址
        List<NetworkTopologyInfo> topologyServiceByMac = networkTopologyService.findByMac(mac);
        for (NetworkTopologyInfo topologyInfo : topologyServiceByMac) {
            if (topologyInfo.getMac().equals(mac)) {
                networkTopologyInfo.setIp(topologyInfo.getIp());
            }
        }
        log.info("保存（MAC匹配）网络拓扑信息：{}", networkTopologyInfo);
        //入库
        networkTopologyService.save(networkTopologyInfo);

    }

    /**
     * 连接VPN 设备
     * @param user
     * @param password
     * @param host
     * @param port
     */
    private void getVPNInfo(String user, String password, String host, int port,SnmpConfigInfo config) {

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);

            // 自动接受未知主机 key
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000); // 连接超时时间30秒

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("wg show");
            channel.setInputStream(null);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()))) {
                channel.connect();

                String line;
                String ip = null, handshake = null;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("endpoint:")) {
                        String[] parts = line.split(":");
                        if (parts.length >= 2) {
                            ip = parts[1].trim();
                        }
                    } else if (line.startsWith("latest handshake:")) {
                        handshake = line.substring("latest handshake:".length()).trim();
                    }

                    if (ip != null && handshake != null) {
                        saveVpnClientInfo(ip, handshake, config.getId().toString());
                        ip = null;
                        handshake = null;
                    }
                }
            } finally {
                channel.disconnect();
                session.disconnect();
            }

        } catch (JSchException | IOException e) {
            log.error("远程执行 VPN 拓扑命令失败", e);
            throw new RuntimeException("获取 VPN 网络拓扑失败", e);
        }


    }

    /**
     * 保存VPN设备信息
     * @param ip
     * @param handshake
     * @param configId
     */
    private void saveVpnClientInfo(String ip, String handshake, String configId) {
        NetworkTopologyInfo info = new NetworkTopologyInfo();
        info.setConfigId(configId);
        info.setIp(ip);
        info.setMac("VPN获取不到MAC"); // MAC在VPN层通常不可见
        info.setInterfaceName(handshake); // 这里使用“握手时间”作为接口名展示
        info.setDeviceType("4"); // 自定义标识符，可使用枚举或常量
        log.info("保存（VPN）网络拓扑信息：{}", info);
        networkTopologyService.save(info);
    }

}
