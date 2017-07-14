package com.ailicai.app.setting;

import android.text.TextUtils;
import android.util.Base64;

import com.ailicai.app.MyApplication;
import com.ailicai.app.common.utils.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Ted on 14-7-28.
 */
public class ServerIPManger {
    public static final String CONFIG_FILE_NAME = "iwjwconfig";

    public static ServerIPModel GetServerIP() {
        String serverIP = getFromAssets(CONFIG_FILE_NAME);
        if (TextUtils.isEmpty(serverIP)) {
            //LogUtil.e("读取的服务器地址内容为空");
            return null;
        } else {

            byte[] base64Bytes = Base64.decode(serverIP, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);

            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(bais);
                ServerIPModel ipModel = (ServerIPModel) ois.readObject();

                return ipModel;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(MyApplication.getInstance().getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * author david
     * 生成ServerModelIp 序列化后的BASE64密文.
     * 备用
     */
    public static String generateIwjwConfig() {
        ServerIPModel model = new ServerIPModel();

        model.mIPType = 0;
        model.mServerProtocol = "https";
        model.mServerHostname = "poros.iwlicaitest.com";
        model.mServerPort = 443;
        model.mServerPath = "";
        model.mPushServerIP = "";
        model.mPushServerPort = 0;


        String privacy = "";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(model);
            privacy = Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);

            LogUtil.d("ManyiServerIp", privacy);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return privacy;
    }

}
