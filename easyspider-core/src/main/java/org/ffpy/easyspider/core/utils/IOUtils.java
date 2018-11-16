package org.ffpy.easyspider.core.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    public static byte[] inputStream2Bytes(InputStream in) throws IOException {
        ByteOutputStream bos = new ByteOutputStream();
        bos.write(in);
        return bos.toString().getBytes();
    }
}
