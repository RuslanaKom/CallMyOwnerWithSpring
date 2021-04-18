package com.call.my.owner.services;

import com.call.my.owner.entities.Stuff;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

@Service
public class QrWriter {

    private final String backUrl;

    public QrWriter(@Value("${app.back.url}") String backUrl) {
        this.backUrl = backUrl;
    }

    public byte[] createStuffQr(Stuff stuff) {
        String url = backUrl + "/loststuff/contact/" + stuff.getId();
        return createQRCode(url);
    }

    private byte[] createQRCode(String url) {
        int size = 250;
        try {
            Map<EncodeHintType, Object> hintMap = setupHintMap();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, size, size, hintMap);
            int width = byteMatrix.getWidth();

            BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = setupGraphics2D(width, image);
            fillInGraphics(byteMatrix, width, graphics);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<EncodeHintType, Object> setupHintMap() {
        Map<EncodeHintType, Object> hintMap = new EnumMap(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.MARGIN, 1);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        return hintMap;
    }

    private Graphics2D setupGraphics2D(int width, BufferedImage image) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, width);
        graphics.setColor(Color.BLACK);
        return graphics;
    }

    private void fillInGraphics(BitMatrix byteMatrix, int width, Graphics2D graphics) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
    }
}
