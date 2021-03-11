import java.util.TimerTask;

public class ClientAliveSetter extends TimerTask {

    boolean isPingSent = false;

    public boolean IsPingSent() {
        return isPingSent;
    }

    public void setPingSent() {
        isPingSent = true;
    }

    public void run() {
        isPingSent = false;
    }
}
