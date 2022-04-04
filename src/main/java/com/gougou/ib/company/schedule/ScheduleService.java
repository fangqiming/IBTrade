package com.gougou.ib.company.schedule;

import com.gougou.ib.company.dao.model.TradePlan;
import com.gougou.ib.company.service.MarketService;
import com.gougou.ib.company.service.TradePlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ScheduleService {

    @Resource
    private MarketService marketService;

    @Resource
    private TradePlanService tradePlanService;

    /**
     * 临近收盘时执行勾股的交易计划
     */
    @Scheduled(cron = "25 55-59 0,1,3,4 * * ?")
    public void closeGougu() {
        if (marketService.isOpenStatus() && marketService.isCloseGougu()) {
            System.out.println("执行收盘交易计划");
            tradePlanService.trade();
        }
    }

}
