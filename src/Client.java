import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

public class Client {
    // for networking
    Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    // The public key, secret key and a value used for both sides, for sending messages to me
    private BigInteger mySk;
    private BigInteger myPk;
    private BigInteger myN;

    // The values I need to send an encrypted message
    private BigInteger hisPk;
    private BigInteger hisN;

    private Client() {
            // Some nice default values
            this(300, "127.0.0.1", 5000);
    }

    private Client(int str, String ip, int port) {
        this(BigInteger.valueOf(10).pow(str), BigInteger.valueOf(10).pow(str+1), ip, port);
    }

    Client(int str) {
        this(BigInteger.valueOf(10).pow(str), BigInteger.valueOf(10).pow(str+1));
    }

    private Client(BigInteger lower_bound, BigInteger upper_bound) {
        System.out.println("[System]: Generating keys...");
        BigInteger q = AdvMath.findPrime(lower_bound, upper_bound);
        BigInteger p = AdvMath.findPrime(lower_bound, upper_bound);
        myN = p.multiply(q);
        BigInteger phi = (q.subtract(BigInteger.ONE)).multiply(p.subtract(BigInteger.ONE));
        myPk = AdvMath.findCoprime(phi);
        mySk = AdvMath.extendedGcd(phi, myPk).add(phi);
    }

    private Client(BigInteger lower_bound, BigInteger upper_bound, String ip, int port) {
        this(lower_bound, upper_bound);
        try {
            socket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connect();
    }

    void connect() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Send the other guy the keys
        try {
            out.writeUTF(myN.toString());
            out.writeUTF(myPk.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get the keys from the other guy
        try {
            hisN =  new BigInteger(in.readUTF());
            hisPk = new BigInteger(in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BigInteger encrypt(BigInteger msg) {
        return AdvMath.modExpo(msg, hisPk, hisN);
    }

    private BigInteger decrypt(BigInteger msg) {
        return AdvMath.modExpo(msg, mySk, myN);
    }

    void sendMsg(String msg) {
        try {
            for (int i = 0; i < msg.length(); i++) {
                out.writeUTF(this.encrypt(BigInteger.valueOf((int)msg.charAt(i))).toString());
            }
            out.writeUTF(this.encrypt(BigInteger.valueOf((int)'\n')).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read()  {
        while (true) {
            try {
                String line = in.readUTF();
                System.out.print((char)this.decrypt(new BigInteger(line)).intValue());
            }
            catch(IOException e)  {
                System.out.println(e.getStackTrace());
            }
        }
    }

    public static void main(String args[]) {
        Client client = new Client();
        System.out.println("[System]: This endpoint is ready!");
        (new Thread(new Reader(client))).start();
        (new Thread(new Writer(client))).start();
    }

}
