package com.gougou.ib.company.dao.model;

import com.baomidou.mybatisplus.annotations.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GouguHold {

    @TableId
    private Long id;
    private String name;
    private String oldDate;
    private BigDecimal oldPrice;
    private String newDate;
    private BigDecimal newPrice;
    private String type;
}
