package com.gougou.ib.company.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gougou.ib.company.dao.model.TradePlan;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TradePlanMapper extends BaseMapper<TradePlan> {

    @Select("truncate trade_plan")
    void truncate();
}
