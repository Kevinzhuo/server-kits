package com.huya.server.kits.serverkits.hosts;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/9/6.
 */
public enum ZookeeperOverseaHostEnum {
    DEV_ZK1("172.17.5.134", "172.17.5.134", 2181),
    TEST_ZK1("172.17.5.134", "172.17.5.134", 2181),
    PROD_ZK1("52.201.64.140", "172.31.17.87", 2181),
    PROD_ZK2("52.7.0.103", "172.31.23.24", 2181),
    PROD_ZK3("52.201.100.187", "172.31.24.45", 2181);

    private String publicIp;

    private String privateIp;

    private int port;

    ZookeeperOverseaHostEnum(String publicIp, String privateIp, int port) {
        this.publicIp = publicIp;
        this.privateIp = privateIp;
        this.port = port;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
