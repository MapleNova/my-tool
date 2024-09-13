package com.example.mytool.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * 将源字符串使用MD5加密为字节数组
     */
    public static byte[] encode2bytes(String source) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(source.getBytes(StandardCharsets.UTF_8));
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将源字符串使用MD5加密为32位16进制数
     */
    public static String encode2hex(String source) {
        byte[] data = encode2bytes(source);

        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 验证字符串是否匹配
     *
     * @param unknown 待验证的字符串
     * @return 匹配返回true，不匹配返回false
     */
    public static boolean validate(String unknown, String known) {
        return known.equals(encode2hex(unknown));
    }

    public static String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String md516(String source) {
        return source.substring(8, 24);
    }
}
