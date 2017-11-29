package com.wt.talentpool.server;

import com.wt.talentpool.entity.TalentPool;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
public interface ITalentPoolServer {
    public List<TalentPool> findTalentPool();
}
