package com.connect;

import com.rabbitmq.client.Channel;
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

    public static Connection getConnection(int i) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        switch (i) {
            case 1:
                return connection1();
            case 2:
                return connection2();
            default:
        }
        return null;
    }

    public static Connection connection1() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Config.hostName);
        connectionFactory.setPort(Config.port);
        connectionFactory.setVirtualHost(Config.virtualHost);
        connectionFactory.setUsername(Config.userName);
        connectionFactory.setPassword(Config.password);
        Connection connection = connectionFactory.newConnection();
        System.out.println(connection);
        return connection;
    }

    public static Connection connection2() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        String uri = Config.protocol + "://" + Config.userName + ":" + Config.password + "@" + Config.hostName + ":" + Config.port + Config.virtualHost;
        System.out.println(uri);
        connectionFactory.setUri(uri);
        Connection connection = connectionFactory.newConnection();
        System.out.println(connection);
        return connection;
    }


}
