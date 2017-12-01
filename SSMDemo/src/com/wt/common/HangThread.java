package com.wt.common;

/**
 * Created by Administrator on 2017/11/30.
 */
public class HangThread implements Runnable{

    private volatile int firstVal;
    private volatile int secondVal;
    //增加标志位，用来实现线程的挂起和恢复
    private volatile boolean suspended;

    public boolean areValuesEqual() {
        return ( firstVal == secondVal );
    }

    public void run() {
        try {
            suspended = false;
            firstVal = 0;
            secondVal = 0;
            workMethod();
        } catch ( InterruptedException x ) {
            System.out.println("interrupted while in workMethod()");
        }
    }

    private void workMethod() throws InterruptedException {
        int val = 1;

        while ( true ) {
            //仅当线程挂起时，才运行这行代码
            waitWhileSuspended();

            stepOne(val);
            stepTwo(val);
            val++;
            //仅当线程挂起时，才运行这行代码
            waitWhileSuspended();

            Thread.sleep(200);
        }
    }

    private void stepOne(int newVal)
            throws InterruptedException {

        firstVal = newVal;
        Thread.sleep(300);
    }

    private void stepTwo(int newVal) {
        secondVal = newVal;
    }

    public void suspendRequest() {
        suspended = true;
    }

    public void resumeRequest() {
        suspended = false;
    }

    private void waitWhileSuspended()
            throws InterruptedException {

        //这是一个“繁忙等待”技术的示例。
        //它是非等待条件改变的最佳途径，因为它会不断请求处理器周期地执行检查，
        //更佳的技术是：使用Java的内置“通知-等待”机制
        while ( suspended ) {
            Thread.sleep(200);
        }
    }
}
