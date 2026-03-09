package com.pig4cloud.pig.common.core.entity.fragment;

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
 * 数据碎片配置信息
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hbz_data_fragment_config")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "数据碎片配置信息 实体")
public class DataFragmentConfig {

    /**
     * 主键 ID，自增生成。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键 ID", example = "1")
    private Long id;


    /**
     * 监控名称
     */
    @Column(length = 100)
    @Schema(description = "监控名称", example = "aaa监控")
    private String dataName;

    /**
     * 设备 IP 地址
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "设备 IP 地址", example = "192.168.2.1")
    private String ip;


    /**
     * 设备 端口号
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "设备端口号", example = "192.168.2.1")
    private String sqlPort;

    /**
     * 数据库连接用户名
     */
    @Column(length = 100)
    @Schema(description = "数据库名", example = "public")
    private String dataBase;


    /**
     * 数据库链接密码
     */
    @Column(length = 100)
    @Schema(description = "密码", example = "123456")
    private String passWord;

    /**
     * 数据库连接用户名
     */
    @Column(length = 100)
    @Schema(description = "用户名", example = "public")
    private String userName;


    /**
     * 连接类型
     */
    @Column(length = 10)
    @Schema(description = "连接类型 0=mysql  1=pg 2=瀚高", example = "0")
    private String sqlType;

    /**
     * 查询 数据碎片 SQL
     */
    @Column(length = 1024)
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
    @CreatedDate
    @Column(name = "create_time",  updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间。
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

