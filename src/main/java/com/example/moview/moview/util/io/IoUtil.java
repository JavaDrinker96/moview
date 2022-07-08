package com.example.moview.moview.util.io;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class IoUtil {

    private static final Logger log = (Logger) LogManager.getRootLogger();

    private IoUtil() {
        throw new UnsupportedOperationException(String.format("Attempt to create %S", this.getClass().getSimpleName()));
    }

    public static String readJsonRequest(final HttpServletRequest request) throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            final StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            br.close();
            return jsonBuilder.toString();
        }
    }

    public static void printJsonResponse(final String json, final int status, final HttpServletResponse response)
            throws IOException {

        final PrintWriter out;
        log.info("Getting the writer from the servlet response.");
        out = response.getWriter();
        response.setContentType("application/json");
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }
}
