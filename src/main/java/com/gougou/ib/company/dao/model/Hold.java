package com.gougou.ib.company.dao.model;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hold {

    @TableId
    private Long id;
    private Integer conid;
    //股票代码
    private String symbol;
    //持股数量
    private Double position;
    //每股市价
    private Double marketPrice;
    //持股市值
    private Double marketValue;
    //平均成本
    private Double averageCost;
    //盈亏额
    private Double unrealized;
    //账户名
    private String accountName;
    //更新时间
    private LocalDateTime updateTime;
}
