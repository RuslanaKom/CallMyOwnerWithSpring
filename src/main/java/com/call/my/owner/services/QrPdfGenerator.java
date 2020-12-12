package com.call.my.owner.services;

import com.call.my.owner.entities.Stuff;
import com.call.my.owner.exceptions.GeneratePDFException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrPdfGenerator {
    private static final String RECOURSE_INVOICE_FTL = "qr-template.ftl";
    private final FreeMarkerConfigurer freeMarkerConfigurer;
    private final QrWriter qrWriter;

    public QrPdfGenerator(FreeMarkerConfigurer freeMarkerConfigurer, QrWriter qrWriter) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.qrWriter = qrWriter;
    }

    public byte[] generatePdf(Stuff stuff) throws Exception {
        String imgAsBase64 = getBase64String(qrWriter.createStuffQr(stuff));

        Map<String, Object> variables = new HashMap<>();
        variables.put("stuffname", stuff.getStuffName());
        variables.put("headerphrase", "Contact the owner");
        variables.put("imgAsBase64", imgAsBase64);
        String htmlFromTemplate = processTemplate(RECOURSE_INVOICE_FTL, variables);
        byte[] byteArray =  generatePdfFromHtml(htmlFromTemplate);
        OutputStream out = new FileOutputStream("qr/out.pdf");
        out.write(byteArray);
        out.close();
        return byteArray;
    }

    private String getBase64String(byte[] imgBytes) {
        byte[] imgBytesAsBase64 = Base64.encodeBase64(imgBytes);
        String imgDataAsBase64 = new String(imgBytesAsBase64);
        return "data:image/png;base64," + imgDataAsBase64;
    }

    private String processTemplate(String templateName, Map<String, Object> model) {
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName, "UTF-8");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] generatePdfFromHtml(String html) throws GeneratePDFException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            new PdfRendererBuilder()
                    .useFastMode()
                    .withHtmlContent(html, StringUtils.EMPTY)
                    .toStream(outputStream)
                    .run();
            return outputStream.toByteArray();

        } catch (Exception ex) {
            throw new GeneratePDFException(ex.getCause());

        }
    }
}
