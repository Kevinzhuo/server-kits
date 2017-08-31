package com.huya.server.kits.serverkits.dns;

import org.apache.http.conn.DnsResolver;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class ServerKitsDnsResolver implements DnsResolver {

    public static final ServerKitsDnsResolver INSTANCE = new ServerKitsDnsResolver();
    private static Logger logger = LoggerFactory.getLogger(ServerKitsDnsResolver.class);
    private final Map<String, InetAddress[]> dnsMap = new ConcurrentHashMap();

    public ServerKitsDnsResolver() {
    }

    public void add(String host, InetAddress... ips) {
        Args.notNull(host, "Host name");
        Args.notNull(ips, "Array of IP addresses");
        this.dnsMap.put(host, ips);
    }

    public InetAddress[] resolve(String host) throws UnknownHostException {
        InetAddress[] resolvedAddresses = (InetAddress[]) this.dnsMap.get(host);
        if (logger.isDebugEnabled()) {
            logger.debug("dns cache Resolving " + host + " to " + Arrays.deepToString(resolvedAddresses));
        }

        return resolvedAddresses == null ? InetAddress.getAllByName(host) : resolvedAddresses;
    }

}
