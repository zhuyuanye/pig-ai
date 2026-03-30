package com.pig4cloud.pig.common.core.entity.network;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 表示网络设备信息的数据库实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hzb_network_topology_info")
public class NetworkTopologyInfo {

    /**
     * 主键 ID，自增生成。
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联配置ID
     */
    private String configId;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * MAC 地址
     */
    private String mac;

    /**
     * 接口名称 ，如类型未VNP 则存入握手时间
     */
    private String interfaceName;

    /**
     * VLAN ID
     */
    private String vlanId;

    /**
     * VLAN 名称
     */
    private String vlanName;

    /**
     * VLAN 端口
     */
    private String port;

    /**
     * 设备类型：0 = 路由器，1 = 防火墙，2 = 交换机，3 = 终端设备，4 = VPN，5 = VLAN。
     */
    @Schema(description = "设备类型 0=路由器 1=防火墙 2=交换机 3=设备 4=VPN 5=VLAN 6 = 三层交换机", example = "0")
    private String deviceType;

    /**
     * 创建时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后更新时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
