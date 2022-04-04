package com.gougou.ib.company.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gougou.ib.company.dao.mapper.HoldMapper;
import com.gougou.ib.company.dao.model.Hold;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class HoldService {

    @Resource
    private HoldMapper holdMapper;

    public void truncate() {
        holdMapper.truncate();
    }

    public List<Hold> find() {
        EntityWrapper<Hold> ew = new EntityWrapper<>();
        ew.where("position !=0");
        return holdMapper.selectList(ew);
    }

    public Integer getQtyBySymbol(String symbol) {
        EntityWrapper<Hold> ew = new EntityWrapper<>();
        ew.where("symbol = {0}", symbol);
        List<Hold> holds = holdMapper.selectList(ew);
        Double position = CollectionUtils.isEmpty(holds) ? 0 : holds.get(0).getPosition();
        return position.intValue();
    }

    public Hold getByConid(Integer conid) {
        EntityWrapper<Hold> ew = new EntityWrapper<>();
        ew.where("conid = {0}", conid);
        List<Hold> holds = holdMapper.selectList(ew);
        return CollectionUtils.isEmpty(holds) ? null : holds.get(0);
    }

    public void update(Hold hold) {
        Hold h = getByConid(hold.getConid());
        if (Objects.isNull(h)) {
            hold.setUpdateTime(LocalDateTime.now());
            holdMapper.insert(hold);
        } else {
            hold.setId(h.getId());
            hold.setUpdateTime(LocalDateTime.now());
            holdMapper.updateById(hold);
        }
    }
}
