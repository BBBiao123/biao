package com.biao.fileserver.util;

import com.mongodb.MongoGridFSException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteInputStreamUtils {

    public static byte[] getInputStreamByte(InputStream input) {
        ByteArrayOutputStream uploadStream = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            uploadStream = new ByteArrayOutputStream();
            while ((len = input.read(buffer)) != -1) {
                uploadStream.write(buffer, 0, len);
            }
            return uploadStream.toByteArray();
        } catch (IOException e) {
            throw new MongoGridFSException("IOException when reading from the InputStream", e);
        } finally {
            if (uploadStream != null) {
                try {
                    uploadStream.close();
                } catch (IOException e) {
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
