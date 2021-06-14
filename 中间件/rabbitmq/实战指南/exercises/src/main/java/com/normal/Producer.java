package com.normal;

import com.common.MqConnect;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
    static String exchangeName = "exchange:study:normal";

    static String queueName = "queue:study:normal";

    static String routingKey = "key.study.normal";

    static String typeDirect = "direct";

    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        // 1. 获取连接
        Connection connection = MqConnect.connection();
        assert connection != null;

        // 2. 获取信道
        Channel channel = connection.createChannel();

        // 3. 声明交换器、声明队列、绑定交换器和队列
        channel.exchangeDeclare(exchangeName, typeDirect, true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(exchangeName, queueName, routingKey);

        channel.basicPublish(exchangeName,
                routingKey,
                new AMQP.BasicProperties()
                        .builder()
                        .contentType("application/json")
                        .expiration("6000")
                        .build(),
                "222".getBytes(StandardCharsets.UTF_8));

        channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body)
                -> System.out.println("Basic.return 返回的消息是:" + new String(body)));
    }
}
