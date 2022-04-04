package com.gougou.ib.company.service;

import com.gougou.ib.company.config.entity.TradeConfig;
import com.gougou.ib.company.dao.mapper.MarketMapper;
import com.gougou.ib.company.dao.model.Market;
import com.gougou.ib.company.entity.ActionEnum;
import com.gougou.ib.company.util.TimeUtil;
import com.tigerbrokers.stock.openapi.client.https.client.TigerHttpClient;
import com.tigerbrokers.stock.openapi.client.https.domain.quote.item.RealTimeQuoteItem;
import com.tigerbrokers.stock.openapi.client.https.request.quote.QuoteMarketRequest;
import com.tigerbrokers.stock.openapi.client.https.request.quote.QuoteRealTimeQuoteRequest;
import com.tigerbrokers.stock.openapi.client.https.response.quote.QuoteMarketResponse;
import com.tigerbrokers.stock.openapi.client.https.response.quote.QuoteRealTimeQuoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Service
public class MarketService {

    @Resource
    private TigerHttpClient client;

    @Resource
    private TradeConfig tradeConfig;

    @Resource
    private MarketMapper marketMapper;


    /**
     * 获取股票的实时数据
     *
     * @param symbol
     * @return
     */
    public RealTimeQuoteItem getRealTimeMrk(String symbol) {
        QuoteRealTimeQuoteResponse response = client.execute(QuoteRealTimeQuoteRequest.newRequest(Arrays.asList(symbol)));
        if (response.isSuccess()) {
            return response.getRealTimeQuoteItems().get(0);
        }
        log.warn("{} 实时数据获取失败:{}", symbol, response.getMessage());
        return null;
    }

    /**
     * 判定是否能够交易
     * @param symbol
     * @param action
     * @return
     */
    public boolean canTrade(String symbol, ActionEnum action) {
        //如果是开仓
        if (ActionEnum.isOpenPosition(action)) {
            RealTimeQuoteItem realTimeMrk = getRealTimeMrk(symbol);
            //昨天的收盘价
            Double preClose = realTimeMrk.getPreClose();
            //现在的价格
            Double price = realTimeMrk.getClose();
            double wave = (price / preClose - 1);
            boolean canBuy = (wave < tradeConfig.getRiseHigh()) && action.equals(ActionEnum.BUY);
            boolean canShort = (wave > -tradeConfig.getRiseHigh()) && action.equals(ActionEnum.SHORT);
            return canBuy || canShort;
        }
        return true;
    }

    public Boolean isCloseGougu() {
        Market market = marketMapper.selectById(1L);
        String hour = market.getCloseTime().split(":")[0];
        String beforeHour = "0" + (Integer.parseInt(hour) - 1);
        String beforeClose = beforeHour + ":" + market.getCloseGougu();
        String timeStr = LocalDateTime.now().format(TimeUtil.DF_TIME);
        return beforeClose.equals(timeStr);
    }

    /**
     * 判定市场状态
     *
     * @return
     */
    public Boolean isOpenStatus() {
        QuoteMarketResponse response = client.execute(QuoteMarketRequest.newRequest(com.tigerbrokers.stock.openapi.client.struct.enums.Market.US));
        return "Trading".equals(response.getMarketItems().get(0).getMarketStatus());
    }
}
