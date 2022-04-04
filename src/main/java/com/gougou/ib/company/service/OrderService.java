package com.gougou.ib.company.service;

import com.ib.client.*;
import com.tigerbrokers.stock.openapi.client.https.domain.quote.item.RealTimeQuoteItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderService {

    @Resource
    private OrderNumberService orderNumberService;

    @Resource
    private MarketService marketService;

    @Resource
    private EClientSocket client;

    /**
     * 根据交易金额进行订单下发
     *
     * @param symbol
     * @param conid
     * @param type
     * @param money
     * @param limitPrice
     */
    public void orderMoney(String symbol, Integer conid, OrderType type, Double money, Double limitPrice) {
        RealTimeQuoteItem realTimeMrk = marketService.getRealTimeMrk(symbol);
        Double price = realTimeMrk.getClose();
        Integer qty = BigDecimal.valueOf(money / price).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        orderQty(symbol, conid, type, qty, limitPrice);
    }


    /**
     * 根据数量进行订单的下发
     *
     * @param symbol     股票代码
     * @param conid
     * @param type       市价单/限价单
     * @param qty        交易数量 正数买入 负数卖出
     * @param limitPrice 限价单需要携带此参数,市价单无意义
     */
    public void orderQty(String symbol, Integer conid, OrderType type, Integer qty, Double limitPrice) {
        //1.构建订单对象
        Order order = new Order();
        order.action(qty > 0 ? Types.Action.BUY : Types.Action.SELL);
        order.orderType(type.getApiString());
        order.totalQuantity(Math.abs(qty));
        if (type.equals(OrderType.LMT)) {
            limitPrice = BigDecimal.valueOf(limitPrice).setScale(2, BigDecimal.ROUND_UP).doubleValue();
            order.lmtPrice(limitPrice);
        }
        //2.构建交易对象
        Contract contract = new Contract();
        contract.secType("STK");
        contract.symbol(symbol);
        contract.currency("USD");
        contract.exchange("SMART");
        contract.conid(conid);
        //3.生成订单号
        Integer orderId = orderNumberService.getOrderId();
        //下发订单
        client.placeOrder(orderId, contract, order);
    }

}
