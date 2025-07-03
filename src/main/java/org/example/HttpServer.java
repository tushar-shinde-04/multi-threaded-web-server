package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.concurrent.*;

public class HttpServer {

    public static void main(String[] args) {
        final int PORT = 8081;
        final int THREAD_POOL_SIZE = 10;

        /*ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }*/

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Virtual-threaded HTTP Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Each client handled by a new virtual thread
                executor.submit(() -> handleClient(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            System.out.println("Received: " + requestLine);
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String path = tokens[1];

            if (!method.equals("GET")) {
                sendResponse(out, 405, "Method Not Allowed", "Only GET is supported.");
                return;
            }

            String filePath = path.equals("/") ? "index.html" : path.substring(1);
            File file = new File("public", filePath);

            if (file.exists() && !file.isDirectory()) {
                byte[] content = Files.readAllBytes(file.toPath());
                String header = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + getMimeType(filePath) + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "\r\n";
                out.write(header.getBytes());
                out.write(content);
            } else {
                File notFoundFile = new File("public", "404.html");
                if (notFoundFile.exists()) {
                    byte[] content = Files.readAllBytes(notFoundFile.toPath());
                    String header = "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Type: text/html\r\n" +
                            "Content-Length: " + content.length + "\r\n" +
                            "\r\n";
                    out.write(header.getBytes());
                    out.write(content);
                } else {
                    sendResponse(out, 404, "Not Found", "File not found.");
                }
            }

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendResponse(OutputStream out, int statusCode, String statusMessage, String body) throws IOException {
        String response = "<html><body><h1>" + statusCode + " " + statusMessage + "</h1><p>" + body + "</p></body></html>";
        String header = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + response.length() + "\r\n" +
                "\r\n";
        out.write(header.getBytes());
        out.write(response.getBytes());
    }

    private static String getMimeType(String fileName) {
        if (fileName.endsWith(".html")) return "text/html";
        else if (fileName.endsWith(".css")) return "text/css";
        else if (fileName.endsWith(".js")) return "application/javascript";
        else if (fileName.endsWith(".png")) return "image/png";
        else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        else if (fileName.endsWith(".gif")) return "image/gif";
        else return "application/octet-stream";
    }
}
