package com.anranruozhu.registry;

import com.anranruozhu.config.RegistryConfig;
import com.anranruozhu.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author anranruozhu
 * @className RegistryTest
 * @description 注册中心测试
 * @create 2024/7/25 下午4:31
 **/
public class RegistryTest {

        final Registry registry = new EtcdRegistry();

        @Before
        public void init() {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress("http://1.94.191.237:2379");
            registry.init(registryConfig);
        }

        @Test
        public void register() throws Exception {
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName("myService1");
            serviceMetaInfo.setServiceVersion("1.0");
            serviceMetaInfo.setServiceHost("1.94.191.237");
            serviceMetaInfo.setServicePort(1234);
            registry.register(serviceMetaInfo);
            serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName("myService");
            serviceMetaInfo.setServiceVersion("1.0");
            serviceMetaInfo.setServiceHost("localhost");
            serviceMetaInfo.setServicePort(1235);
            registry.register(serviceMetaInfo);
            serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName("myService");
            serviceMetaInfo.setServiceVersion("2.0");
            serviceMetaInfo.setServiceHost("localhost");
            serviceMetaInfo.setServicePort(1234);
            registry.register(serviceMetaInfo);
        }

        @Test
        public void unRegister() {
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName("myService");
            serviceMetaInfo.setServiceVersion("1.0");
            serviceMetaInfo.setServiceHost("localhost");
            serviceMetaInfo.setServicePort(1234);
            registry.unRegister(serviceMetaInfo);
        }

        @Test
        public void serviceDiscovery() {

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName("myService");
            serviceMetaInfo.setServiceVersion("1.0");
            String serviceKey = serviceMetaInfo.getServiceKey();
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceKey);
            Assert.assertNotNull(serviceMetaInfoList);
        }
        @Test
        public void heartBeat() throws Exception{
            // init 方法中已经执行心跳检测了
            register();
            //阻塞 1分钟
            Thread.sleep(60*1000L);
        }

}
