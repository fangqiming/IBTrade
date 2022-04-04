package com.gougou.ib.company.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "trade.tws")
public class TWSConfig {
    private String ip;
    private Integer port;
    private Integer clientId;
}
