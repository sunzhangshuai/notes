package com.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.sql.DataSourceDefinition;

/**
 * RabbitmqConfig:
 *
 * @author sunchen
 * @date 2021/6/2 9:01 ����
 */
public class RabbitmqConfig {
    String host;
    String port;
    String userName;
    String password;
}
