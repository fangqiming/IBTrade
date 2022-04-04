package com.gougou.ib.company.config.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "trade.tiger")
public class TigerConfig {

    private String serverUrl;
    private String wssUrl;
    private String account;
    private String tigerId;
    private String privateKey;
    private Boolean isTigerAccount;
}
