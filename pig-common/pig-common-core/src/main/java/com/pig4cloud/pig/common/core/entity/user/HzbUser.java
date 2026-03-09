package com.pig4cloud.pig.common.core.entity.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 表示系统中的用户实体，映射到数据库中的 "hzb_user" 表。
 * 该实体用于存储与用户相关的信息，并通过 Spring Data JPA 的审计机制记录创建和更新时间。
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hzb_user")
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "表示系统用户的实体")
public class HzbUser {

    /**
     * 主键ID，由数据库自动生成。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户名，唯一且不能为空，用作用户的登录名。
     */
    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "用户名，唯一", example = "john_doe", accessMode = Schema.AccessMode.READ_WRITE)
    private String username;

    /**
     * 加密后的密码，存储为写入模式以确保安全性。
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "加密后的密码", example = "encrypted_password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    /**
     * 邮箱地址，可选字段，用于账户验证和密码找回。
     */
    @Column(length = 100)
    @Schema(description = "邮箱地址", example = "john@example.com", accessMode = Schema.AccessMode.READ_WRITE)
    private String email;

    /**
     * 手机号码，可选字段，用于联系用户。
     */
    @Column(length = 20)
    @Schema(description = "手机号码", example = "+1234567890", accessMode = Schema.AccessMode.READ_WRITE)
    private String phone;

    /**
     * 账户状态，1 表示激活，0 表示禁用。
     */
    @Column(nullable = false)
    @Schema(description = "账户状态 (1=激活, 0=禁用)", example = "1", accessMode = Schema.AccessMode.READ_WRITE)
    private Short status; // 注意：SQL中定义的是 SMALLINT，所以这里是 Short 类型

    /**
     * 创建时间，自动生成，不可手动修改。
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间", example = "2024-04-28T20:09:34.903217", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    /**
     * 用户权限
     */
    @Column(name = "role", nullable = false)
    @Schema(description = "用户权限", example = "user", accessMode = Schema.AccessMode.READ_WRITE)
    private String role;
    /**
     * 最后更新时间，自动更新，不可手动修改。
     */
    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    @Schema(description = "最后更新时间", example = "2024-04-28T22:10:00.123456", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;
}

