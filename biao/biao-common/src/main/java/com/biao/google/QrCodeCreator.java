package com.biao.google;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QrCodeCreator {

    public static void createQrcode(BitMatrix matrix, OutputStream stream) throws IOException {
        MatrixToImageWriter.writeToStream(matrix, "png", stream);
    }

    public static byte[] createQrcode(BitMatrix matrix) throws IOException {
        ByteArrayOutputStream stream = null;
        try {
            stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "png", stream);
            return stream.toByteArray();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static BitMatrix bitMatrix(String content, int width, int height) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        return multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
    }
}
