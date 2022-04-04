package com.gougou.ib.company.config;

import com.gougou.ib.company.callback.MessageCallBack;
import com.gougou.ib.company.config.entity.TigerConfig;
import com.ib.client.EClientSocket;
import com.tictactec.ta.lib.Core;
import com.tigerbrokers.stock.openapi.client.https.client.TigerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configuration
public class CommonConfig {

    @Resource
    private MessageCallBack messageCallBack;

    @Resource
    private TigerConfig tigerConfig;

    @Bean
    public TigerHttpClient tigerHttpClient() {
        return new TigerHttpClient(tigerConfig.getServerUrl(), tigerConfig.getTigerId(),
                tigerConfig.getPrivateKey());
    }

    @Bean
    public EClientSocket eClientSocket() {
        EClientSocket clientSocket = messageCallBack.getClientSocket();
        return clientSocket;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
