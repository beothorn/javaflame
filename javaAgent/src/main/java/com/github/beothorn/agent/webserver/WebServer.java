package com.github.beothorn.agent.webserver;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.InetSocketAddress;
import java.util.stream.Stream;

public class WebServer {

    public static void start(String directoryPath, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

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

        server.setExecutor(null); // creates a default executor
        Thread serverThread = new Thread(() -> {
            server.start();
        });
        serverThread.setDaemon(true);
        serverThread.start();
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