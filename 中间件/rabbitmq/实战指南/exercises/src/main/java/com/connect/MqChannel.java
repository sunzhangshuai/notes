package com.connect;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * Channel:
 *
 * @author sunchen
 * @date 2021/6/6 11:50 上午
 */
public class MqChannel {
    public static Channel getChannel(Connection connection) throws IOException {
        return connection.createChannel();
    }
}
