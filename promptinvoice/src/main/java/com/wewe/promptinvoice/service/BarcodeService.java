package com.wewe.promptinvoice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BarcodeService {

    public byte[] generateBarcodeImage(String text) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.CODE_128, 300, 100);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
        return outputStream.toByteArray();
    }
}

