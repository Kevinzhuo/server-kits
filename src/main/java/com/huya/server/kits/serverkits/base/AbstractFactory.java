package com.huya.server.kits.serverkits.base;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/25.
 */
public abstract class AbstractFactory {

    public abstract ZooKeeper zookeeperInstance(String connectString, int sessionTimeout, Watcher watcher, long sessionId, byte[] sessionPasswd, boolean canBeReadOnly) throws IOException;

}
