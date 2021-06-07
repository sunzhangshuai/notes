package com.connect;

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * Queue:
 *
 * @author sunchen
 * @date 2021/6/6 11:44 上午
 */
public class MqQueue {
    public static String getQueue(Channel channel) throws IOException {
        return channel.queueDeclare().getQueue();
    }

    public static void bind(Channel channel) throws IOException {
        channel.queueBind(getQueue(channel), Config.exchangeName, Config.RoutingKey);
    }
}
