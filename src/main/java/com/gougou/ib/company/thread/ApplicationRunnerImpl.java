package com.gougou.ib.company.thread;

import com.gougou.ib.company.callback.MessageCallBack;
import com.gougou.ib.company.config.entity.TWSConfig;
import com.ib.client.EClientSocket;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * SpringBoot启动成功后,用于创建TWS监听实例
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Resource
    private MessageCallBack wrapper;

    @Resource
    private TWSConfig twsConfig;

    @Override
    public void run(ApplicationArguments args) {
        EClientSocket clientSocket = wrapper.getClientSocket();
        EReaderSignal signal = wrapper.getSignal();
        clientSocket.eConnect(twsConfig.getIp(), twsConfig.getPort(), twsConfig.getClientId());
        EReader eReader = new EReader(clientSocket, signal);
        eReader.start();
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                signal.waitForSignal();
                try {
                    eReader.processMsgs();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
