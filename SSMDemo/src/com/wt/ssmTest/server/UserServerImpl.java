package com.wt.ssmTest.server;

import com.wt.ssmTest.dao.IUserDao;
import com.wt.ssmTest.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

@Service("userServer")
public class UserServerImpl {

    @Resource
    private IUserDao iUserDao;

    public User getUserById(int id){
        return iUserDao.getUserById(id);
    }

    public User longin(String username,String password){
        return iUserDao.longin(username,password);
    }

    public List<User> findUser(){
        return iUserDao.findUser();
    }
}
