/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.monitor.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JWT Token 工具类 - 替代 sureness 的 JsonWebTokenUtil
 */
public class JwtTokenUtil {

    private static volatile String secretKey = "CyaFv0bwq2Eik0jdrKUtsA6bx3sDJeFV643R"
            + "LnfKefTjsIfJLBa2YkhEqEGtcHDTNe4CU6+9"
            + "8tVt4bisXQ13rbN0oxhUZR73M6EByXIO+SV5"
            + "dKhaX0csgOCTlCxq20yhmUea6H6JIpSE2Rwp";

    public static void setDefaultSecretKey(String key) {
        secretKey = key;
    }

    public static String getDefaultSecretKey() {
        return secretKey;
    }

    private static SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        // Ensure key is at least 256 bits for HS256
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            keyBytes = padded;
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Issue a JWT token
     *
     * @param subject          subject (username)
     * @param expirationSeconds expiration time in seconds, null for no expiration
     * @param roles            user roles
     * @param customClaims     additional custom claims
     * @return JWT token string
     */
    public static String issueJwt(String subject, Long expirationSeconds, List<String> roles,
                                   Map<String, Object> customClaims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(subject)
                .issuedAt(now);

        if (roles != null && !roles.isEmpty()) {
            builder.claim("roles", String.join(",", roles));
        }

        if (customClaims != null) {
            for (Map.Entry<String, Object> entry : customClaims.entrySet()) {
                builder.claim(entry.getKey(), entry.getValue());
            }
        }

        if (expirationSeconds != null) {
            long expMillis = nowMillis + expirationSeconds * 1000;
            builder.expiration(new Date(expMillis));
        }

        builder.signWith(getSigningKey());
        return builder.compact();
    }

    /**
     * Parse a JWT token
     *
     * @param token JWT token string
     * @return Claims
     */
    public static Claims parseJwt(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
