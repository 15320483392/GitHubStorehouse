package com.wt.common;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/11/30.
 */
public class CallableThread implements Callable{

    private volatile int num;

    private volatile List<Object> datalist;

    public CallableThread(int num,List<Object> datalist) {
        this.num = num;
        this.datalist = datalist;
    }

    @Override
    public Object call() throws Exception {
        //项目中使用异步往往能提高项目运行效率，但有时候为了数据不被脏读取，则需要给对象加锁
        //实际操作传入list或Map，遍历对象时，需要加锁
        if(num > 0){
            for(int i = 0 ; i < num ; i++){
                Map<String,Object> mapdata = new Hashtable<>();
                mapdata.put("name",Thread.currentThread());
                mapdata.put("sex",Thread.currentThread());
                mapdata.put("phone","15325485965");
                Map<String,Object> obj = new Hashtable<>();
                obj.put("names","holle");
                mapdata.put("obj",obj);
                mapdata.put("objstr",obj.toString());
                datalist.add(mapdata);
            }
        }

        return datalist;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
