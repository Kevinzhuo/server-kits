package com.huya.server.kits.serverkits.dns;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class DnsUpdateTimerTask extends TimerTask {
    private static Logger logger = LoggerFactory.getLogger(DnsUpdateTimerTask.class);
    private Set<String> hosts;
    public static final int updateInterval = 120000;

    public DnsUpdateTimerTask() {
        this((HashSet) null);
    }

    public DnsUpdateTimerTask(HashSet<String> hosts) {
        this.hosts = new HashSet();
        if (hosts != null) {
            this.hosts = hosts;
        }

        logger.info(String.format("DnsUpdateTimerTask start. update host size:%d", this.hosts.size()));
    }

    public void addHost(String host) {
        this.hosts.add(host);
        logger.info(String.format("DnsUpdateTimerTask add update host:%s", host));
    }

    public void run() {
        if (logger.isDebugEnabled()) {
            logger.debug("DnsUpdateTimerTask begin run.");
        }

        if (this.hosts != null && this.hosts.size() > 0) {
            Iterator var2 = this.hosts.iterator();

            while (var2.hasNext()) {
                String host = (String) var2.next();
                this.updateHost(host);
            }
        }

    }

    private void updateHost(String host) {
        if (!StringUtils.isBlank(host)) {
            InetAddress[] ips = null;

            try {
                ips = InetAddress.getAllByName(host);
                ServerKitsDnsResolver.INSTANCE.add(host, ips);
            } catch (UnknownHostException var4) {
                var4.printStackTrace();
                logger.error(String.format("update host %s dns error:", host), var4);
            }

        }
    }
}
