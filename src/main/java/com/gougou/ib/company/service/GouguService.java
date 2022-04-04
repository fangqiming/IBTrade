package com.gougou.ib.company.service;

import com.alibaba.fastjson.JSONObject;
import com.gougou.ib.company.config.entity.TradeConfig;
import com.gougou.ib.company.dao.mapper.GouguHoldMapper;
import com.gougou.ib.company.dao.mapper.GouguPlanMapper;
import com.gougou.ib.company.dao.model.GouguHold;
import com.gougou.ib.company.dao.model.GouguPlan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GouguService {

    @Resource
    private GouguPlanMapper gouguPlanMapper;

    @Resource
    private GouguHoldMapper gouguHoldMapper;

    @Resource
    private TradeConfig tradeConfig;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取勾股网站的交易数据
     */
    public void fetchGouguData() {
        List<GouguHold> gouguHolds = fetchHold();
        List<GouguPlan> gouguPlans = fetchPlan();
        gouguHoldMapper.truncate();
        gouguPlanMapper.truncate();
        //保存当前持仓信息
        for (GouguHold hold : gouguHolds) {
            gouguHoldMapper.insert(hold);
        }
        //保存交易计划(正确)
        for (GouguPlan plan : gouguPlans) {
            gouguPlanMapper.insert(plan);
        }
    }


    /**
     * 获取勾股的持仓
     *
     * @return
     */
    public List<GouguHold> findHold() {
        return gouguHoldMapper.selectList(null);
    }

    public List<GouguPlan> findPlan() {
        return gouguPlanMapper.selectList(null);
    }

    private String getToken() {
        JSONObject param = new JSONObject();
        param.put("name", tradeConfig.getIbUser());
        param.put("password", tradeConfig.getPwd());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> httpEntity = new HttpEntity<>(param.toJSONString(), header);
        JSONObject jsonObject = restTemplate.postForObject(tradeConfig.getLoginUrl(), httpEntity, JSONObject.class);
        if (isSuccess(jsonObject)) {
            return jsonObject.getJSONObject("data").getJSONObject("entity").getString("accessCode");
        }
        throw new RuntimeException("登陆勾股系统,获取token失败");
    }

    /**
     * 查询勾股系统的交易计划
     *
     * @return
     */
    private List<GouguPlan> fetchPlan() {
        String token = getToken();
        HttpEntity<String> requestEntity = getHeader("Access-code", token);
        JSONObject jsonObject = restTemplate.exchange(tradeConfig.getPlanUrl(), HttpMethod.GET, requestEntity, JSONObject.class).getBody();
        if (isSuccess(jsonObject)) {
            return jsonObject.getJSONObject("data").getJSONArray("entities").toJavaList(GouguPlan.class);
        }
        throw new RuntimeException("从勾股系统获取交易计划失败");
    }

    /**
     * 查询勾股系统的当前持仓
     *
     * @return
     */
    private List<GouguHold> fetchHold() {
        String token = getToken();
        HttpEntity<String> requestEntity = getHeader("Access-code", token);
        JSONObject jsonObject = restTemplate.exchange(tradeConfig.getHoldUrl(), HttpMethod.GET, requestEntity, JSONObject.class).getBody();
        if (isSuccess(jsonObject)) {
            return jsonObject.getJSONObject("data").getJSONArray("entities").toJavaList(GouguHold.class);
        }
        throw new RuntimeException("从勾股系统获取当前持仓失败");
    }

    private boolean isSuccess(JSONObject json) {
        return json.getLong("code") == 0;
    }

    private HttpEntity getHeader(String key, String value) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        header.add(key, value);
        return new HttpEntity<String>(null, header);
    }


}
