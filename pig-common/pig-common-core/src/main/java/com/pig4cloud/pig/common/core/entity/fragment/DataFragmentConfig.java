package com.pig4cloud.pig.common.core.entity.fragment;

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
 * 数据碎片配置信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hbz_data_fragment_config")
@Schema(description = "数据碎片配置信息 实体")
public class DataFragmentConfig {

    /**
     * 主键 ID，自增生成。
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键 ID", example = "1")
    private Long id;

    /**
     * 监控名称
     */
    @Schema(description = "监控名称", example = "aaa监控")
    private String dataName;

    /**
     * 设备 IP 地址
     */
    @Schema(description = "设备 IP 地址", example = "192.168.2.1")
    private String ip;

    /**
     * 设备 端口号
     */
    @Schema(description = "设备端口号", example = "192.168.2.1")
    private String sqlPort;

    /**
     * 数据库连接用户名
     */
    @Schema(description = "数据库名", example = "public")
    private String dataBase;

    /**
     * 数据库链接密码
     */
    @Schema(description = "密码", example = "123456")
    private String passWord;

    /**
     * 数据库连接用户名
     */
    @Schema(description = "用户名", example = "public")
    private String userName;

    /**
     * 连接类型
     */
    @Schema(description = "连接类型 0=mysql  1=pg 2=瀚高", example = "0")
    private String sqlType;

    /**
     * 查询 数据碎片 SQL
     */
    @Schema(description = "SELECT*", example = "0")
    private String sqlInfo;

    /**
     * 查询数量 TOP 几
     */
    @Schema(description = "查询top几", example = "5")
    private Long topNumber;

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
