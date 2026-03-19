package com.pig4cloud.pig.monitor.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import com.pig4cloud.pig.common.core.entity.network.NetworkTopologyInfo;

import java.util.List;

@Data
@Builder
@Schema(description = "网络拓扑信息视图对象")
public class NetworkTopologyVO {

    @Schema(description = "设备 配置 IP 地址", example = "192.168.1.10")
    private String ip;

    @Schema(description = "设备类型 0=路由器 1=防火墙 2=交换机 3=设备 4=VPN 5=VLAN 6 = 三层交换机")
    private String deviceType;

    @Schema(description = "网络拓扑数据 ")
    private List<NetworkTopologyInfo> networkTopologyInfoList;
}
