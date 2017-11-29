package com.wt.talentpool.action;

import com.wt.common.BaseAction;
import com.wt.talentpool.entity.TalentPool;
import com.wt.talentpool.server.ITalentPoolServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */
@Controller
@RequestMapping("/talentPoolAction")
public class TalentPoolAction extends BaseAction{

    @Autowired
    private ITalentPoolServer iTalentPoolServer;

    private List<TalentPool> talentPoolList;

    @RequestMapping(value = "/findTalentPool")
    public void findTalentPool(){
        talentPoolList = new ArrayList<>();
        talentPoolList = iTalentPoolServer.findTalentPool();
        writeJson(talentPoolList);
    }
}
