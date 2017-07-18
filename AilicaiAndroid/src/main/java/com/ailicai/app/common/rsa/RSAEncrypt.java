package com.ailicai.app.common.rsa;

import android.text.TextUtils;


import com.ailicai.app.common.rsa.base64.BASE64Decoder;
import com.ailicai.app.common.rsa.base64.BASE64Encoder;
import com.ailicai.app.ui.login.UserInfo;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA 加密解密共通类
 */
public class RSAEncrypt {
    private static int keySize = 1024;// 明文最大长度不能超过117个字节
    private static String defualtCode = "UTF-8";

    public static String byte2base64(byte[] bytes) throws IOException {
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] base642byte(String base64Str) throws IOException {
       return new BASE64Decoder().decodeBuffer(base64Str);
    }

    public static KeyPair getKeyPair() throws Exception {
        KeyPair kp = null;
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(keySize);
        kp = kpg.genKeyPair();
        return kp;
    }

    public static String getPublicKey(KeyPair kp) throws Exception {
        PublicKey pbkey = kp.getPublic();
        byte[] bytes = pbkey.getEncoded();
        return byte2base64(bytes);
    }

    public static String getPrivateKey(KeyPair kp) throws Exception {
        PrivateKey prkey = kp.getPrivate();
        byte[] bytes = prkey.getEncoded();
        return byte2base64(bytes);
    }

    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] keyBytes = base642byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey string2PrivateKey(String pubStr) throws Exception {
        byte[] keyBytes = base642byte(pubStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    private static String encrypt(String content, String publicKeyStr) {
        String str = "";

        if (content != null && content.length() > 0) {
            try {
                byte[] bytes = content.getBytes(defualtCode);
                str = byte2base64(publicEncrypt(bytes, string2PublicKey(publicKeyStr)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * RSA加密接口.
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        //rsa 关闭  返回原值.
        if (UserInfo.getRSAClosed() == 1) return content;

        String publicKey = UserInfo.getPublicKey();
        if (TextUtils.isEmpty(publicKey)) return "";

        return RSAEncrypt.encrypt(content, publicKey);
    }

    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    public static String decrypt(String content, String privateKeyStr) {
        String str = "";
        if (content != null && content.length() > 0) {
            try {
                str = new String(privateDecrypt(base642byte(content), string2PrivateKey(privateKeyStr)), defualtCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static void main(String args[]) throws Exception {
        String str = "111111";
        byte[] content = str.getBytes(defualtCode);
//        KeyPair kp = getKeyPair();
//        String publicKeyStr = getPublicKey(kp);
        String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvuhePXXSrIjRmQZUvN9OsnActly5DVsM3zl4nh9gZWkJoJwRgAGIP9JVlUJ+Qrj+eW2hbiAYyhVEG9fZVX9oLSWN/LCvivblhV/KDUh66CFdmStDw976vZb7Frnog37AXUJALdAv6UVs/nRjFtLDwETI5raQfqkXOAnNZ3wmznQIDAQAB";
        System.out.print("publicKeyStr:" + publicKeyStr);
//        String privateKeyStr = getPrivateKey(kp);
        String privateKeyStr = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK+6F49ddKsiNGZBlS8306ycBy2XLkNWwzfOXieH2BlaQmgnBGAAYg/0lWVQn5CuP55baFuIBjKFUQb19lVf2gtJY38sK+K9uWFX8oNSHroIV2ZK0PD3vq9lvsWueiDfsBdQkAt0C/pRWz+dGMW0sPARMjmtpB+qRc4Cc1nfCbOdAgMBAAECgYEAnE+0VVqURQYQBkWgJnhKWegQmoU4Kb4ruKBhMFit6R9YorzSL3Mnx3mQyqaEyXW2MW6tzX2ZxabBBKgVjqvMj3IfS3p8rBkOgumLMEVDutamugFncT783i2oNXj1DThLQVwxHh0ZcccONax3wJ4NPV927+pan/wyuY2vK3hGVQECQQDsF9Att7LO/cpqQk1PjLnMYZarut8hhbYvvzoAHE4NSAFtoEK0gtjWTbc4QZllpMnJOceFOLquYQmyWh5Ue7NZAkEAvos/BgGhjm1V1JP1J/eidX98my4obtcTX1rEaaOaD9BG5Y+D1DHVxWPmN5pfwBLPNKokO0YTNDtf8RDdx2TN5QJAGID4OGaChn1Mzu6Gu6Tte8r9KmHA2ufX2ujMCkrlxvccPtaNVdLm0odKZupYE3ahAuOeU7NpnMOxj/NOhpI4MQJBAIXDy2K0Em6iYvbxR9HfDrXd4eQu60OkyXzPg8OFjso7NQpDThDRS3lfQGcYgM+eZMhcCUNzVlf8tsXNSWSJZ+ECQQC72AB9fT/0s5X/Xoe54xmVdHdsmQgg0o+sRwNqIQ5dqJQwOXmkWTSngqgIYAAhrYOyZ+NISNueFoq47e3Igt8M";
        System.out.println();
        System.out.print("privateKeyStr:" + privateKeyStr);
        String encryptStr = byte2base64(publicEncrypt(content, string2PublicKey(publicKeyStr)));
        System.out.println();
        System.out.println();
        System.out.print("encryptStr:" + encryptStr);
        byte[] decryptBytes = privateDecrypt(base642byte(encryptStr), string2PrivateKey(privateKeyStr));
        System.out.println();
        System.out.println();
        System.out.print("decryptStr:" + new String(decryptBytes, defualtCode));
        System.out.println();
        System.out.println();
        System.out.println();


        String key1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOLengHnHko/+RJdof6ow3vhF3fDV1Ibn3AR/qRgbJbN+K/4CHWDpTGNFtXx/q4qC+IQDu7xvcA6B1OXM4jMm+rHKbTBZIS3BPWhZbmZD0MExYPHRC6E3678mjedA9+eNTLiWn3+x6dXG2h0zD2ob5idSnYc59IViciVsyaQuDaQIDAQAB";
        String key2 = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI4t6eAeceSj/5El2h/qjDe+EXd8NXUhufcBH+pGBsls34r/gIdYOlMY0W1fH+rioL4hAO7vG9wDoHU5cziMyb6scptMFkhLcE9aFluZkPQwTFg8dELoTfrvyaN50D3541MuJaff7Hp1cbaHTMPahvmJ1Kdhzn0hWJyJWzJpC4NpAgMBAAECgYAFz9yqm7P+K7ILxedXvKfEs5FVOA+bXSiT70jVs80dOMUeknk3jyS7Nt2Awg209VSq0QCPw9h4svFBOXr7Sc4JXCa3tPGfXsCncxbSv5EaGEE6N46cAmI9aD299VEaYG4ao4i597pS2G8imG33cHyJNaHR9VEnYhg9mFJTRvaWgQJBAPqg2TRh+qjQkG39knHQfbgomgXBDuf+4Hb2XFTJT/1EtD3+t43jdEpqk+XkScmyoYcgpDtZqtjtywvA/Cdq3BECQQCROgaLZMTbGLP1JhV4CO2YmTv9GFhQXOI85Wo1dQqy5f8CLXv7n3klxicpz7Eo0R+195xphwGPc+0NnbC79GnZAkB5JNKYjVuZyeZmJBFrTjnWkFmSunS4Euzw07hhi0VMyK7O4JZc0Tre1ZFTp/s6sUt+g3qe7YK1hIuGzOuVKgPhAkBHUgDOyiEnHD2cgiR++5t7mp1sUV1lsgflMiFzuVQUwYD319CQpSPA2kx3ayksy27QVZb5+DRa9+qOCkg2NfUZAkB1vB9m3SLfLIFZYTqFbp1jDtJPf8BPa6e1x3St39Rkl879XVKzM4UaXtPtHqjKGcMzTpJyEETd7uJRvTGKXrAX";


        System.out.print("EncryptStr:" + encrypt(str, publicKeyStr));
    }
}
