package com.pig4cloud.pig.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.pig4cloud.pig.common.core.entity.dto.Message;
import com.pig4cloud.pig.common.core.util.ResponseUtil;
import com.pig4cloud.pig.monitor.config.RedirectUrlsProperties;
import com.pig4cloud.pig.monitor.config.ThreeServiceProperties;
import com.pig4cloud.pig.monitor.pojo.dto.JumpServerDTO;
import com.pig4cloud.pig.monitor.util.RsaUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static com.pig4cloud.pig.common.core.constants.CommonConstants.FAIL_CODE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/api/sso", produces = {APPLICATION_JSON_VALUE})
public class SsoController {

    @Resource
    private ThreeServiceProperties threeServiceProperties;

    @Resource
    private RedirectUrlsProperties redirectUrlsProperties;

    @PostMapping("/loginToSystem")
    public ResponseEntity<Message<String>> loginToSystem(@RequestBody JumpServerDTO jumpServerDTO) {
        ThreeServiceProperties.ServiceProperties serviceProperties = threeServiceProperties.getServiceMap().get(jumpServerDTO.getSystem());
        if (serviceProperties == null) {
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "系统不存在"));
        }

        String loginUrl = serviceProperties.getLoginUrl();
        String keyUrl = serviceProperties.getKeyUrl();
        String name = serviceProperties.getName();
        String pwd = serviceProperties.getPwd();
        PwdLoginDTO pwdLoginDTO = new PwdLoginDTO();
        pwdLoginDTO.setName(name);
        pwdLoginDTO.setPwd(pwd);
        String key = getKey(keyUrl);
        log.info("public key:{}",key);
        JSONObject jsonObject = JSONObject.parseObject(key);
        String string = jsonObject.getString("data");
        try {
            String s3 = rsaEncrypt(string);
            String s1 = RsaUtils.encryptStr(name, s3);
            pwdLoginDTO.setName(s1);
            String s2 = RsaUtils.encryptStr(pwd, s3);
            pwdLoginDTO.setPwd(s2);
        } catch (Exception e) {
            log.error("用户名密码加密失败",e);
            return ResponseEntity.ok(Message.fail(FAIL_CODE, "登录失败!"));
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建HttpEntity对象，其中包含请求体和头信息
        HttpEntity<PwdLoginDTO> requestEntity = new HttpEntity<>(pwdLoginDTO, headers);

        // 发送POST请求
        String result = restTemplate.postForObject(loginUrl, requestEntity, String.class);
        log.info("loginToSystem：{}", result);
        return ResponseUtil.handle(() -> {
            JSONObject map = JSONObject.parseObject(result);
            Object s = map.get("data");
            JSONObject data = JSONObject.parseObject(s.toString());
            return String.format(serviceProperties.getTargetUrl(), data.getString("token"));
        });
    }

    private String getKey(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        if (forEntity.getStatusCode().is2xxSuccessful()) {
            return forEntity.getBody();
        }
        throw new IllegalArgumentException("登录失败！");
    }

    private String rsaEncrypt(String data) throws Exception {
        String separator = Base64.getUrlEncoder().encodeToString("-pk_separator-".getBytes(StandardCharsets.UTF_8));
        String[] split = data.split(separator);
        String pk = split[0];
        String aesKey = split[1];
        return aesDecrypt(pk, aesKey);
    }

    public static String aesDecrypt(String word, String keyStr) throws Exception {
        String iv = "0000000000000000";
        byte[] keyBytes = keyStr.getBytes("UTF-8");
        byte[] ivBytes = iv.getBytes("UTF-8");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = cipher.doFinal(Base64.getDecoder().decode(word));

        return new String(results, "UTF-8");
    }

    @Data
    private class PwdLoginDTO {

        @Schema(description = "账号(需加密)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "login.validator.name")
        private String name;
        @Schema(description = "密码(需加密)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "login.validator.pwd")
        private String pwd;
        @Hidden
        private Integer origin = 0;

    }

    @Operation(
            summary = "更新某个跳转 URL 配置",
            description = "根据 key 更新某个跳转 URL 值。例如 key=alarmUrl，value=https://new-url.com"
    )
    @PutMapping("/redirect-urls/{key}")
    public ResponseEntity<Message<Map<String, String>>> updateRedirectUrl(@PathVariable String key, @RequestBody String value) {
        Map<String, String> urls = redirectUrlsProperties.getUrls();
        if (!urls.containsKey(key)) {
            return ResponseEntity.notFound().build();
        }
        urls.put(key, value);
        return ResponseEntity.ok(Message.success(urls));
    }


    @Operation(
            summary = "获取所有跳转 URL 配置",
            description = "返回当前配置的所有跳转 URL（以 Map 形式）。"
    )
    @GetMapping("/redirect-urls")
    public ResponseEntity<Message<Map<String, String>>> getRedirectUrls() {
        return ResponseEntity.ok(Message.success(redirectUrlsProperties.getUrls()));
    }



    @Operation(
            summary = "获取指定跳转 URL 配置",
            description = "通过 key 获取某个跳转 URL，比如 alarmUrl、monitoringUrl 等。"
    )
    @GetMapping("/redirect-urls/{key}")
    public ResponseEntity<Message<String>> getRedirectUrlByKey(@PathVariable String key) {
        Map<String, String> urls = redirectUrlsProperties.getUrls();
        if (!urls.containsKey(key)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Message.successWithData(urls.get(key)));
    }


}
