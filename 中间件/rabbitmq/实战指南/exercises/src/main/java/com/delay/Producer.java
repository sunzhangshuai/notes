package com.delay;

import com.common.MqConnect;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Producer:
 *
 * @author sunchen
 * @date 2021/6/2 9:10 下午
 */
public class Producer {
    static String originExchangeName = "exchange:study:delay:origin";

    static String originQueueNamePre = "queue:study:delay:origin:";

    static String originRoutingKeyPre = "key.study.delay.origin.";

    static String delayExchangeName = "exchange:study:delay";

    static String delayQueueNamePre = "queue:study:delay:";

    static String delayRoutingKeyPre = "key.study.delay.";

    static List<Integer> times = new ArrayList<>();

    static List<Channel> channels = new ArrayList<>();

    static {
        times.add(5);
        times.add(10);
        times.add(20);
        times.add(30);
        times.add(60);
    }

    static String typeDirect = "direct";

    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        // 1. 获取连接
        Connection connection = MqConnect.connection();
        assert connection != null;

        // 2. 获取信道
        Channel channel = connection.createChannel();

        // 3. 声明交换器、声明队列、绑定交换器和队列
        channel.exchangeDeclare(delayExchangeName, typeDirect, true);
        channel.exchangeDeclare(originExchangeName, typeDirect, true);
        for (Integer time : times) {
            // 声明死信队列、绑定交换器和队列
            String delayQueueName = delayQueueNamePre + time + "s";
            channel.queueDeclare(delayQueueName, true, false, false, null);
            String delayRoutingKey = delayRoutingKeyPre + time + "s";
            channel.queueBind(delayQueueName, delayExchangeName, delayRoutingKey);

            // 声明队列
            HashMap<String, Object> param = new HashMap<>(3);
            param.put("x-message-ttl", time * 1000);
            param.put("x-dead-letter-exchange", delayExchangeName);
            param.put("x-dead-letter-routing-key", delayRoutingKey);
            String queueName = originQueueNamePre + time + "s";
            channel.queueDeclare(queueName, true, false, false, param);
            // 绑定交换器和队列
            String routingKey = originRoutingKeyPre + time + "s";
            channel.queueBind(queueName, originExchangeName, routingKey);
        }

        // 4. 发送消息
        int maxNum = 100;
        while (maxNum > 0) {
            int i = new Random().nextInt(5);
            String routingKey = originRoutingKeyPre + times.get(i) + "s";
            String msg = "this is " + times.get(i) + "s msg";
            channel.basicPublish(originExchangeName,
                    routingKey,
                    null,
                    msg.getBytes(StandardCharsets.UTF_8));
            maxNum--;
        }
    }
}
