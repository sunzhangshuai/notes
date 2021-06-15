package com.rpc;

import com.alibaba.fastjson.JSONObject;
import com.common.MqConnect;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * RpcClient:
 *
 * @author sunchen
 * @date 2021/6/15 11:42 下午
 */
public class RpcClient {
    static String callQueueName = "queue:study:rpc:call";

    static String exchangeName = "exchange:study:rpc";

    static String queueName = "queue:study:rpc";

    static String routingKey = "key.study.rpc";

    static String typeDirect = "direct";

    static String callRoutingKey = "key.study.rpc.call";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1. 获取连接和信道
        Connection connection = MqConnect.connection();
        Channel channel = connection.createChannel();
        // 2. 声明交换器、声明队列、绑定交换器和队列
        channel.exchangeDeclare(exchangeName, typeDirect, true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        // 3. 声明回调队列
        channel.queueDeclare(callQueueName, true, false, false, null);
        channel.queueBind(callQueueName, exchangeName, callRoutingKey);

        // 开始测试
        for (int i = 0; i < 100; i++) {
            call(channel);
        }
    }

    public static void call(Channel channel) throws IOException {
        int a = new Random().nextInt(100);
        int b = new Random().nextInt(100);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", a);
        jsonObject.put("b", b);
        // 1. 发消息
        String correlationId = UUID.randomUUID().toString();
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
                .builder()
                .replyTo(callRoutingKey)
                .correlationId(correlationId)
                .build();
        channel.basicPublish(exchangeName, routingKey, basicProperties, jsonObject.toString().getBytes());
        // 2. 收消息
        while (true) {
            GetResponse getResponse = channel.basicGet(callQueueName, true);
            if (getResponse != null && correlationId.equals(getResponse.getProps().getCorrelationId())) {
                byte[] body = getResponse.getBody();
                System.out.println(a + "+" + b + "=" + new String(body));
                break;
            }
        }
    }
}
