package com.gougou.ib.company.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gougou.ib.company.dao.model.Hold;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldMapper extends BaseMapper<Hold> {

    @Select("truncate hold")
    void truncate();
}
