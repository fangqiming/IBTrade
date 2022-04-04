package com.gougou.ib.company.service;

import com.gougou.ib.company.config.entity.TradeConfig;
import com.gougou.ib.company.dao.mapper.TradePlanMapper;
import com.gougou.ib.company.dao.model.*;
import com.gougou.ib.company.entity.ActionEnum;
import com.gougou.ib.company.entity.Diff;
import com.ib.client.OrderType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TradePlanService {

    @Resource
    private TradePlanMapper tradePlanMapper;

    @Resource
    private GouguService gouguService;

    @Resource
    private AccountService accountService;

    @Resource
    private HoldService holdService;

    @Resource
    private TradeConfig tradeConfig;

    @Resource
    private SubService subService;

    @Resource
    private OrderService orderService;

    @Resource
    private MarketService marketService;


    /**
     * 利用勾股数据生成自己的交易计划
     */
    public void createPlan() {
        //从勾股获取了表结构
        tradePlanMapper.truncate();
        gouguService.fetchGouguData();
        List<GouguPlan> plans = gouguService.findPlan();
        Account account = accountService.getDefault();
        double sharePercent = tradeConfig.getLever() / tradeConfig.getTotalStock();
        double shareMoney = account.getNetAsset() * sharePercent;
        for (GouguPlan plan : plans) {
            ActionEnum action = ActionEnum.getByAction(plan.getAction());
            //开仓
            TradePlan tradePlan = null;
            if (action.equals(ActionEnum.BUY) || action.equals(ActionEnum.SHORT)) {
                Double money = (action.equals(ActionEnum.BUY) ? 1 : -1) * shareMoney;
                tradePlan = TradePlan.builder().action(action.name()).conid(subService.getConid(plan.getName()))
                        .money(money).symbol(plan.getName()).build();
            }
            //平仓
            if (action.equals(ActionEnum.SELL) || action.equals(ActionEnum.COVER)) {
                tradePlan = TradePlan.builder().action(action.name()).conid(subService.getConid(plan.getName()))
                        .money(0.0).symbol(plan.getName()).build();
            }
            tradePlanMapper.insert(tradePlan);
        }
    }

    /**
     * 获取勾股持仓与实盘持仓的差异
     *
     * @return
     */
    public Diff findDiff() {
        Diff result = Diff.builder().cover(new ArrayList<>()).long1(new ArrayList<>()).multi(new ArrayList<>()).build();
        List<GouguHold> gouguHolds = gouguService.findHold();
        List<Hold> holds = holdService.find();
        Map<String, List<GouguHold>> gougu = gouguHolds.stream().collect(Collectors.groupingBy(GouguHold::getName));
        Map<String, List<Hold>> hold = holds.stream().collect(Collectors.groupingBy(Hold::getSymbol));
        for (Map.Entry<String, List<GouguHold>> entry : gougu.entrySet()) {
            if (!hold.containsKey(entry.getKey())) {
                //勾股有而实盘没有
                result.getLong1().add(entry.getKey());
            }
            if (entry.getValue().size() > 1) {
                //勾股的多份的
                result.getMulti().add(entry.getKey());
            }
        }
        for (Map.Entry<String, List<Hold>> entry : hold.entrySet()) {
            if (!gougu.containsKey(entry.getKey())) {
                //实盘有勾股没有
                result.getCover().add(entry.getKey());
            }
        }
        result.setTip("请重置勾美股交易配置");
        return result;
    }

    /**
     * 按照交易计划进行交易
     */
    public void trade() {
        List<TradePlan> tradePlans = tradePlanMapper.selectList(null);
        for (TradePlan plan : tradePlans) {
            if (plan.getMoney() != 0) {
                if (marketService.canTrade(plan.getSymbol(), ActionEnum.getByAction(plan.getAction()))) {
                    orderService.orderMoney(plan.getSymbol(), plan.getConid(), OrderType.MKT, plan.getMoney(), null);
                }
            } else {
                //平仓
                Integer holdQty = holdService.getQtyBySymbol(plan.getSymbol());
                orderService.orderQty(plan.getSymbol(), plan.getConid(), OrderType.MKT, -holdQty, null);
            }
        }
    }

    /**
     * 资金变动,对应的股票需要批量加减
     *
     * @param money             正数表示钱款转入,负数表示取出
     * @param beforeChangeAsset 账户变化之前的资金
     */
    public void accountChange(Double money, Double beforeChangeAsset) {
        if (Math.abs(money) > beforeChangeAsset * 0.05) {
            //转入就是正数,转出就是负数
            Integer change = money > 0 ? 1 : -1;
            //降低的比例
            double percent = Math.abs(money) / beforeChangeAsset;
            List<Hold> holds = holdService.find();
            for (Hold hold : holds) {
                int changeQty = BigDecimal.valueOf(hold.getPosition() * percent).setScale(0, BigDecimal.ROUND_DOWN).intValue();
                //转出就需要卖出
                int qty = changeQty * change;
                orderService.orderQty(hold.getSymbol(), hold.getConid(), OrderType.MKT, qty, null);
            }
        }
    }


}
