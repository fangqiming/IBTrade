package com.gougou.ib.company.service;

import com.gougou.ib.company.dao.mapper.OrderNumberMapper;
import com.gougou.ib.company.dao.model.OrderNumber;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderNumberService {

    @Resource
    private OrderNumberMapper orderNumberMapper;

    public Integer getOrderId() {
        List<OrderNumber> orderNumbers = orderNumberMapper.selectList(null);
        //如果没有则将订单号初始化为 800并返回
        if (CollectionUtils.isEmpty(orderNumbers)) {
            OrderNumber orderNumber = OrderNumber.builder().orderId(800).build();
            orderNumberMapper.insert(orderNumber);
            return orderNumber.getOrderId();
        } else {
            OrderNumber orderNumber = orderNumbers.get(0);
            orderNumber.setOrderId(orderNumber.getOrderId() + 1);
            orderNumberMapper.updateById(orderNumber);
            return orderNumber.getOrderId();
        }
    }
}
