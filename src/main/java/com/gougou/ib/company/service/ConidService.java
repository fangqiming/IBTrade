package com.gougou.ib.company.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gougou.ib.company.dao.mapper.ConidMapper;
import com.gougou.ib.company.dao.model.Conid;
import com.ib.client.EWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class ConidService {

    @Resource
    private ConidMapper conidMapper;


    public Integer getConid(String symbol) {
        EntityWrapper<Conid> ew = new EntityWrapper<>();
        ew.where("symbol = {0}", symbol);
        List<Conid> conids = conidMapper.selectList(ew);
        return (CollectionUtils.isEmpty(conids)) ? null : conids.get(0).getConid();
    }

    public void updateConid(String symbol, Integer conid) {
        Conid c = Conid.builder().symbol(symbol).conid(conid).build();
        if (Objects.isNull(getConid(symbol))) {
            conidMapper.insert(c);
        }
    }
}
