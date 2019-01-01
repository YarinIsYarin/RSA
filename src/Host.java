import java.net.*;

public class Host extends Client {
    private ServerSocket server = null;

    Host(int str, int port) {
        super(str);
        try {
            server = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println("[System]: Error, illegal port number " + port);
            System.exit(0);
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
}
