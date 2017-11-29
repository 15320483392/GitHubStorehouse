package com.wt.talentpool.server;

import com.wt.talentpool.entity.TalentPool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
@Service("talentPoolServerImpl")
public class TalentPoolServerImpl implements ITalentPoolServer{
    @Resource
    private ITalentPoolServer iTalentPoolServer;

    @Override
    public List<TalentPool> findTalentPool() {
        return iTalentPoolServer.findTalentPool();
    }
}
