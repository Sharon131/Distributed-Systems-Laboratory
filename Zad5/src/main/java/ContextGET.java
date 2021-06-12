import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ContextGET implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                System.out.println("Get message");
                Headers headers = exchange.getResponseHeaders();
                String respText = Files.readString(Path.of("/opt/app/src/main/webapp/index.html"));
//                String respText = Files.readString(Path.of("C:\Users\pastu\Documents\Rozproszone\Laby\Zad5\src\main\webapp\index.html"));

                headers.add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, respText.length());
                OutputStream output = exchange.getResponseBody();
                output.write(respText.getBytes());
                output.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
        }
        exchange.close();
    }
}
