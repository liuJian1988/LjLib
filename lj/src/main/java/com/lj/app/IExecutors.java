package com.lj.app;

import java.util.concurrent.Executor;

/**
 * 作者：liujian on 2018/6/28 13:56
 * 邮箱：15313727484@163.com
 */
public interface IExecutors {
    Executor diskIO();

    Executor networkIO();

    Executor mainThread();

}
