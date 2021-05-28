import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;

public class Application {

    static ContextGET contextGET = new ContextGET();
    static ContextPOST contextPOST = new ContextPOST();

    public static void main(String[] args) {
        int serverPort = 8000;
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/api/hello", contextGET);
        server.createContext("/api/info", contextPOST);
        server.setExecutor(null);
        server.start();

        System.out.println("Server is ready.");
    }
}
