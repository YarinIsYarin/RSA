import java.util.Scanner;

public class Writer implements Runnable{
    private Client endpoint;
    public Writer(Client endpoint) {
        this.endpoint = endpoint;
    }

    public void run(){
        Scanner sc = new Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();
            endpoint.sendMsg(msg);
        }
    }

}
