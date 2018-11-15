package org.ffpy.easyspider.core.util;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

    public static byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteOutputStream bos = new ByteOutputStream();
        bos.write(in);
        return bos.getBytes();
    }

    public static String inputStream2String(InputStream in) throws IOException {
        ByteOutputStream bos = new ByteOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while ((n = in.read(buffer)) > 0) {
            bos.write(buffer, 0, n);
        }
        return bos.toString();
    }
}
