package com.example.mytool.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 数字签名工具
 */
@Slf4j
public class HMacUtils {

    /**
     * 计算参数的md5信息
     * @param str 待处理的字节数组
     * @return md5摘要信息
     */
    private static byte[] md5(byte[] str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("--> MD5算法不存在：{}", e.getMessage());
            return null;
        }
    }

    private static  String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    /**
     * 将待加密数据data，通过密钥key，使用hmac-md5算法进行加密，然后返回加密结果。 参照rfc2104 HMAC算法介绍实现。
     * @param key  密钥
     * @param data 待加密数据
     * @return 加密结果
     */
    public static byte[] getHmacMd5Bytes(byte[] key, byte[] data) {

        //HmacMd5 计算公式：H(K XOR opad, H(K XOR ipad, text))
        //H代表hash算法，本类中使用MD5算法，K代表密钥，text代表要加密的数据 ipad为0x36，opad为0x5C。
        int length = 64;
        byte[] ipad = new byte[length];
        byte[] opad = new byte[length];
        for (int i = 0; i < 64; i++) {
            ipad[i] = 0x36;
            opad[i] = 0x5C;
        }
        byte[] actualKey = key;
        byte[] keyArr = new byte[length];

        //如果密钥长度，大于64字节，就使用哈希算法，计算其摘要，作为真正的密钥。
        if (key.length > length) {
            actualKey = md5(key);
        }
        if (actualKey == null) {
            return null;
        }
        System.arraycopy(actualKey, 0, keyArr, 0, actualKey.length);

        //如果密钥长度不足64字节，就使用0x00补齐到64字节。
        if (actualKey.length < length) {
            for (int i = actualKey.length; i < keyArr.length; i++)
                keyArr[i] = 0x00;
        }

        //使用密钥和ipad进行异或运算。
        byte[] kIpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kIpadXorResult[i] = (byte) (keyArr[i] ^ ipad[i]);
        }


        //将待加密数据追加到K XOR ipad计算结果后面。
        byte[] firstAppendResult = new byte[kIpadXorResult.length + data.length];
        System.arraycopy(kIpadXorResult, 0, firstAppendResult, 0, kIpadXorResult.length);
        System.arraycopy(data, 0, firstAppendResult, keyArr.length, data.length);

        //使用哈希算法计算上面结果的摘要。
        byte[] firstHashResult = md5(firstAppendResult);

        //使用密钥和opad进行异或运算。
        byte[] kOpadXorResult = new byte[length];
        for (int i = 0; i < length; i++) {
            kOpadXorResult[i] = (byte) (keyArr[i] ^ opad[i]);
        }

        //将H(K XOR ipad, text)结果追加到K XOR opad结果后面
        if (firstHashResult == null) {
            return null;
        }
        byte[] secondAppendResult = new byte[kOpadXorResult.length + firstHashResult.length];
        System.arraycopy(kOpadXorResult, 0, secondAppendResult, 0, kOpadXorResult.length);
        System.arraycopy(firstHashResult, 0, secondAppendResult, keyArr.length, firstHashResult.length);

        return md5(secondAppendResult);
    }

    /**
     * 字节转16进制
     * @param src 源数据
     * @return string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String encrypt(String key, String data) {
        byte[] hmacMd5Bytes = HMacUtils.getHmacMd5Bytes(key.getBytes(), data.getBytes());
        String string = HMacUtils.bytesToHexString(hmacMd5Bytes);
        if (string != null) {
            return string.toUpperCase();
        } else {
            return null;
        }
    }

}
