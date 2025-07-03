package org.example;

import java.io.*;
import java.net.*;

public class HttpClient {

    public static void main(String[] args) {
        String server = "localhost";
        int port = 8081;

        try (Socket socket = new Socket(server, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            // Send HTTP GET request
            out.write("GET / HTTP/1.1\r\n");
            out.write("Host: " + server + "\r\n");
            out.write("\r\n");
            out.flush();

            // Read and print response
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
