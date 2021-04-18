import java.io.IOException;

import org.apache.zookeeper.*;

public class DataMonitor implements Watcher {
    ZooKeeper zk;
    String znode;
    DataMonitorListener listener;
    Process childProcess;
    String exec[];

    public DataMonitor(ZooKeeper zk, String znode, DataMonitorListener listener, String exec[]) {
        this.zk = zk;
        this.znode = znode;
        this.listener = listener;
        this.exec = new String[exec.length];
        System.arraycopy(exec, 0, this.exec, 0, exec.length);

        try {
            if (zk.exists(znode, false) != null) {
                childProcess = Runtime.getRuntime().exec(exec);
            }
            zk.addWatch(znode, this, AddWatchMode.PERSISTENT_RECURSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface DataMonitorListener {
        void close();
    }

    private void printChildrenCount() {
        try {
            if (zk.exists(znode, false) != null) {
                System.out.println("Number of children of " + znode + ": " + zk.getAllChildrenNumber(znode));
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printZNodeTree(String node, String prefix) throws KeeperException, InterruptedException {
        if (zk.exists(node, false) != null) {
            System.out.println(prefix + node);
            for (String child : zk.getChildren(node, false)) {
                printZNodeTree(node + "/" + child, prefix + "\t");
            }
        } else {
            System.out.println("Node " + znode + " does not exists.");
        }
    }

    public void process(WatchedEvent event) {
        String path = event.getPath();
        Event.EventType eventType = event.getType();
        if (eventType != Event.EventType.None) {
            if(eventType == Event.EventType.NodeCreated && path.equals(znode)){
                try {
                    childProcess = Runtime.getRuntime().exec(exec);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (eventType == Event.EventType.NodeCreated && path.startsWith(znode)){
                printChildrenCount();
            }
            else if(eventType == Event.EventType.NodeDeleted && path.equals(znode)){
                childProcess.destroy();
                listener.close();
            }
        } else if (event.getState() == Event.KeeperState.Expired) {
            childProcess.destroy();
            listener.close();
        }
    }

}