package com.lj.lang.task;

import com.lj.app.AppExecutors;
import com.lj.lang.develop.log.LjLogger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 作者：liujian on 2018/9/13 14:10
 * 邮箱：15313727484@163.com
 */
public class DefaultExecutor<T> implements IExecutorManager<T> {
    private static final String TAG = DefaultExecutor.class.getSimpleName();
    private BlockingQueue<T> queue = new LinkedBlockingDeque<>();
    private Thread t = new Thread(new MyRunner());
    private IExecutor<T> executor;
    private boolean flag = true;

    public DefaultExecutor(IExecutor<T> executor) {
        this.executor = executor;
    }

    @Override
    public void start() {
        //开启线程
        flag = true;
        t.start();
    }

    @Override
    public void stop() {
        //结束线程
        flag = false;
        t.interrupt();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void addTask(T t) {
        queue.add(t);
    }

    @Override
    public void deleteTask(T t) {

    }

    @Override
    public void setExecutor(IExecutor e) {
        this.executor = e;
    }

    private class MyRunner implements Runnable {

        @Override
        public void run() {
            //实际运行
            while (flag) {
                try {
                    T t = queue.take();
                    AppExecutors.instance.networkIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            LjLogger.instance.log(TAG, "执行任务:" + Thread.currentThread().getName());
                            executor.execute(t);
                            LjLogger.instance.log(TAG, "执行任务完成:" + Thread.currentThread().getName());
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}