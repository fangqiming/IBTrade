package com.gougou.ib.company.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gougou.ib.company.dao.model.GouguPlan;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface GouguPlanMapper extends BaseMapper<GouguPlan> {

    @Select("truncate gougu_plan")
    void truncate();
}
