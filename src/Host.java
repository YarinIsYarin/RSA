import java.net.*;
import java.io.*;

public class Host extends Client {
    private ServerSocket server = null;

    Host(int str, int port) {
        super(str);
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connect();
    }

    @Override
    void connect() {
        try {
            System.out.println("[System]: Waiting for a client ...");
            socket = server.accept();
            System.out.println("[System]: Found one");
            super.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Host server = new Host(300, 5000);
        System.out.println("[system]: server is ready");
        (new Thread(new Reader(server))).start();
        (new Thread(new Writer(server))).start();
    }
}
