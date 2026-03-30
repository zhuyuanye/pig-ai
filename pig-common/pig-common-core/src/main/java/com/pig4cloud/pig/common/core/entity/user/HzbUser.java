package com.pig4cloud.pig.common.core.entity.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 表示系统中的用户实体，映射到数据库中的 "hzb_user" 表。
 * 该实体用于存储与用户相关的信息，通过 MyBatis Plus 自动填充机制记录创建和更新时间。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("hzb_user")
@Schema(description = "表示系统用户的实体")
public class HzbUser {

    /**
     * 主键ID，由数据库自动生成。
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户名，唯一且不能为空，用作用户的登录名。
     */
    @Schema(description = "用户名，唯一", example = "john_doe", accessMode = Schema.AccessMode.READ_WRITE)
    private String username;

    /**
     * 加密后的密码，存储为写入模式以确保安全性。
     */
    @Schema(description = "加密后的密码", example = "encrypted_password", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    /**
     * 邮箱地址，可选字段，用于账户验证和密码找回。
     */
    @Schema(description = "邮箱地址", example = "john@example.com", accessMode = Schema.AccessMode.READ_WRITE)
    private String email;

    /**
     * 手机号码，可选字段，用于联系用户。
     */
    @Schema(description = "手机号码", example = "+1234567890", accessMode = Schema.AccessMode.READ_WRITE)
    private String phone;

    /**
     * 账户状态，1 表示激活，0 表示禁用。
     */
    @Schema(description = "账户状态 (1=激活, 0=禁用)", example = "1", accessMode = Schema.AccessMode.READ_WRITE)
    private Short status; // 注意：SQL中定义的是 SMALLINT，所以这里是 Short 类型

    /**
     * 创建时间，自动生成，不可手动修改。
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间", example = "2024-04-28T20:09:34.903217", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    /**
     * 用户权限
     */
    @Schema(description = "用户权限", example = "user", accessMode = Schema.AccessMode.READ_WRITE)
    private String role;
    /**
     * 最后更新时间，自动更新，不可手动修改。
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "最后更新时间", example = "2024-04-28T22:10:00.123456", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;
}
