package com.producer;

import com.connect.MqChannel;
import com.connect.MqConnect;
import com.connect.MqExchange;
import com.connect.MqQueue;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

/**
 * Producer:
 *
 * @author sunchen
 * @date 2021/6/2 9:10 下午
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        Connection connection = MqConnect.getConnection(1);
        assert connection != null;
        Channel channel = MqChannel.getChannel(connection);
        MqExchange.declare(channel, "direct");
        MqQueue.bind(channel);
    }

    public static Connection connection1() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        System.out.println(connection);
        return connection;
    }

    public static Connection connection2() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://guest:guest@127.0.0.1:5672");
        Connection connection = connectionFactory.newConnection();
        System.out.println(connection);
        return connection;
    }

    public static Channel getChannel( Connection connection) throws IOException {
        return connection.createChannel();
    }
}
