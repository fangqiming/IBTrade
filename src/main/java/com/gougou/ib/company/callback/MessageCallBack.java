package com.gougou.ib.company.callback;

import com.alibaba.fastjson.JSONObject;
import com.gougou.ib.company.dao.model.Conid;
import com.gougou.ib.company.dao.model.Hold;
import com.gougou.ib.company.service.AccountService;
import com.gougou.ib.company.service.ConidService;
import com.gougou.ib.company.service.HoldService;
import com.ib.client.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 此对象为接收处理来自TWS的信息,实质上及TWS相应API车回调类
 */
@Component
public class MessageCallBack implements EWrapper {

    @Getter
    private int nextOrderId;

    @Getter
    private EClientSocket clientSocket;

    @Getter
    private EReaderSignal signal;

    @Resource
    private AccountService accountService;

    @Resource
    private HoldService holdService;

    @Resource
    private ConidService conidService;

    @PostConstruct
    private void init() {
        signal = new EJavaSignal();
        clientSocket = new EClientSocket(this, signal);
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attrib) {
//        PeriodSymbol symbol = redisService.getSymbolBySeq(tickerId);
//        redisService.addRealMrkData(symbol.getSymbol(), field, price, LocalDateTime.now().format(TimeUtil.DF));
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
//        PeriodSymbol symbol = redisService.getSymbolBySeq(tickerId);
//        redisService.addRealMrkData(symbol.getSymbol(), field, Double.valueOf(size), LocalDateTime.now().format(TimeUtil.DF));
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        System.out.println(String.format("tickerId:%s field:%s impliedVol:%s delta:%s optPrice:%s pvDividend:%s gamma:%s vega:%s vega:%s theta:%s undPrice:%s", tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, vega, theta, undPrice));
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
//        System.out.println(String.format("tickerId:%s tickType:%s value:%s", tickerId, tickType, value));
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
//        System.out.println(String.format("tickerId:%s tickType:%s value:%s", tickerId, tickType, value));
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        System.out.println(String.format("tickerId:%s tickType:%s basisPoints:%s formattedBasisPoints:%s impliedFuture:%s holdDays:%s futureLastTradeDate:%s dividendImpact:%s dividendsToLastTradeDate:%s", tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate));
    }

    /**
     * 订单的状态发生变更的通知消息
     *
     * @param orderId
     * @param status
     * @param filled
     * @param remaining
     * @param avgFillPrice
     * @param permId
     * @param parentId
     * @param lastFillPrice
     * @param clientId
     * @param whyHeld
     * @param mktCapPrice
     */
    @Override
    public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
//        String redisKey = RedisKeyHelp.getOrderIdSymbolMap();
//        String symbol = redisService.getMapVByK(redisKey, orderId + "");
//        String orderRedisKey = RedisKeyHelp.getOrderKey(symbol, orderId + "");
//        ThreeUnit unit = ThreeUnit.builder().time(LocalDateTime.now().format(TimeUtil.DF)).avgFillPrice(avgFillPrice)
//                .filled(filled).status(status).orderId(orderId + "").build();
//        redisService.insertKV(orderRedisKey, JSONObject.toJSONString(unit), 10);
    }

    /**
     * 订单未完成的前提下,发生状态的变化通知
     *
     * @param orderId
     * @param contract
     * @param order
     * @param orderState
     */
    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        //System.out.println(String.format("openOrder-->orderId:%s contract:%s order:%s orderState:%s", orderId, JSONObject.toJSONString(contract), JSONObject.toJSONString(order), JSONObject.toJSONString(orderState)));
    }

    /**
     * 订单完成的回调
     *
     * @param contract
     * @param order
     * @param orderState
     */
    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        System.out.println(String.format("completedOrder-->contract:%s order:%s orderState:%s", JSONObject.toJSONString(contract),
                JSONObject.toJSONString(order), JSONObject.toJSONString(orderState)));
    }


    @Override
    public void openOrderEnd() {

    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        if ("USD".equals(currency) && "NetLiquidationByCurrency".equals(key)) {
            accountService.update(currency, Double.valueOf(value), accountName);
        }
    }

    @Override
    public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        Hold hold = Hold.builder().accountName(accountName).averageCost(averageCost).marketPrice(marketPrice).marketValue(marketValue)
                .position(position).unrealized(unrealizedPNL).symbol(contract.symbol()).conid(contract.conid()).build();
        holdService.update(hold);
    }


    @Override
    public void updateAccountTime(String s) {

    }

    @Override
    public void accountDownloadEnd(String s) {

    }

    @Override
    public void nextValidId(int i) {
        System.out.println(String.format("Next valid is : %s", i));
        nextOrderId = i;
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        System.out.println(EWrapperMsgGenerator.contractDetails(reqId, contractDetails));
    }

    @Override
    public void bondContractDetails(int i, ContractDetails contractDetails) {

    }

    @Override
    public void contractDetailsEnd(int reqId) {
        System.out.println("ContractDetailsEnd. " + reqId);
    }

    @Override
    public void execDetails(int i, Contract contract, Execution execution) {

    }

    @Override
    public void execDetailsEnd(int i) {

    }

    @Override
    public void updateMktDepth(int i, int i1, int i2, int i3, double v, int i4) {

    }

    @Override
    public void updateMktDepthL2(int i, int i1, String s, int i2, int i3, double v, int i4, boolean b) {

    }


    @Override
    public void updateNewsBulletin(int i, int i1, String s, String s1) {

    }

    @Override
    public void managedAccounts(String accountName) {
        accountService.update(null, null, accountName);
    }

    @Override
    public void receiveFA(int i, String s) {

    }

    @Override
    public void historicalData(int reqId, Bar bar) {
//        PeriodSymbol symbolBySeq = redisService.getSymbolBySeq(reqId);
//        DataBar dataBar = new DataBar(bar, symbolBySeq.getSymbol(), symbolBySeq.getPeriod());
//        redisService.addBar(dataBar);
    }


    @Override
    public void scannerParameters(String s) {

    }

    @Override
    public void scannerData(int i, int i1, ContractDetails contractDetails, String s, String s1, String s2, String s3) {

    }

    @Override
    public void scannerDataEnd(int i) {

    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
        System.out.println(String.format("reqId:%s time:%s open:%s high:%s low:%s close:%s volume:%s wap:%s count:%s", reqId, time, open, high, low, close, volume, wap, count));
    }


    @Override
    public void currentTime(long l) {

    }

    @Override
    public void fundamentalData(int i, String s) {

    }

    @Override
    public void deltaNeutralValidation(int i, DeltaNeutralContract deltaNeutralContract) {

    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        System.out.println(String.format("reqId:%s", reqId));
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        System.out.println(String.format("reqId:%s marketDataType:%s", reqId, marketDataType));
    }


    @Override
    public void commissionReport(CommissionReport commissionReport) {

    }

    @Override
    public void position(String account, Contract contract, double pos, double avgCost) {
        System.out.println(String.format("account:%s contract:%s pos:%s avgCost:%s", account, JSONObject.toJSONString(contract), pos, avgCost));
    }

    @Override
    public void positionEnd() {

    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {

    }

    @Override
    public void accountSummaryEnd(int reqId) {

    }

    @Override
    public void verifyMessageAPI(String apiData) {

    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {

    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {

    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {

    }

    @Override
    public void displayGroupList(int reqId, String groups) {

    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {

    }


    @Override
    public void error(Exception e) {
        System.out.println(String.format("TWS Error message : %s", e));
    }

    @Override
    public void error(String s) {
        System.out.println(String.format("Net Error message : %s"));
    }

    @Override
    public void error(int i, int i1, String s) {
        System.out.println(String.format("TWS Error: id:%s code:%s message:%s", i, i1, s));
    }

    @Override
    public void connectionClosed() {
        System.out.println("Connection to TWS closed");
    }

    @Override
    public void connectAck() {
        if (clientSocket.isAsyncEConnect()) {
            clientSocket.startAPI();
        }
    }

    @Override
    public void positionMulti(int i, String s, String s1, Contract contract, double v, double v1) {

    }

    @Override
    public void positionMultiEnd(int i) {

    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode, String key, String value, String currency) {
        System.out.println(String.format(" reqId %s,  account %s,  modelCode %s,  key %s,  value %s,  currency %s", reqId, account, modelCode, key, value, currency));
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {

    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {

    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {

    }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {

    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {

    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        for (ContractDescription cd : contractDescriptions) {
            Contract contract = cd.contract();
            String exchange = contract.primaryExch();
            System.out.println(contract);
            if("USD".equals(contract.currency())){
                if (exchange.contains("NASDAQ") || exchange.contains("NYSE") || exchange.contains("AMEX")) {
                    conidService.updateConid(contract.symbol(),contract.conid());
                }
            }
        }
    }


    @Override
    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        System.out.println(String.format("History reqId : %s startDateStr : %s endDateStr %s", reqId, startDateStr, endDateStr));
    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {

    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {

    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Map.Entry<String, Character>> theMap) {

    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {

    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {

    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {

    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {

    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {

    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        System.out.println(String.format("reqId : %s ", reqId));
        System.out.println(items);
    }

    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
//        PeriodSymbol symbolBySeq = redisService.getSymbolBySeq(reqId);
//        DataBar dataBar = new DataBar(bar, symbolBySeq.getSymbol(), symbolBySeq.getPeriod());
//        redisService.addTempBar(dataBar);
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {

    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {

    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {

    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {

    }

    @Override
    public void pnlSingle(int reqId, int pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {

    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        System.out.println(ticks);
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        System.out.println(ticks);
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        System.out.println(ticks);
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, int size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {

    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize, TickAttribBidAsk tickAttribBidAsk) {

    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {

    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        System.out.println(String.format("orderId:%s apiClientId:%s apiOrderId:%s", orderId, apiClientId, apiOrderId));
    }


    @Override
    public void completedOrdersEnd() {

    }


}
