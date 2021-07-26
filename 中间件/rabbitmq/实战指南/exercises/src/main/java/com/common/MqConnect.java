package com.common;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Connect:
 *
 * @author sunchen
 * @date 2021/6/6 11:41 上午
 */
public class MqConnect {

    /**
     * 协议
     */
    static String protocol = "amqp";

    /**
     * 域名
     */
    static String hostName = "localhost";

    /**
     * 端口
     */
    static int port = 5672;

    /**
     * 用户名
     */
    static String username = "guest";

    /**
     * 密码
     */
    static String password = "guest";

    /**
     * 虚拟主机
     */
    static String virtualHost = "/study";

    /**
     * 常用connection
     *
     * @return 连接
     * @throws IOException      ...
     * @throws TimeoutException ...
     */
    public static Connection connection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(hostName);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory.newConnection();
    }

    /**
     * 通过uri的方式连接
     *
     * @return 连接
     * @throws IOException              ...
     * @throws TimeoutException         ...
     * @throws NoSuchAlgorithmException ...
     * @throws KeyManagementException   ...
     * @throws URISyntaxException       ...
     */
    public static Connection connectionByUri() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        String uri = protocol + "://" + username + ":" + password + "@" + hostName + ":" + port + virtualHost;
        System.out.println(uri);
        connectionFactory.setUri(uri);
        return connectionFactory.newConnection();
    }


}
