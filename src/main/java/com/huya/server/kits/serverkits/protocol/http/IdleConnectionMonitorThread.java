package com.huya.server.kits.serverkits.protocol.http;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class IdleConnectionMonitorThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
        logger.info("IdleConnectionMonitorThread start.");
    }

    public void run() {
        while(true) {
            try {
                if (!this.shutdown) {
                    synchronized(this) {
                        this.wait(5000L);
                        this.connMgr.closeExpiredConnections();
                        this.connMgr.closeIdleConnections(60L, TimeUnit.SECONDS);
                        continue;
                    }
                }
            } catch (InterruptedException var3) {
                logger.info("IdleConnectionMonitorThread InterruptedException exits!");
            }

            return;
        }
    }

    public void shutdown() {
        this.shutdown = true;
        synchronized(this) {
            this.notifyAll();
        }
    }

}
