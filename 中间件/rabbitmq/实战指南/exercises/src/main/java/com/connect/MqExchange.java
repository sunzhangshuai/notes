package com.connect;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * Exchange:
 *
 * @author sunchen
 * @date 2021/6/6 11:44 上午
 */
public class MqExchange {
    public static void declare(Channel channel, String type) throws IOException {
        AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare(Config.exchangeName, type, true);
    }
}
