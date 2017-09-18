package com.huya.server.kits.serverkits.hosts;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/9/12.
 */
public enum ZookeeperDomesticHostEnum {

    DEV_ZK1("172.17.5.134", "172.17.5.134", 2181),
    TEST_ZK1("172.17.5.134", "172.17.5.134", 2181),
    PROD_ZK1("172.31.116.24", "172.31.116.24", 2181),
    PROD_ZK2("172.31.116.23", "172.31.116.23", 2181),
    PROD_ZK3("172.31.116.20", "172.31.116.20", 2181);

    private String publicIp;

    private String privateIp;

    private int port;

    ZookeeperDomesticHostEnum(String publicIp, String privateIp, int port) {
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
