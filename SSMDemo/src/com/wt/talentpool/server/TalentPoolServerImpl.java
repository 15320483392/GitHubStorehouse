package com.wt.talentpool.server;

import com.wt.talentpool.dao.ITalentPoolDao;
import com.wt.talentpool.entity.TalentPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
@Service("talentPoolServerImpl")
public class TalentPoolServerImpl implements ITalentPoolServer{

    @Autowired
    private ITalentPoolDao iTalentPoolDao;

    @Override
    public List<TalentPool> findTalentPool() {
        return iTalentPoolDao.findTalentPool();
    }

    @Override
    public TalentPool getTalentPoolById(String id) {
        return iTalentPoolDao.getTalentPoolById(Integer.parseInt(id));
    }

}
