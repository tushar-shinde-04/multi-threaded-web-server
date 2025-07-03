Java Multi-threaded HTTP Web Server (Virtual Thread Powered)

A high-performance, lightweight HTTP web server built from scratch in java designed to handle thousands of concurrent connections using Java 21 Virtual Threads (Project Loom).

> Scales effortlessly from 10 to 10,000+ clients with minimal resource usage  
> Demonstrates modern concurrency, networking, and thread-safe server design

## Project Structure
```plaintext
multi-threaded-web-server/
├── public/
│   ├── index.html      # Default homepage
│   └── 404.html        # Custom 404 error page
└── src/
    ├── SimpleHttpServer.java   # Multi-threaded HTTP server (virtual threads)
    └── SimpleHttpClient.java   # Test client for HTTP GET requests

## Features

- Handles HTTP GET requests
- Serves static files such as .html, .css, .js, and images
- Custom 404 error page handling
- Uses Java 21 virtual threads for lightweight concurrency
- Logs request details with thread information
- Simple client provided to simulate real-world request load

## Getting Started

### Prerequisites

- Java 21 must be installed and configured in your PATH

### Run the Server

1. Navigate to the project directory
2. Compile the server
3. Run the server  #javac src/SimpleHttpServer.java
4. Open your browser and go to http://localhost:8081
5. ### Run the Test Client (Optional)
      javac src/SimpleHttpClient.java
      java src.SimpleHttpClient


## How It Works

- The server starts on port 8081 and waits for incoming socket connections.
- For every incoming request, a virtual thread is spawned to handle it.
- It parses the HTTP GET request and tries to serve the requested file from the public directory.
- If the file is found, it is returned with a 200 OK response.
- If not found, a custom 404 page is served (or a default error message).
- MIME type is detected dynamically based on file extension.

## Architecture

- Java's ServerSocket is used to accept client connections.
- Java 21's Executors.newVirtualThreadPerTaskExecutor() creates a lightweight thread per request.
- Static resources are served using basic file I/O.
- The server handles concurrency using Java’s built-in virtual thread scheduling.

## Deployment

- Suitable for internal demos or learning environments
- Can be deployed as a standalone .jar file using basic javac and jar commands
- Place your static files in the public folder before running the server
- For benchmarking, use Apache JMeter or the included SimpleHttpClient class to simulate load


