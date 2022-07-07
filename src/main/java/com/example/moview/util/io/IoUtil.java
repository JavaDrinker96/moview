package com.example.moview.util.io;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class IoUtil {

    static final Logger log = (Logger) LogManager.getRootLogger();

    public static String readJsonRequest(final HttpServletRequest request) {
        log.info("Reading a request in json string format.");
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            final StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            br.close();
            return jsonBuilder.toString();
        } catch (IOException e) {
            log.error("Could not get the data stream from the request.");
            throw new RuntimeException(e);
        }
    }

    public static void printJsonResponse(final String json, final int status, final HttpServletResponse response) {
        final PrintWriter out;
        try {
            log.info("Getting the writer from the servlet response.");
            out = response.getWriter();
            log.info("The writer from the servlet response was successfully received");
        } catch (IOException e) {
            log.error("Writer could not be received from the servlet response");
            throw new RuntimeException(e);
        }
        response.setContentType("application/json");
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        out.print(json);
        out.flush();
    }
}
