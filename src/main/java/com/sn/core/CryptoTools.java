package com.sn.core;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by xuhui on 16/6/13.
 */
public class CryptoTools {


    private final byte[] DESIV = {0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};// 设置向量，略去

    private AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
    private Key key = null;


    byte[] getKeyData(String key) throws Exception {
        return key.getBytes("UTF-8");
    }

    public CryptoTools(String key) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(getKeyData(key));// 设置密钥参数
        iv = new IvParameterSpec(DESIV);// 设置向量
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
        this.key = keyFactory.generateSecret(keySpec);// 得到密钥对象
    }

    public String encode(String data) throws Exception {
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
        enCipher.init(Cipher.ENCRYPT_MODE, this.key, iv);// 设置工作模式为加密模式，给出密钥和向量
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        return SNUtility.instance().base64EncodeStr(pasByte);
    }

    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, this.key, iv);
        byte[] pasByte = deCipher.doFinal(SNUtility.instance().base64Decode(data));
        return new String(pasByte, "UTF-8");
    }
}
