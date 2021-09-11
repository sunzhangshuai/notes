package com.federation.exchange;

import com.common.MqConnect;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * Producer:
 *
 * @author sunchen
 * @date 2021/9/11 11:32 下午
 */
public class Producer {
    /**
     * 交换器名称
     */
    static String exchangeName = "exchange:study:federationExchange";

    /**
     * 上游交换器
     */
    static String upstreamExchangeName = "exchange:study:upstream:federationExchange";

    /**
     * 上游队列
     */
    static String upstreamQueueName = "queue:study:upstream:federationExchange";

    /**
     * 队列名称
     */
    static String queueName = "queue:study:federationExchange";

    /**
     * 绑定键
     */
    static String routeKey = "key:study:federationExchange";

    /**
     * 消息个数
     */
    static int msgNum = 400;

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 创建连接
        Connection connection = MqConnect.connection();
        // 2. 获取信道
        Channel channel = connection.createChannel();
        // 3. 定义交换器
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT.getType());
        // 4. 定义队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 5. 绑定队列
        channel.queueBind(queueName, exchangeName, routeKey);
        // 6. 发消息
        for (int i = 0; i < msgNum; i++) {
            byte[] msg = ("federationExchange:" + i).getBytes(StandardCharsets.UTF_8);
            byte[] upstreamMsg = ("upstream-federationExchange:" + i).getBytes(StandardCharsets.UTF_8);
            //channel.basicPublish(exchangeName, routeKey, null, msg);
            channel.basicPublish(upstreamExchangeName, routeKey, null, upstreamMsg);
        }
        // 7. 关闭
        channel.close();
        connection.close();
    }
}