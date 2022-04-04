package com.gougou.ib.company.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gougou.ib.company.dao.mapper.AccountMapper;
import com.gougou.ib.company.dao.model.Account;
import com.ib.client.EClient;
import com.ib.client.EClientSocket;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    public void update(String currency, Double value, String accountName) {
        Account account = getByName(accountName);
        if (Objects.isNull(account)) {
            accountMapper.insert(Account.builder().currency(currency)
                    .name(accountName).netAsset(value).updateTime(LocalDateTime.now()).build());
        } else {
            if (!Objects.isNull(value)) {
                account.setNetAsset(value);
                account.setCurrency(currency);
            }
            account.setUpdateTime(LocalDateTime.now());
            accountMapper.updateById(account);
        }
    }

    public Account getByName(String name) {
        EntityWrapper<Account> ew = new EntityWrapper<>();
        ew.where("name = {0}", name);
        List<Account> accounts = accountMapper.selectList(ew);
        return CollectionUtils.isEmpty(accounts) ? null : accounts.get(0);
    }

    public Account getDefault() {
        return accountMapper.selectById(1);
    }
}
