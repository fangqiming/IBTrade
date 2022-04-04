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
public class GouguPlan {
    @TableId
    private Long id;
    private String newDate;
    private String name;
    private String type;
    private String action;
}
