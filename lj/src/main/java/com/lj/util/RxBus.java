package com.lj.util;

import android.os.Bundle;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 作者：liujian on 2018/6/28 11:37
 * 邮箱：15313727484@163.com
 */
public class RxBus {
    private final FlowableProcessor<Object> mBus;

    private RxBus() {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxBus get() {
        return Holder.BUS;
    }

    public void post(Event e) {
        mBus.onNext(e);
    }

    public void post(int what, Object data) {
        RxBus.Event event = new RxBus.Event();
        event.what = what;
        event.object = data;
        post(event);
    }

    public void post(int what, Object data, int arg1, int arg2) {
        RxBus.Event event = new RxBus.Event();
        event.what = what;
        event.object = data;
        event.arg1 = arg1;
        event.arg2 = arg2;
        post(event);
    }

    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Flowable<Object> toFlowable() {
        return mBus;
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }

    public void postEmptyEvent(int what) {
        RxBus.Event e = new RxBus.Event();
        e.what = what;
        post(e);
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }

    /**
     * 事件消息
     */
    public static class Event {
        public static final int STRING = 0;
        public static final int CUSTOMER_OBJECT = 1;

        public int arg1;
        public int arg2;
        public int what;
        public Object object;
        private Bundle bundle;

        public Event() {

        }

        public Bundle getBundle() {
            return bundle;
        }

        public void setBundle(Bundle bundle) {
            this.bundle = bundle;
        }
    }
}
