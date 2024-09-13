package com.example.mytool.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class AesUtils {

    /**
     * AES加密
     * 加密方式：AES/CBC/PKCS5Padding
     * @param data 需要加密的原始数据
     * @param key  密钥
     * @param iv   初始化向量
     * @return 加密结果用base64编码，String类型，出错返回null
     */
    public static String encryptAES(String data, String key, String iv) {

        String encryptAES = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            //加密方式AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] source = cipher.doFinal(dataBytes);
            byte[] encode = Base64.getEncoder().encode(source);
            encryptAES = new String(encode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("---> AES加密错误：{}", e.getMessage());
        }
        return encryptAES;
    }


    /**
     * AES解密
     * 解密方式：AES/CBC/PKCS5Padding
     * @param data 需要解密原始数据
     * @param key  密钥
     * @param iv   初始化向量
     * @return 解密结果，String类型，出错返回null
     */
    public static String decryptAES(String data, String key, String iv) {
        String decryptData = null;
        try {
            //先用base64解码
            byte[] decode = Base64.getDecoder().decode(data);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            //解密方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] source = cipher.doFinal(decode);
            decryptData = new String(source, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("---> AES解密错误：{}", e.getMessage());
        }
        return decryptData;
    }

}
