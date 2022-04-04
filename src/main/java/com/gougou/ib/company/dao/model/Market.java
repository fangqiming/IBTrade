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
public class Market {

    @TableId
    private Long id;

    /**
     * 当天是否不开盘
     */
    private Integer isClose;

    /**
     * 开盘时间
     */
    private String openTime;

    /**
     * 收盘时间
     */
    private String closeTime;

    /**
     * 收盘回补时间
     */
    private String closeCovering;

    /**
     * 收盘勾股执行时间
     */
    private String closeGougu;
}
