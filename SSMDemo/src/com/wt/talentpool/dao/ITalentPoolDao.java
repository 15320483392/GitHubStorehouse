package com.wt.talentpool.dao;

import com.wt.talentpool.entity.TalentPool;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
public interface ITalentPoolDao {

    public List<TalentPool> findTalentPool();
}
