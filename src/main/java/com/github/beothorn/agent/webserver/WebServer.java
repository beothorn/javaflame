package com.github.beothorn.agent.webserver;

import com.github.beothorn.agent.recorder.FunctionCallRecorder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WebServer {

    public static void start(String directoryPath, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        Path baseDir = Paths.get(directoryPath);

        // Get all files in the directory
        try (Stream<Path> paths = Files.walk(baseDir)) {
            paths.filter(Files::isRegularFile).forEach(filePath -> {
                String urlPath = baseDir.relativize(filePath).toString().replace("\\", "/");
                String contentType = getContentType(filePath.toString());
                serveFileAt("/" + urlPath, contentType, server, filePath);
            });
        }

        serveFileAt("/", "text/html", server, baseDir.resolve("index.html"));
        serveFileAt("/data.js", "application/javascript", server, baseDir.resolve("data.js"));

        server.createContext("/alive", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                FunctionCallRecorder.isRecording = false;
                exchange.sendResponseHeaders(200, 0);
            }
        });

        server.createContext("/start", exec(() -> FunctionCallRecorder.isRecording = true));
        server.createContext("/stop", exec(() -> FunctionCallRecorder.isRecording = false));
        server.createContext("/recording", exec(() -> Boolean.toString(FunctionCallRecorder.isRecording)));

        server.setExecutor(null); // creates a default executor
        Thread serverThread = new Thread(server::start);
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private static HttpHandler exec(){
        return new DoSomething(() -> null);
    }

    private static HttpHandler exec(Runnable runnable){
        return new DoSomething(() -> {
            runnable.run();
            return null;
        });
    }

    private static HttpHandler exec(Supplier<String> runnable){
        return new DoSomething(runnable);
    }

    private static void serveFileAt(final String uriPath, final String contentType, final HttpServer server, final Path baseDir) {
        server.createContext(uriPath, new StaticFileHandler(baseDir, contentType));
    }

    private static String getContentType(String filePath) {
        String contentType = "application/octet-stream";
        if (filePath.endsWith("html")) {
            contentType = "text/html";
        } else if (filePath.endsWith("css")) {
            contentType = "text/css";
        } else if (filePath.endsWith("js")) {
            contentType = "application/javascript";
        } else if (filePath.endsWith("svg")) {
            contentType = "image/svg+xml";
        }
        System.out.println(filePath+" "+contentType);
        return contentType;
    }

    static class DoSomething implements HttpHandler {
        private final Supplier<String> action;
        public DoSomething(Supplier<String> action) {
            this.action = action;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] result;
            int code;
            try{
                String maybeResult = action.get();
                result = maybeResult == null ? new byte[0] : maybeResult.getBytes();
                code = 200;
            } catch (Exception e) {
                result = e.getMessage().getBytes();
                code = 500;
            }

            t.sendResponseHeaders(code, result.length);
            OutputStream os = t.getResponseBody();
            os.write(result);
            os.close();
        }
    }

    static class StaticFileHandler implements HttpHandler {
        private final Path filePath;
        private final String contentType;

        public StaticFileHandler(Path filePath, String contentType) {
            this.filePath = filePath;
            this.contentType = contentType;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] fileBytes = Files.readAllBytes(filePath);
            t.getResponseHeaders().set("Content-Type", contentType);
            t.sendResponseHeaders(200, fileBytes.length);
            OutputStream os = t.getResponseBody();
            os.write(fileBytes);
            os.close();
        }
    }
}