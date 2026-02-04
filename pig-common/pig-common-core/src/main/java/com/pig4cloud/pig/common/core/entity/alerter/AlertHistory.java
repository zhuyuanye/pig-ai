package com.pig4cloud.pig.common.core.entity.alerter;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * 报警历史记录实体类，用于存储报警的历史数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hzb_alert_history")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "报警历史记录实体")
public class AlertHistory {

    /**
     * 主键ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "主键ID", accessMode = READ_ONLY)
    private Long id;

    /**
     * 报警组件名称，例如“CPU监控”
     */
    @Schema(title = "报警名称", example = "CPU监控")
    @Column(name = "component_name", length = 255, nullable = false)
    private String componentName;

    /**
     * 报警组件名称，例如“CPU监控”
     */
    @Schema(title = "监控组件名称", example = "CPU监控")
    @Column(name = "instance_name", length = 255, nullable = false)
    private String instanceName;

    /**
     * 报警地址，例如“192.168.1.1”
     */
    @Schema(title = "报警地址", example = "127.0.0.1")
    @Column(name = "ip_address", length = 32, nullable = false)
    private String ipAddress;


    /**
     * 报警所属监控ID
     */
    @Schema(title = "报警所属监控ID", example = "1001")
    @Column(name = "monitor_id", nullable = false)
    private Long monitorId;

    /**
     * 报警指纹
     */
    @Schema(title = "报警指纹ID", example = "id")
    @Column(name = "fingerprint_id", length = 64, nullable = false)
    private String fingerprintId;

    /**
     * 报警级别，例如“critical”
     */
    @Schema(title = "报警级别", example = "critical")
    @Column(name = "severity", length = 32, nullable = false)
    private String severity;

    /**
     * 是否恢复，0表示未恢复，1表示已恢复
     */
    @Schema(title = "是否恢复（0未恢复，1已恢复）", example = "1")
    @Column(name = "is_resolved", nullable = false)
    private Integer isResolved;

    /**
     * 报警恢复时间
     */
    @Schema(title = "报警恢复时间")
    @Column(name = "resolved_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolvedTime;


    /**
     * 报警描述，详细说明报警内容
     */
    @Schema(title = "报警描述", example = "CPU使用率超过90%")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 报警时间
     */
    @Schema(title = "记录创建时间")
    @CreatedDate
    @Column(name = "create_alert_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAlertTime;

}
