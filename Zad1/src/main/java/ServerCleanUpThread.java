import java.util.HashMap;
import java.util.LinkedList;

public class ServerCleanUpThread extends Thread{

    HashMap<String, LinkedList<String>> usersQueues;
    HashMap<String, Thread> usersThreads;

    public ServerCleanUpThread(HashMap<String, LinkedList<String>> usersQueues, HashMap<String, Thread> usersThreads) {
        this.usersQueues = usersQueues;
        this.usersThreads = usersThreads;
    }

    public void run() {
        for (String name: usersThreads.keySet()) {
            if (usersThreads.get(name).isAlive()) {
                usersQueues.get(name).addFirst("DISCONNECT");
            }
        }
    }
}
