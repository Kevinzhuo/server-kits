package com.huya.server.kits.serverkits.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class RegistryModel implements Serializable {

    private static final long serialVersionUID = 3642337500776174518L;

    private String ip;

    private String port;

    private String appName;

    private String ClusterName;

    private String ServiceName;

    private String role;

    private List<String> api;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getClusterName() {
        return ClusterName;
    }

    public void setClusterName(String clusterName) {
        ClusterName = clusterName;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getApi() {
        return api;
    }

    public void setApi(List<String> api) {
        this.api = api;
    }
}
