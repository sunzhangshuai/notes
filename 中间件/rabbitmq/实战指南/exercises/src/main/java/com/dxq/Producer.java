package com.dxq;

import com.common.MqConnect;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Producer:
 *
 * @author sunchen
 * @date 2021/6/2 9:10 下午
 */
public class Producer {
    static String originExchangeName = "exchange:study:dx:origin";

    static String originQueueName = "queue:study:dx:origin";

    static String originRoutingKey = "key.study.dx.origin";

    static String dxExchangeName = "exchange:study:dx";

    static String dxQueueName = "queue:study:dx";

    static String dxRoutingKey = "key.study.dx";

    static String typeDirect = "direct";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 1. 获取连接
        Connection connection = MqConnect.connection();
        assert connection != null;

        // 2. 获取信道
        Channel channel = connection.createChannel();

        // 3. 声明死信交换器、声明死信队列、绑定死信交换器和死信队列
        channel.exchangeDeclare(dxExchangeName, typeDirect, true);
        channel.queueDeclare(dxQueueName, true, false, false, null);
        channel.queueBind(dxQueueName, dxExchangeName, dxRoutingKey);

        // 4. 声明源交换器、声明源队列、绑定源交换器和源队列
        channel.exchangeDeclare(originExchangeName, typeDirect, true);
        Map<String, Object> param = new HashMap<>(2);
        param.put("x-dead-letter-exchange", dxExchangeName);
        param.put("x-dead-letter-routing-key", dxRoutingKey);
        channel.queueDeclare(originQueueName, true, false, false, param);
        channel.queueBind(originQueueName, originExchangeName, originRoutingKey);

        // 5. 发送消息
        int maxNum = 100;
        while (maxNum > 0) {
            Thread.sleep(5000);
            channel.basicPublish(originExchangeName,
                    originRoutingKey,
                    new AMQP.BasicProperties()
                            .builder()
                            .contentType("application/json")
                            .messageId("12222")
                            .build(),
                    "123".getBytes(StandardCharsets.UTF_8));
            maxNum--;
        }
    }
}
