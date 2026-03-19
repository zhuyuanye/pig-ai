package com.pig4cloud.pig.monitor.network.impl;

import com.pig4cloud.pig.common.core.entity.network.SnmpConfigInfo;
import com.pig4cloud.pig.monitor.network.NetworkRouterTopologyService;
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
 * 路由器 防火墙 拓扑服务
 */
@Service
public class NetworkRouterTopologyServiceImpl implements NetworkRouterTopologyService {

    private final Snmp snmp;

    public NetworkRouterTopologyServiceImpl() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }




    /**
     * 统一接口，发现路由器或防火墙连接设备信息
     *
     * @param config Snmp配置，包含ip、community、snmp版本、设备类型
     * @return 设备列表，包含ip、mac、接口名称
     */
    @Override
    public List<Map<String, String>> discover(SnmpConfigInfo config) throws IOException {
        String ip = config.getIp();
        String community = config.getCommunity();
        int version = config.getVersion();
        // 目前路由器和防火墙用相同标准MIB实现发现 以后可以根据TYPE进行扩展
        return discoverDevicesByIfAndArp(ip, community, version);
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

    private String formatMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (byte b : mac) {
            sb.append(String.format("%02X:", b));
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private String oidToIp(OID oid) {
        int length = oid.size();
        StringBuilder ip = new StringBuilder();
        for (int i = length - 4; i < length; i++) {
            ip.append(oid.get(i));
            if (i < length - 1) ip.append(".");
        }
        return ip.toString();
    }



    /**
     * 基于接口表和ARP表，发现设备连接关系
     */
    private List<Map<String, String>> discoverDevicesByIfAndArp(String ip, String community, int version) throws IOException {
        OID arpIfIndexOid = new OID("1.3.6.1.2.1.4.22.1.1");
        OID arpPhysAddressOid = new OID("1.3.6.1.2.1.4.22.1.2");
        OID ifDescrOid = new OID("1.3.6.1.2.1.2.2.1.2");

        // 1. 查询接口描述
        Map<Integer, String> ifDescrMap = new HashMap<>();
        for (VariableBinding vb : snmpWalk(ip, community, version, ifDescrOid)) {
            int ifIndex = vb.getOid().last();
            ifDescrMap.put(ifIndex, vb.getVariable().toString());
        }

        // 2. 查询ARP表，构造 IP->MAC 和 IP->ifIndex 映射
        Map<String, String> ipToMac = new HashMap<>();
        Map<String, Integer> ipToIfIndex = new HashMap<>();

        for (VariableBinding vb : snmpWalk(ip, community, version, new OID("1.3.6.1.2.1.4.22.1"))) {
            OID oid = vb.getOid();
            String oidStr = oid.toString();

            if (oidStr.startsWith(arpPhysAddressOid.toString())) {
                String ipAddr = oidToIp(oid);
                OctetString macOctet = (OctetString) vb.getVariable();
                ipToMac.put(ipAddr, formatMac(macOctet.toByteArray()));
            } else if (oidStr.startsWith(arpIfIndexOid.toString())) {
                String ipAddr = oidToIp(oid);
                ipToIfIndex.put(ipAddr, vb.getVariable().toInt());
            }
        }

        // 3. 组装结果
        List<Map<String, String>> devices = new ArrayList<>();
        for (String ipAddr : ipToMac.keySet()) {
            String mac = ipToMac.get(ipAddr);
            Integer ifIndex = ipToIfIndex.get(ipAddr);
            String ifName = ifDescrMap.getOrDefault(ifIndex, "未知接口");
            Map<String, String> device = new LinkedHashMap<>();
            device.put("ip", ipAddr);
            device.put("mac", mac);
            device.put("interface", ifName);
            devices.add(device);
        }
        return devices;
    }
}
