package com.lj.lang.task;

/**
 * 作者：liujian on 2018/9/13 14:12
 * 邮箱：15313727484@163.com
 */
public interface IExecutorManager<T>  {
    //开始
    void start();

    //结束
    void stop();

    //查询任务执行器运行状态
    int getState();

    <T> void setExecutor(IExecutor e);

    //添加任务
    void addTask(T t);

    //删除任务
    void deleteTask(T t);
}
