package com.gougou.ib.company.service;

import com.gougou.ib.company.dao.model.Account;
import com.gougou.ib.company.thread.ThreadSleep;
import com.gougou.ib.company.util.NumberUtil;
import com.ib.client.EClientSocket;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class SubService {

    @Resource
    private EClientSocket client;

    @Resource
    private ConidService conidService;

    @Resource
    private AccountService accountService;

    @Resource
    private HoldService holdService;

    public void subAccount() {
        holdService.truncate();
        Account account = accountService.getDefault();
        client.reqAccountUpdates(true, account.getName());
    }

    public void sendAccountNameReq() {
        client.reqManagedAccts();
    }

    public Integer getConid(String symbol) {
        Integer conid = conidService.getConid(symbol);
        if (Objects.isNull(conid)) {
            Integer seq = NumberUtil.getSeq(symbol);
            client.reqMatchingSymbols(seq, symbol);
            ThreadSleep.sleep(1000);
            conid = conidService.getConid(symbol);
        }
        return conid;

    }
}
