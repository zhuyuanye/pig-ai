package com.pig4cloud.pig.monitor.network.impl;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.NetworkSwitchTopologyDiscoverService;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 交换机拓扑服务实现
 */
@Service
public class NetworkSwitchTopologyDiscoverServiceImpl implements NetworkSwitchTopologyDiscoverService {
    private final Snmp snmp;

    public NetworkSwitchTopologyDiscoverServiceImpl() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }


    /**
     * 获取拓扑信息：MAC 地址 - 接口名称 映射
     */
    @Override
    public List<Map<String, String>> discoverSwitchMacTable(SnmpConfigInfo config) throws IOException {

        String ip = config.getIp();
        String community = config.getCommunity();
        int version = config.getVersion();

        // OID 定义
        OID macToPortOid = new OID("1.3.6.1.2.1.17.4.3.1.2");
        OID portToIfIndexOid = new OID("1.3.6.1.2.1.17.1.4.1.2");
        OID ifDescrOid = new OID("1.3.6.1.2.1.2.2.1.2");

        // 步骤一：获取 MAC → bridge port 映射
        Map<String, Integer> macToBridgePort = new HashMap<>();
        for (VariableBinding vb : snmpWalk(ip, community, version, macToPortOid)) {
            String mac = formatMac(vb.getOid());
            int port = vb.getVariable().toInt();
            macToBridgePort.put(mac, port);
        }

        // 步骤二：bridge port → ifIndex 映射
        Map<Integer, Integer> bridgePortToIfIndex = new HashMap<>();
        for (VariableBinding vb : snmpWalk(ip, community, version, portToIfIndexOid)) {
            int bridgePort = vb.getOid().last();
            int ifIndex = vb.getVariable().toInt();
            bridgePortToIfIndex.put(bridgePort, ifIndex);
        }

        // 步骤三：ifIndex → 接口名称 映射
        Map<Integer, String> ifIndexToName = new HashMap<>();
        for (VariableBinding vb : snmpWalk(ip, community, version, ifDescrOid)) {
            int ifIndex = vb.getOid().last();
            String ifName = vb.getVariable().toString();
            ifIndexToName.put(ifIndex, ifName);
        }

        // 构建结果：MAC → 接口名称
        List<Map<String, String>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : macToBridgePort.entrySet()) {
            String mac = entry.getKey();
            Integer bridgePort = entry.getValue();
            Integer ifIndex = bridgePortToIfIndex.get(bridgePort);
            String ifName = ifIndexToName.getOrDefault(ifIndex, "未知接口");

            Map<String, String> record = new LinkedHashMap<>();
            record.put("mac", mac);
            record.put("interface", ifName);
            result.add(record);
        }

        return result;
    }


    private CommunityTarget createTarget(String ip, String community, int version) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(GenericAddress.parse("udp:" + ip + "/161"));
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(version);
        return target;
    }

    private List<VariableBinding> snmpWalk(String ip, String community, int version, OID baseOid) throws IOException {
        List<VariableBinding> result = new ArrayList<>();
        CommunityTarget target = createTarget(ip, community, version);
        OID currentOid = baseOid;

        while (true) {
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(currentOid));
            pdu.setType(PDU.GETNEXT);
            ResponseEvent resp = snmp.send(pdu, target);
            if (resp == null || resp.getResponse() == null) break;

            VariableBinding vb = resp.getResponse().get(0);
            if (vb == null || vb.getOid() == null || !vb.getOid().startsWith(baseOid)) break;

            currentOid = vb.getOid();
            result.add(vb);
        }
        return result;
    }

    private String formatMac(OID oid) {
        StringBuilder sb = new StringBuilder();
        for (int i = oid.size() - 6; i < oid.size(); i++) {
            sb.append(String.format("%02X", oid.get(i)));
            if (i < oid.size() - 1) sb.append(":");
        }
        return sb.toString();
    }

}
