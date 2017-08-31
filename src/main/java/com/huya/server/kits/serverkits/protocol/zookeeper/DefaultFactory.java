package com.huya.server.kits.serverkits.protocol.zookeeper;

import com.huya.server.kits.serverkits.base.AbstractFactory;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/25.
 */
public class DefaultFactory extends AbstractFactory {

    @Override
    public ZooKeeper zookeeperInstance(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd, boolean canBeReadOnly) throws IOException {
        ZooKeeper zk = new ZooKeeper(connectString,
                sessionTimeout, watcher);
        return zk;
    }

}
