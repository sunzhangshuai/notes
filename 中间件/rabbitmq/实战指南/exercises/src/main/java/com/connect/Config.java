package com.connect;

/**
 * Config:
 *
 * @author sunchen
 * @date 2021/6/6 11:36 上午
 */
public class Config {

    static String protocol = "amqp";
    static String hostName =  "127.0.0.1";
    static int port =  5672;
    static String userName = "guest";
    static String password = "guest";

    static String RoutingKey = "routingkey:study:and:laogong";

    static String exchangeName = "exchange:study:and:laogong";

    static String queueName = "queue:study:and:laogong";

    static String virtualHost = "/study";
}
