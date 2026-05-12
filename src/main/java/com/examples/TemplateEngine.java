package com.examples;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateEngine {
    private static Configuration cfg;

    private static void init(HttpServletRequest req) throws IOException {
        if (cfg == null) {
            cfg = new Configuration(Configuration.VERSION_2_3_31);
            // Отримуємо фізичний шлях до папки з шаблонами
            String path = req.getServletContext().getRealPath("/WEB-INF/templates");
            cfg.setDirectoryForTemplateLoading(new File(path));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        }
    }

    // Метод для виклику шаблону з передачею даних
    public static void render(HttpServletRequest req, HttpServletResponse resp, String templateName, Map<String, Object> data) throws IOException {
        init(req);
        resp.setContentType("text/html;charset=UTF-8");

        if (data == null) {
            data = new HashMap<>();
        }

        try {
            Template template = cfg.getTemplate(templateName);
            template.process(data, resp.getWriter());
        } catch (Exception e) {
            throw new IOException("Помилка генерації сторінки", e);
        }
    }
}