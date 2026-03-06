package com.pig4cloud.pig.monitor.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户参数类，通常用于创建或更新用户时传递请求体数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用于创建/更新用户的参数类")
public class HzbUserParam {

    /**
     * 用户名，唯一
     * 必须非空，且最大长度为50个字符
     */
    @NotEmpty(message = "用户名不能为空")
    @Size(max = 50, message = "用户名不能超过50个字符")
    @Schema(description = "用户名，唯一", example = "john_doe", required = true)
    private String username;

    /**
     * 密码（加密存储）
     * 必须非空，且密码长度在6到100个字符之间
     */
    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码必须在6到100个字符之间")
    @Schema(description = "密码（加密存储）", example = "encrypted_password", required = true)
    private String password;

    /**
     * 邮箱地址
     * 必须符合邮箱格式，并且最大长度为100个字符
     */
    @Email(message = "邮箱格式无效")
    @Size(max = 100, message = "邮箱不能超过100个字符")
    @Schema(description = "用户的邮箱地址", example = "john@example.com")
    private String email;

    /**
     * 手机号码
     * 最大长度为20个字符
     */
    @Size(max = 20, message = "手机号码不能超过20个字符")
    @Schema(description = "用户的手机号码", example = "+1234567890")
    private String phone;

    /**
     * 账号状态
     * 1表示正常，0表示禁用
     */
    @Schema(description = "账号状态（1=正常，0=禁用）", example = "1")
    private Short status;

}
