package com.call.my.owner.services;

import com.call.my.owner.exceptions.GeneratePDFException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrPdfGenerator {
    private static final String RECOURSE_INVOICE_FTL = "qr-template.ftl";
    private final FreeMarkerConfigurer freeMarkerConfigurer;

    public QrPdfGenerator(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }

    public void generatePdf() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("headerphrase", "Contact the owner");
        String htmlFromTemplate = processTemplate(RECOURSE_INVOICE_FTL, variables);
        byte[] byteArray =  generatePdfFromHtml(htmlFromTemplate);
        OutputStream out = new FileOutputStream("qr/out.pdf");
        out.write(byteArray);
        out.close();
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
