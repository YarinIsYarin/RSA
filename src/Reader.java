public class Reader implements Runnable{
    private Client endpoint;
    public Reader(Client endpoint) {
        this.endpoint = endpoint;
    }

    public void run(){
        endpoint.read();
    }

}
