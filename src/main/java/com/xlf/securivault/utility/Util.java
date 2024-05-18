/*
 * ******************************************************************************
 *  Copyright (c) 2016-NOW(至今) 筱锋
 *  Author: 筱锋(https://www.x-lf.com)
 *
 *  本文件包含 SecuriValue 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 *  本代码仅进行 Java 大作业提交，个人发行版本计划使用 Go 语言重构。
 * ******************************************************************************
 *  许可证声明：
 *
 *  版权所有 (c) 2016-2024 筱锋。保留所有权利。
 *
 *  本软件是“按原样”提供的，没有任何形式的明示或暗示的保证，包括但不限于
 *  对适销性、特定用途的适用性和非侵权性的暗示保证。在任何情况下，
 *  作者或版权持有人均不承担因软件或软件的使用或其他交易而产生的、
 *  由此引起的或以任何方式与此软件有关的任何索赔、损害或其他责任。
 *
 *  由于作者需要进行 Java 大作业提交，所以请勿抄袭。您可以作为参考，但是
 *  一定不可以抄袭，尤其是同校同学！！！
 *  你们可以自己参考代码优化给你们提供思路，开源目的不是给你们抄袭的，共
 *  同维护好开源的社区环境！！！
 *
 *  使用本软件即表示您了解此声明并同意其条款。
 *
 *  有关MIT许可证的更多信息，请查看项目根目录下的LICENSE文件或访问：
 *  https://opensource.org/licenses/MIT
 * ******************************************************************************
 *  免责声明：
 *
 *  使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 *  对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * ******************************************************************************
 */

package com.xlf.securivault.utility;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * 工具类
 * <hr/>
 * 用于定义一些工具类，用于定义一些工具类；
 *
 * @author xiao_lfeng
 * @version 1.0.0
 * @since 1.0.0
 */
public class Util {

    /**
     * 生成一个 UUID
     * <hr/>
     * 生成一个 UUID; 用于生成数据库所需要的随机 UUID
     *
     * @return UUID
     */
    public static @NotNull UUID generateUuid() {
        return UUID.randomUUID();
    }

    /**
     * 加密密码
     * <hr/>
     * 用于加密密码；用于加密用户的密码；加密方法为 Base64 + BCrypt
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static @NotNull String enPassword(@NotNull String password) {
        String base64Password = Base64.getEncoder().encodeToString(password.getBytes());
        return BCrypt.hashpw(base64Password, BCrypt.gensalt());
    }

    /**
     * 检查密码
     * <hr/>
     * 用于检查密码；用于检查用户的密码是否正确；加密方法为 Base64 + BCrypt
     *
     * @param password     密码
     * @param hashPassword 加密后的密码
     * @return 是否正确
     */
    public static boolean checkPassword(
            @NotNull String password,
            @NotNull String hashPassword
    ) {
        String base64Password = Base64.getEncoder().encodeToString(password.getBytes());
        return BCrypt.checkpw(base64Password, hashPassword);
    }

    /**
     * 生成随机数
     * <hr/>
     * 用于生成随机数；用于生成验证码、随机数等；
     *
     * @param size 验证码长度
     * @return 随机数
     */
    public static @NotNull String generateRandString(int size) {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder randString = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(62);
            randString.append(str.charAt(number));
        }
        return randString.toString();
    }

    /**
     * 替换 Token 中的 Bearer
     * <hr/>
     * 用于替换 Token 中的 Bearer；用于替换 Token 中的 Bearer; 用于获取 Token 本身
     *
     * @param token Token
     * @return 替换后的 Token
     */
    public static @NotNull String tokenReplaceBearer(@NotNull String token) {
        return token.replace("Bearer ", "");
    }

    /**
     * 替换 Token 中的 Bearer
     * <hr/>
     * 用于替换 Token 中的 Bearer；用于替换 Token 中的 Bearer; 用于获取 Token 本身
     *
     * @param request 请求
     * @return 替换后的 Token
     */
    public static @NotNull String tokenReplaceBearer(@NotNull HttpServletRequest request) {
        String getAuthorization = request.getHeader("Authorization");
        if (getAuthorization != null && !getAuthorization.isBlank()) {
            return getAuthorization.replace("Bearer ", "");
        } else {
            return "";
        }
    }

    /**
     * 加密密码库
     * <hr/>
     * 用于加密密码库；用于加密密码库；加密方法为 Base64 + Base64
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String passwordLibraryEncode(@NotNull String password) {
        String base64Password = Base64.getEncoder().encodeToString(password.getBytes());
        return Base64.getEncoder().encodeToString(base64Password.getBytes());
    }

    /**
     * 解密密码库
     * <hr/>
     * 用于解密密码库；用于解密密码库；解密方法为 Base64 + Base64
     *
     * @param password 密码
     * @return 解密后的密码
     */
    public static @NotNull String passwordLibraryDecode(@NotNull String password) {
        String base64Password = new String(Base64.getDecoder().decode(password));
        return new String(Base64.getDecoder().decode(base64Password));
    }

    /**
     * 获取用户 UUID
     * <hr/>
     * 用于获取用户 UUID；用于获取用户 UUID；用于获取用户 UUID
     *
     * @param request 请求
     * @return 用户 UUID
     */
    public static String getUserUuid(@NotNull HttpServletRequest request) {
        return request.getHeader("X-User-Uuid");
    }

    /**
     * 获取用户 UUID
     * <hr/>
     * 用于获取用户 UUID；用于获取用户 UUID；用于获取用户 UUID
     *
     * @param key 用户 UUID
     * @return 用户 UUID
     */
    public static String maskKey(@NotNull String key) {
        if (key.length() <= 5) {
            String start = key.substring(0, 2);
            String middle = key.substring(2);
            middle = middle.replaceAll("\\.", "*");
            return start + middle;
        } else {
            String start = key.substring(0, 3);
            String end = key.substring(key.length() - 2);
            String middle = key.substring(3, key.length() - 2);
            middle = middle.replaceAll("\\.", "*");
            return start + middle + end;
        }
    }
}
