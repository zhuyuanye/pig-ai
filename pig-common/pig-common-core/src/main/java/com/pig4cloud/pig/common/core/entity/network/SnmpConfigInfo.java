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
 * 表示 SNMP 网络设备的配置信息实体，用于数据库持久化。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hzb_snmp_config")
@Schema(description = "SNMP 网络设备配置信息实体")
public class  SnmpConfigInfo {

    /**
     * 主键 ID，自增生成。
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键 ID", example = "1")
    private Long id;

    /**
     * 设备 IP 地址。 VPN ip+端口
     */
    @Schema(description = "设备 IP 地址", example = "192.168.2.1")
    private String ip;

    /**
     * SNMP 团体字符串，默认值为 public。  VPN 账号密码 !#!号拼接
     */
    @Schema(description = "SNMP 团体字符串", example = "public")
    private String community;

    /**
     * SNMP 协议版本：0 = V1，1 = V2c。
     */
    @Schema(description = "SNMP 版本：0=V1, 1=V2c", example = "1")
    private Integer version;

    /**
     * 设备类型：0 = 路由器，1 = 防火墙，2 = 交换机，3 = 终端设备，4 = VPN，5 = VLAN。
     */
    @Schema(description = "设备类型 0=路由器 1=防火墙 2=交换机 3=设备 4=VPN 5=VLAN 6 = 三层交换机", example = "0")
    private String deviceType;

    @Schema(description = "是否运行 0 = 关闭 1 = 开启 ", example = "0")
    private String isRunType;

    /**
     * 创建时间。
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
