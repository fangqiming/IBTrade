package com.gougou.ib.company.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diff {

    /**
     * 需要做多的
     */
    private List<String> long1;

    /**
     * 需要平仓的
     */
    private List<String> cover;

    //勾股有多份的
    private List<String> multi;

    private String tip ;
}
