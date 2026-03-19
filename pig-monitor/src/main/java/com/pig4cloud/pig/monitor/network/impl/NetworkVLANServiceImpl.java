package com.pig4cloud.pig.monitor.network.impl;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.NetworkVLANService;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * VLAN 网络拓扑信息服务实现
 */
@Service
public class NetworkVLANServiceImpl implements NetworkVLANService {


    private final Snmp snmp;

    public NetworkVLANServiceImpl() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    /**
     * 发现 VLAN 网络拓扑信息
     *
     * @param config SNMP 配置信息（IP、community、版本）
     * @return VLAN 列表，每个包含 vlanId、vlanName、端口、设备
     * @throws IOException SNMP 通信异常
     */
    @Override
    public List<Map<String, Object>> discover(SnmpConfigInfo config) throws IOException {
        String ip = config.getIp();
        String community = config.getCommunity();
        int version = config.getVersion();

        CommunityTarget target = createTarget(ip, community, version);
        Map<Integer, String> vlanNames = getVlanNames(target); // 获取 VLAN 名称
        Map<Integer, List<Integer>> vlanPorts = getVlanPorts(target); // 获取 VLAN 对应端口
        Map<String, Integer> macPortMap = getMacToPortMapping(target); // 获取 MAC 到端口的映射

        List<Map<String, Object>> result = new ArrayList<>();
        for (Integer vlanId : vlanNames.keySet()) {
            Map<String, Object> vlanInfo = new LinkedHashMap<>();
            vlanInfo.put("vlanId", vlanId);
            vlanInfo.put("vlanName", vlanNames.get(vlanId));
            List<Integer> ports = vlanPorts.getOrDefault(vlanId, new ArrayList<>());
            vlanInfo.put("ports", ports);

            List<Map<String, Object>> devices = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : macPortMap.entrySet()) {
                if (ports.contains(entry.getValue())) {
                    Map<String, Object> device = new LinkedHashMap<>();
                    device.put("mac", entry.getKey());
                    device.put("port", entry.getValue());
                    devices.add(device);
                }
            }
            vlanInfo.put("devices", devices);
            result.add(vlanInfo);
        }
        return result;
    }

    /**
     * 构建 SNMP 目标
     */
    private CommunityTarget createTarget(String ip, String community, int version) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(GenericAddress.parse("udp:" + ip + "/161"));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(version);
        return target;
    }

    /**
     * 获取 VLAN ID 与名称的映射
     */
    private Map<Integer, String> getVlanNames(CommunityTarget target) {
        OID vlanNameOid = new OID("1.3.6.1.2.1.17.7.1.4.3.1.1");
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = utils.getTable(target, new OID[]{vlanNameOid}, null, null);

        Map<Integer, String> vlanMap = new HashMap<>();
        for (TableEvent event : events) {
            if (event.isError()) continue;
            VariableBinding vb = event.getColumns()[0];
            int vlanId = vb.getOid().last();
            vlanMap.put(vlanId, vb.getVariable().toString());
        }
        return vlanMap;
    }

    /**
     * 获取 VLAN 对应的端口号列表
     */
    private Map<Integer, List<Integer>> getVlanPorts(CommunityTarget target) throws IOException {
        OID egressPortOid = new OID("1.3.6.1.2.1.17.7.1.4.3.1.2");
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = utils.getTable(target, new OID[]{egressPortOid}, null, null);
        Map<Integer, List<Integer>> vlanPortMap = new HashMap<>();
        for (TableEvent event : events) {
            int vlanId = event.getIndex().last();
            OctetString macOctet = (OctetString) event.getColumns()[0].getVariable();
            byte[] bitmap = macOctet.toByteArray();
            List<Integer> ports = decodePortBitmap(bitmap);
            vlanPortMap.put(vlanId, ports);
        }
        return vlanPortMap;
    }

    /**
     * 将位图形式的端口数据解码为端口号列表
     */
    private List<Integer> decodePortBitmap(byte[] bitmap) {
        List<Integer> ports = new ArrayList<>();
        for (int i = 0; i < bitmap.length; i++) {
            int b = bitmap[i] & 0xFF;
            for (int j = 0; j < 8; j++) {
                if ((b & (1 << (7 - j))) != 0) {
                    ports.add(i * 8 + j + 1);
                }
            }
        }
        return ports;
    }

    /**
     * 获取 MAC 地址到交换机端口号的映射表
     */
    private Map<String, Integer> getMacToPortMapping(CommunityTarget target) throws IOException {
        OID fdbOid = new OID("1.3.6.1.2.1.17.4.3.1.2"); // dot1dTpFdbPort
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory());
        List<TableEvent> events = utils.getTable(target, new OID[]{fdbOid}, null, null);

        Map<String, Integer> macPortMap = new HashMap<>();
        for (TableEvent event : events) {
            if (event.isError()) continue;
            OID oid = event.getIndex();
            int[] macBytes = Arrays.copyOfRange(oid.getValue(), oid.size() - 6, oid.size());
            StringBuilder sb = new StringBuilder();
            for (int b : macBytes) {
                sb.append(String.format("%02X:", b));
            }
            String mac = sb.substring(0, sb.length() - 1);
            int port = event.getColumns()[0].getVariable().toInt();
            macPortMap.put(mac, port);
        }
        return macPortMap;
    }


}
