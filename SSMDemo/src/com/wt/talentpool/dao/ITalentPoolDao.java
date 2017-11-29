package com.wt.talentpool.dao;

import com.wt.talentpool.entity.TalentPool;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
public interface ITalentPoolDao {

    /**
     * 获取全部人员信息
     * @return
     */
    public List<TalentPool> findTalentPool();

    /**
     * 根据id获取人员信息
     * @param id
     * @return
     */
    public TalentPool getTalentPoolById(int id);
}
