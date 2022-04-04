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
public class Account {

    @TableId
    private Long id;

    /**
     * 账户名
     */
    private String name;

    /**
     * 净资产
     */
    private Double netAsset;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
