package com.queuepartition;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Base:
 *
 * @author sunchen
 * @date 2021/9/11 9:32 下午
 */
public class Base {
    static String exchangeName = "exchange:study:queuePartition";
    static String queueNamePre = "queue:study:queuePartition:";
    static String routeKeyPre = "key:study:queuePartition:";
    static int partNum = 4;

    public static void queueDeclare(Channel channel) throws IOException {
        for (int i = 0; i < partNum; i++) {
            channel.queueDeclare(queueNamePre + i, true, false, false, null);
        }
    }

    public static void queueBind(Channel channel) throws IOException {
        for (int i = 0; i < partNum; i++) {
            channel.queueBind(queueNamePre + i, exchangeName, routeKeyPre + i);
        }
    }

    public static void basicPublish(Channel channel, String msg) throws IOException {
        int part = new Random().nextInt(partNum);
        channel.basicPublish(exchangeName, routeKeyPre + part, new BasicProperties(), msg.getBytes(StandardCharsets.UTF_8));
    }
}