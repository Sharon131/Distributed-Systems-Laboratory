import java.io.IOException;
import java.util.Scanner;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Main
        implements Watcher, Runnable, DataMonitor.DataMonitorListener
{
    static String znode = "/z";
    DataMonitor dataMonitor;
    boolean shouldClose = false;

    public Main(String hostPort, String exec[]) throws KeeperException, IOException {
        ZooKeeper zk = new ZooKeeper(hostPort, 3000, this);
        dataMonitor = new DataMonitor(zk, znode, this, exec);

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Should be: Executor hostPort program [args ...]");
            System.exit(2);
        }
        String hostPort = args[0];
        String exec[] = new String[args.length - 1];
        System.arraycopy(args, 1, exec, 0, exec.length);
        try {
            new Main(hostPort, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    public void run() {
        try {
            synchronized (this) {
                Scanner reader = new Scanner(System.in);
                while (!shouldClose) {
                    String line = reader.nextLine();
                    if (line.equals("tree")) {
                        dataMonitor.printZNodeTree(znode, "");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void startExec() {
//        try {
//            childProcess = Runtime.getRuntime().exec(exec);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void close() {
        synchronized (this) {
//            childProcess.destroy();
            shouldClose = true;
        }
    }
}