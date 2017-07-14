package com.ailicai.app.common.reqaction.ssl;


import android.support.annotation.Nullable;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.internal.huc.OkHttpURLConnection;
import okhttp3.internal.huc.OkHttpsURLConnection;

/**
 * A HttpStack implement witch can verify specified self-signed certification.
 */
public class RequestUrlStack extends HurlStack {

    private OkHttpClient okHttpClient;

    private SSLSocketFactory socketFactory;

    /**
     * Create a OkHttpStack with default OkHttpClient.
     */
    public RequestUrlStack(SSLSocketFactory factory) {
        this(new OkHttpClient.Builder().build(), factory);
    }

    /**
     * Create a OkHttpStack with a custom OkHttpClient
     *
     * @param okHttpClient Custom OkHttpClient, NonNull
     */
    public RequestUrlStack(OkHttpClient okHttpClient, SSLSocketFactory factory) {
        this.okHttpClient = okHttpClient;
        this.socketFactory = factory;
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        if ("https".equals(url.getProtocol())) {
            OkHttpsURLConnection connection = new OkHttpsURLConnection(url, okHttpClient);//(HttpsURLConnection) new OkUrlFactory(okHttpClient).open(url);
            connection.setSSLSocketFactory(socketFactory);
            return connection;
        } else {
            return new OkHttpURLConnection(url, okHttpClient);
        }
    }

    private static void copy(InputStream in, OutputStream out, byte[] buf) throws IOException {
        if (in == null) {
            return;
        }
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
    }

    @Nullable
    private static InputStream applyDecompressionIfApplicable(
            HttpURLConnection conn, @Nullable InputStream in) throws IOException {
        if (in != null && GZIP_ENCODING.equals(conn.getContentEncoding())) {
            return new GZIPInputStream(in);
        }
        return in;
    }

    private static final String GZIP_ENCODING = "gzip";
}
