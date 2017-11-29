package com.wt.talentpool.server;

import com.wt.talentpool.entity.TalentPool;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
public interface ITalentPoolServer {
    /**
     * 获取全部人员信息
     * @return
     */
    public List<TalentPool> findTalentPool();

    /**
     * 根据id获取单个人员信息
     * @param id
     * @return
     */
    public TalentPool getTalentPoolById(String id);
}
