package com.delay;

import com.common.CommonConsumer;
import com.common.MqConnect;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * Producer:
 *
 * @author sunchen
 * @date 2021/6/2 9:10 下午
 */
public class Consumer {
    static String consumerTag = "consumer:study:delay:normal";


    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        // 连接
        Connection connection = MqConnect.connection();
        assert connection != null;

        // 信道
        Channel channel = connection.createChannel();

        // 推模式开始消费
        CommonConsumer consumer = new CommonConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        for (Integer time : Producer.times) {
            // 声明死信队列、绑定交换器和队列
            String queueName = Producer.delayQueueNamePre + time + "s";
            String newConsumerTag = consumerTag + time + "s";
            channel.basicConsume(queueName, false, newConsumerTag, consumer);
        }
    }
}
