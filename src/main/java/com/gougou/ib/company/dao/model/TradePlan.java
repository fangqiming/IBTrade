package com.gougou.ib.company.dao.model;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradePlan {

    @TableId
    private Long id;

    private String symbol;

    private Integer conid;

    private String action;

    private Double money;
}
