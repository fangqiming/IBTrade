package com.gougou.ib.company.controller;

import com.gougou.ib.company.service.MarketService;
import com.gougou.ib.company.service.SubService;
import com.gougou.ib.company.service.TradePlanService;
import com.gougou.ib.company.thread.ThreadSleep;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private SubService subService;

    @Resource
    private TradePlanService tradePlanService;

    @Resource
    private MarketService marketService;

    /**
     * 对账户进行订阅
     *
     * @return
     */
    @GetMapping("/sub")
    public Object sub() {
        //订阅账户
        subService.subAccount();
        //生成实盘交易计划
        tradePlanService.createPlan();
        //返回实盘与勾股的差异
        ThreadSleep.sleep(10000);
        return tradePlanService.findDiff();
    }

    /**
     * 转账交易
     *
     * @param change    change值为负数时为转出/赎回 , 为正数时为转入/申购 , 绝对值为对应的金额
     * @param before    在没有申购/赎回时,账户对应的金额
     * @param direction 值为转入/转出 目的在于与change做一次校验,防止调用错误
     * @return
     */
    @GetMapping("/transfer")
    public Object transfer(@RequestParam Double change, @RequestParam Double before, @RequestParam String direction) {
        if (("转出".equals(direction) && change < 0) || ("转入".equals(direction) && change > 0)) {
            if (Math.abs(change) < before) {
                tradePlanService.accountChange(change, before);
                return "批量交易成功";
            }
        }
        return "参数错误";
    }

    /**
     * 针对之前没有交易的股票,进行市价单再次交易
     * 需要将待交易的股票,手动补充到 trade_plan 表中,表字段解释如下
     * action 为 SELL , BUY , COVER ,SHORT
     * money 当为平仓时,money为 0, BUY 为正数,SHORT 为负数
     *
     * @return
     */
    @GetMapping("/retrade")
    public Object reTrade() {
        //调用此接口之前,需要先调用 sub ,否则可能会出错
        tradePlanService.trade();
        return "处理完成后,需要将程序关了,防止程序自动交易";
    }

    /**
     * 获取指定股票的Conid
     *
     * @param symbol
     * @return
     */
    @GetMapping("/get_conid")
    public Object getConid(@RequestParam String symbol) {
        symbol = symbol.toUpperCase();
        return subService.getConid(symbol);
    }


    /**
     * 测试用接口
     *
     * @return
     */
    @GetMapping("/test")
    public Object test() {
        return marketService.getRealTimeMrk("AAPL");
    }


}
