import java.util.LinkedList;
import java.util.TimerTask;

public class ClientAliveChecker extends TimerTask {

    LinkedList<String> parentQueue;
    boolean isClientAlive = true;

    public ClientAliveChecker(LinkedList<String> parentQueue) {
        this.parentQueue = parentQueue;
    }

    public void setToTrue() {
        isClientAlive = true;
    }

    public void run() {

        if (!isClientAlive) {
            //tell server thread about it
            parentQueue.addFirst("NO_CONNECTION");
            this.cancel();
        }
        isClientAlive = false;
    }
}
