package com.pig4cloud.pig.common.core.entity.network;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 表示网络设备信息的数据库实体
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hzb_network_topology_info")
@EntityListeners(AuditingEntityListener.class)
public class NetworkTopologyInfo {

    /**
     * 主键 ID，自增生成。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 关联配置ID
     */
    @Column(name = "config_id", nullable = false, length = 100)
    private String configId;

    /**
     * IP 地址
     */
    @Column(name = "ip", length = 100)
    private String ip;


    /**
     * MAC 地址
     */
    @Column(name = "mac", length = 50)
    private String mac;

    /**
     * 接口名称 ，如类型未VNP 则存入握手时间
     */
    @Column(name = "interface_name", length = 100)
    private String interfaceName;

    /**
     * VLAN ID
     */
    @Column(name = "vlan_id", length = 50)
    private String vlanId;

    /**
     * VLAN 名称
     */
    @Column(name = "vlan_name", length = 100)
    private String vlanName;

    /**
     * VLAN 端口
     */
    @Column(name = "port", length = 100)
    private String port;

    /**
     * 设备类型：0 = 路由器，1 = 防火墙，2 = 交换机，3 = 终端设备，4 = VPN，5 = VLAN。
     */
    @Column(nullable = false, length = 10)
    @Schema(description = "设备类型 0=路由器 1=防火墙 2=交换机 3=设备 4=VPN 5=VLAN 6 = 三层交换机", example = "0")
    private String deviceType;

    /**
     * 创建时间（自动填充）
     */
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间（自动填充）
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
