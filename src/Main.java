import java.util.Scanner;

public class Main {
    public static void main (String[] args) {
        String hostCommand = "c"; String joinCommand = "j";
        System.out.println("[System]: Do you wanna create a new chat or join an existing ?" +
                " [enter " + hostCommand + " for create \\ " + joinCommand + " for join]");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        while (!(input.equals("c") || input.equals("j"))) {
            System.out.println("[System]: Illegal command try " + hostCommand + " \\ " + joinCommand);
            input = sc.nextLine();
        }
        // The keys will be 10^defaultStrength
        int strength = 5;
        Client endpoint = null;
        if (input.equals(hostCommand)) {
            System.out.println("[System]: Which port do you want to use?");
            int port = sc.nextInt();
            endpoint = new Host(strength, port);
        } else if (input.equals(joinCommand)) {
            System.out.println("[System]: Which port do you want to use?");
            int port = sc.nextInt();
            System.out.println("What is the ip address of the host?");
            String address = sc.next();
            endpoint = new Client(strength, address, port);
        }
        System.out.println("[System]: Chat is ready!");
        // Run the chat
        (new Thread(new Reader(endpoint))).start();
        (new Thread(new Writer(endpoint))).start();

    }
}