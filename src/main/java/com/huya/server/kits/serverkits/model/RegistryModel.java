package com.huya.server.kits.serverkits.model;

import com.huya.server.kits.serverkits.utils.DomainUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/28.
 */
public class RegistryModel implements Serializable {

    private static final long serialVersionUID = 3642337500776174518L;

    public static final String SOA = "soa";

    private String ip;

    private String port;

    private String appName;

    private String ClusterName;

    private String ServiceName;

    private String loadBalancing;

    private String role;

    private String environment;

    private String area;

    private List<String> api;

    public String getIp() throws UnknownHostException {
        String realIp;
        if (StringUtils.isNoneBlank(ip)) {
            realIp = ip;
        } else {
            realIp = DomainUtils.getServerIp();
        }
        return realIp;
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

    public String getLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(String loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getApi() {
        return api;
    }

    public void setApi(List<String> api) {
        this.api = api;
    }
}
