package com.gougou.ib.company.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;


@Data
@Configuration
@ConfigurationProperties(prefix = "trade.rule")
public class TradeConfig {
    private Double riseHigh;
    private Double lever;
    private Integer totalStock;
    private String ibUser;
    private String pwd;
    private String loginUrl;
    private String holdUrl;
    private String planUrl;

}
