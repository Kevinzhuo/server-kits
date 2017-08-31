package com.huya.server.kits.serverkits.soa;

import com.huya.server.kits.serverkits.exception.RemoteException;
import com.huya.server.kits.serverkits.model.RegistryModel;
import com.huya.server.kits.serverkits.model.ResponseModel;
import com.huya.server.kits.serverkits.model.RetCodeEnum;
import com.huya.server.kits.serverkits.protocol.zookeeper.DefaultFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Kevin[wangzhuo@yy.com] on 2017/8/25.
 */
public class ZkRegistry {

    private final Logger logger = LoggerFactory.getLogger(ZkRegistry.class);

    public static final Integer CONNECTION_TIMEOUT = 2000;

    public ResponseModel registerZookeeper(RegistryModel registryModel) throws RemoteException, InterruptedException, KeeperException {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setCode(RetCodeEnum.SUCCESS.getCode());
        responseModel.setMessage(RetCodeEnum.SUCCESS.getMsg());

        DefaultFactory zkFactory = new DefaultFactory();
        ZooKeeper zk;
        try {
            zk = zkFactory.zookeeperInstance(registryModel.getIp() + ":" + registryModel.getPort(), CONNECTION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    logger.info("已经触发了" + event.getType() + "事件！");
                }
            }, 0, null, false);
            String returnPath = zk.create(StitchingPath(registryModel.getAppName(), registryModel.getClusterName(),
                    registryModel.getRole(), registryModel.getServiceName(), registryModel.getIp(), registryModel.getPort()),
                    StringUtils.join(registryModel.getApi(), ",").getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            responseModel.setData(returnPath);
            zk.close();
        } catch (IOException e) {
            logger.error("实例化zookeeper出现IOException");
            responseModel.setCode(RetCodeEnum.INTERNAL_ERROR.getCode());
            responseModel.setMessage(RetCodeEnum.INTERNAL_ERROR.getMsg());
            throw new RemoteException(responseModel);
        } catch (InterruptedException e) {
            logger.error("InterruptedException");
            responseModel.setCode(RetCodeEnum.INTERNAL_ERROR.getCode());
            responseModel.setMessage(RetCodeEnum.INTERNAL_ERROR.getMsg());
            throw new RemoteException(responseModel);
        } catch (KeeperException e) {
            logger.error("KeeperException");
            responseModel.setCode(RetCodeEnum.INTERNAL_ERROR.getCode());
            responseModel.setMessage(RetCodeEnum.INTERNAL_ERROR.getMsg());
            throw new RemoteException(responseModel);
        }

        return responseModel;
    }

    private String StitchingPath(String... args) {
        StringBuilder str = new StringBuilder("/");

        if (args == null || args.length == 0)
            return str.toString();

        for (String arg : args) {
            str.append(arg);
        }

        str.append("/");

        return str.toString();
    }

}
