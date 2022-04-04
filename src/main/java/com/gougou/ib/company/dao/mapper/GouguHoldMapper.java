package com.gougou.ib.company.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gougou.ib.company.dao.model.GouguHold;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface GouguHoldMapper extends BaseMapper<GouguHold> {

    @Select("truncate gougu_hold")
    void truncate();
}
