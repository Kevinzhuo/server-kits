package com.huya.server.kits.serverkits.soa;

import com.huya.server.kits.serverkits.exception.RemoteException;
import com.huya.server.kits.serverkits.hosts.ZookeeperOverseaHostEnum;
import com.huya.server.kits.serverkits.model.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/25.
 */
public class ZkRegistry {

    private final Logger logger = LoggerFactory.getLogger(ZkRegistry.class);

    public static final Integer CONNECTION_TIMEOUT = 30000;

    public ResponseModel registerZookeeper(ZooKeeper zooKeeper, RegistryModel registryModel) throws RemoteException {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setCode(RetCodeEnum.SUCCESS.getCode());
        responseModel.setMessage(RetCodeEnum.SUCCESS.getMsg());

        try {
            String returnPath = createZookeeperNode(zooKeeper, registryModel);
            responseModel.setData(returnPath);
        } catch (UnknownHostException e) {
            this.logger.error("无法获取服务器IP, 异常信息:" + e.getMessage());
            responseModel.setCode(RetCodeEnum.WRONG_SERVER.getCode());
            responseModel.setMessage(RetCodeEnum.WRONG_SERVER.getMsg());
            throw new RemoteException(responseModel);
        } catch (InterruptedException e) {
            this.logger.error("InterruptedException, 异常信息:" + e.getMessage());
            responseModel.setCode(RetCodeEnum.INTERNAL_ERROR.getCode());
            responseModel.setMessage(RetCodeEnum.INTERNAL_ERROR.getMsg());
            throw new RemoteException(responseModel);
        } catch (KeeperException e) {
            this.logger.error("KeeperException, 异常信息:" + e.getMessage());
            responseModel.setCode(RetCodeEnum.INTERNAL_ERROR.getCode());
            responseModel.setMessage(RetCodeEnum.INTERNAL_ERROR.getMsg());
            throw new RemoteException(responseModel);
        }

        return responseModel;
    }

    public void closeZookeeper(ZooKeeper zooKeeper) throws InterruptedException {
        zooKeeper.close();
    }

    private String createZookeeperNode(ZooKeeper zookeeper, RegistryModel registryModel) throws UnknownHostException, KeeperException, InterruptedException {
        String path;
        Stat stat1 = zookeeper.exists(stitchingPath(registryModel.getAppName()), true);
        if (stat1 == null)
            zookeeper.create(stitchingPath(registryModel.getAppName()), registryModel.getAppName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Stat stat2 = zookeeper.exists(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()), true);
        if (stat2 == null)
            zookeeper.create(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()), registryModel.getClusterName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Stat stat3 = zookeeper.exists(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                , registryModel.getRole()), true);
        if (stat3 == null)
            zookeeper.create(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                    , registryModel.getRole()), registryModel.getRole().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Stat stat4 = zookeeper.exists(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                , registryModel.getRole(), registryModel.getServiceName()), true);
        if (stat4 == null)
            zookeeper.create(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                    , registryModel.getRole(), registryModel.getServiceName()),
                    registryModel.getServiceName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        Stat stat = zookeeper.exists(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                , registryModel.getRole(), registryModel.getServiceName()), true);
        zookeeper.setData(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                , registryModel.getRole(), registryModel.getServiceName()),
                registryModel.getApi() == null ? "".getBytes() : ArrayUtils.toString(registryModel.getApi()).getBytes(), stat.getVersion());

        Stat stat5 = zookeeper.exists(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                , registryModel.getRole(), registryModel.getServiceName()
                , registryModel.getIp()) + ":" + registryModel.getPort(), true);
        if (stat5 == null) {
            path = zookeeper.create(stitchingPath(registryModel.getAppName(), registryModel.getClusterName()
                    , registryModel.getRole(), registryModel.getServiceName(), registryModel.getIp()) + ":" + registryModel.getPort(),
                    (registryModel.getIp() + ":" + registryModel.getPort()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } else {
            path = stitchingPath(registryModel.getAppName(), registryModel.getClusterName(), registryModel.getRole(), registryModel.getServiceName(),
                    registryModel.getIp()) + ":" + registryModel.getPort();
        }
        return path;
    }

    public String initEnv(RegistryModel registryModel) {
        if (registryModel == null)
            return null;

        String connectString = null;

        if (AreaEnum.DOMESTIC.getDescri().equals(registryModel.getArea())) {
            if (EnvironmentEnum.DEV.getEnv().equals(registryModel.getEnvironment())) {
                connectString = ZookeeperOverseaHostEnum.DEV_ZK1.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.DEV_ZK1.getPort();
            }

            if (EnvironmentEnum.TEST.getEnv().equals(registryModel.getEnvironment())) {
                connectString = ZookeeperOverseaHostEnum.TEST_ZK1.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.TEST_ZK1.getPort();
            }

            if (EnvironmentEnum.PROD.getEnv().equals(registryModel.getEnvironment())) {
                connectString = ZookeeperOverseaHostEnum.PROD_ZK1.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.PROD_ZK1.getPort() + ","
                        + ZookeeperOverseaHostEnum.PROD_ZK2.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.PROD_ZK2.getPort() + ","
                        + ZookeeperOverseaHostEnum.PROD_ZK3.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.PROD_ZK3.getPort();
            }
        }

        if (AreaEnum.OVERSEA.getDescri().equals(registryModel.getArea())) {
            if (EnvironmentEnum.DEV.getEnv().equals(registryModel.getEnvironment())) {
                connectString = ZookeeperOverseaHostEnum.DEV_ZK1.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.DEV_ZK1.getPort();
            }

            if (EnvironmentEnum.TEST.getEnv().equals(registryModel.getEnvironment())) {
                connectString = ZookeeperOverseaHostEnum.TEST_ZK1.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.TEST_ZK1.getPort();
            }

            if (EnvironmentEnum.PROD.getEnv().equals(registryModel.getEnvironment())) {
                connectString = ZookeeperOverseaHostEnum.PROD_ZK1.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.PROD_ZK1.getPort() + ","
                        + ZookeeperOverseaHostEnum.PROD_ZK2.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.PROD_ZK2.getPort() + ","
                        + ZookeeperOverseaHostEnum.PROD_ZK3.getPrivateIp() + ":" + ZookeeperOverseaHostEnum.PROD_ZK3.getPort();
            }
        }

        return connectString;
    }

    private String stitchingPath(String... args) {
        StringBuilder str = new StringBuilder();

        if (args == null || args.length == 0)
            return str.toString();

        for (String arg : args) {
            str.append("/").append(arg);
        }

        return str.toString();
    }

}
