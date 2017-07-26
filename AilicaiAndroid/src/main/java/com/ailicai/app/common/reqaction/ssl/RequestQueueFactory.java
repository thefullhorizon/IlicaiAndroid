package com.ailicai.app.common.reqaction.ssl;

import android.content.Context;

import com.ailicai.app.MyApplication;
import com.ailicai.app.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by David on 16/1/29.
 */
public class RequestQueueFactory {

    public static RequestQueue newRequestQueue() {
        Context context = MyApplication.getInstance().getApplicationContext();
        RequestQueue requestQueue;
        try {
            SSLSocketFactory sslSocketFactory = createSSLSocketFactory(context, R.raw.ilicai, "iwjw_android");
            HurlStack stack = new RequestUrlStack(sslSocketFactory);
            requestQueue = Volley.newRequestQueue(context, stack);
        } catch (KeyStoreException
                | CertificateException
                | NoSuchAlgorithmException
                | KeyManagementException
                | IOException e) {
            throw new RuntimeException(e);
        }
        return requestQueue;
    }

    private static SSLSocketFactory createSSLSocketFactory(Context context, int res, String password)
            throws CertificateException,
            NoSuchAlgorithmException,
            IOException,
            KeyStoreException,
            KeyManagementException {
        InputStream inputStream = context.getResources().openRawResource(res);
        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(inputStream, password.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
    }
}
