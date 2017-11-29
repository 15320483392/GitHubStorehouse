package com.wt.ssmTest.controller;

import com.wt.common.BaseAction;
import com.wt.ssmTest.entity.User;
import com.wt.ssmTest.server.UserServerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */
@Controller
@RequestMapping("/UserController")
public class UserController extends BaseAction{

    @Autowired
    private UserServerImpl userServer;

    @RequestMapping(value = "/getUser")
    public void getUser(User user){
        System.out.println("参数:"+user);
        User users = userServer.getUserById(1);
        System.out.println(users);
    }

    @RequestMapping(value = "/longin")
    public void longin(User user){
        //@Validated User 多钟方式验证实体类
        System.out.println("登录:"+userServer.longin(user.getUsername(),user.getPassword()));
    }

    @RequestMapping(value = "/getdata")
    public void getdata(){
        List<User> listUser = userServer.findUser();
        System.out.println("获取数据:"+listUser);
        writeJson(listUser);
    }

    @RequestMapping(value = "/otherpag",method = RequestMethod.GET)
    public ModelAndView otherpag(){
        ModelAndView modelAndView = new ModelAndView("success");
        //redirect forward
        modelAndView.addObject("success","跳转页面!");
        return modelAndView;
    }
}
