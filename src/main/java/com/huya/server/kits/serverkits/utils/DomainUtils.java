package com.huya.server.kits.serverkits.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/9/4.
 */
public class DomainUtils {

    private static final Logger logger = LoggerFactory.getLogger(DomainUtils.class);

    /**
     * 获取服务器ip
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getServerIp() throws UnknownHostException {
        String ip;
        InetAddress addr = InetAddress.getLocalHost();//获得本机IP
        if (addr == null) {
            return null;
        } else {
            ip = addr.getHostAddress();
        }

        logger.info("服务器IP为：" + ip);

        return ip;
    }

}
