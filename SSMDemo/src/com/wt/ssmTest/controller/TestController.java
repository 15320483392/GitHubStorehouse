package com.wt.ssmTest.controller;

import com.wt.common.CallableThread;
import com.wt.common.HangThread;
import com.wt.common.TestThread;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/11/30.
 */
public class TestController {

    /**
     * 测试线程挂起
     */
    public static void tesThangThread(){
        HangThread asr = new HangThread();

        Thread t = new Thread(asr);
        t.start();

        //休眠1秒，让其他线程有机会获得执行
        try {
            Thread.sleep(1000);
        } catch ( InterruptedException x ) {

        }

        for ( int i = 0; i < 10; i++ ) {
            asr.suspendRequest();

            //让线程有机会注意到挂起请求
            //注意：这里休眠时间一定要大于
            //stepOne操作对firstVal赋值后的休眠时间，即300ms，
            //目的是为了防止在执行asr.areValuesEqual（）进行比较时,
            //恰逢stepOne操作执行完，而stepTwo操作还没执行
            try {
                Thread.sleep(350);
            } catch ( InterruptedException x ) {

            }

            System.out.println("dsr.areValuesEqual()=" +
                    asr.areValuesEqual());

            asr.resumeRequest();

            try {
                //线程随机休眠0~2秒
                Thread.sleep(
                        ( long ) (Math.random() * 2000.0) );
            } catch ( InterruptedException x ) {
                //略
            }
        }

        System.exit(0); //退出应用程序
    }

    /**
     * 有返回值的多线程
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void testCallableThread() throws ExecutionException, InterruptedException {
        System.out.println("----程序开始运行----");
        Date nowtime = new Date();

        int taskSize = 5;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);

        //List<Object> datalist = new CopyOnWriteArrayList<>();
        //效果明显不一样
        List<Object> datalist = Collections.synchronizedList(new ArrayList<>());
        // 创建多个有返回值的任务
        for (int i = 0; i < taskSize; i++) {
            Callable c = new CallableThread(100000*(i+1),datalist);
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            datalist = (List<Object>) f.get();
        }
        // 关闭线程池
        pool.shutdown();
        Date nowtime1 = new Date();
        System.out.println("数据量:"+datalist.size()+"，耗时:"+((nowtime1.getTime() - nowtime.getTime()))+"ms");
        //获取wordList实例的对象锁，
        //迭代时，阻塞其他线程调用add或remove等方法修改元素
       /* synchronized ( datalist ) {
            Iterator iter = datalist.iterator();
            while ( iter.hasNext() ) {
                String s = (String) iter.next();
                System.out.println("found string: " + s + ", length=" + s.length());
            }
        }*/
    }

    /**
     * 无返回值的多线程
     */
    public static void testTestThread(){
        //无返回中线程
        TestThread testThread = new TestThread();
        testThread.setNum(100);
        Thread t1 = new Thread(testThread);
        Thread t2 = new Thread(testThread);
        Thread t3 = new Thread(testThread);
        t1.start();
        t2.start();
        t3.start();
    }

    @Test
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //tesThangThread();
        testCallableThread();
    }
}
