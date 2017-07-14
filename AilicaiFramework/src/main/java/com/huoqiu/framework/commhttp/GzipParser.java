package com.huoqiu.framework.commhttp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipParser {

    public static byte[] parse(byte[] data) {
        if (isInputStreamGZIPCompressed(data)) {
            InputStream sbs = new ByteArrayInputStream(data); 
            GZIPInputStream gzippedStream = null;
            try {
                gzippedStream = new GZIPInputStream(sbs);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parseGZIPInputStream(gzippedStream);
        } else {
            return data;
        }
    }
    
    /**
     * Checks the InputStream if it contains GZIP compressed data
     * 
     * @return true or false if the stream contains GZIP compressed data
     * @throws IOException
     */
    private static boolean isInputStreamGZIPCompressed(byte[] data) {
        if (data == null){
            return false;
        }
        if (data.length < 2) {
            return false;
        }
        byte[] signature = new byte[2];
        signature[0] = data[0];
        signature[1] = data[1];
        int streamHeader = ((int) signature[0] & 0xff) | ((signature[1] << 8) & 0xff00);
        return GZIPInputStream.GZIP_MAGIC == streamHeader;
    }

    private static byte[] parseGZIPInputStream(GZIPInputStream gzipInputStream) {
        if (gzipInputStream == null) {
            return null;
        }
        byte[] result;
        byte[] buf = new byte[1024];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            int num = -1;
            while ((num = gzipInputStream.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            result = baos.toByteArray();
            baos.flush();
            baos.close();
            gzipInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                baos.close();
                gzipInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        return result;
    }
}
