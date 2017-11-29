package com.wt.ssmTest.dao;

import com.wt.ssmTest.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

public interface IUserDao {

    public User getUserById(int id);

    public User longin(String username,String password);

    public List<User> findUser();
}
