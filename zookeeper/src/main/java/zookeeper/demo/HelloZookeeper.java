package zookeeper.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class HelloZookeeper {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;
    private static final String ZOOKEEPER_ZNODE_NAME = "zookeeper";

    public static void main(String[] args) throws Exception{
        startZK();
    }

    public static void startZK() throws Exception {
        System.out.println("startZK----------------------");
        //确保server确实已经开启了，这里是创建client到server的session
        zk = new ZooKeeper("127.0.0.1:2181", 20000,
                new Watcher() {
                    public void process(WatchedEvent watchedEvent) {
                        System.out.println("process " + watchedEvent);
                        if (watchedEvent.getState() ==
                                Watcher.Event.KeeperState.SyncConnected) {
                            countDownLatch.countDown();
                        }
                    }
                });
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("=================state is " + zk.getState());
        System.out.println("=================zk session begin");
    }

}
