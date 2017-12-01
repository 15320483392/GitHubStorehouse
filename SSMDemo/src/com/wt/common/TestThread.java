package com.wt.common;

/**
 * 一个没有返回值的多线程
 * Created by Administrator on 2017/11/30.
 */
public class TestThread implements Runnable{

    private volatile int num = 10;

    @Override
    public void run() {
        for (int i=0;i<num;i++) {
            //线程锁
            synchronized (this) {
                if (num > 0) {
                    System.out.println(Thread.currentThread().getName() + ":num = " + num--);
                }
            }
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
