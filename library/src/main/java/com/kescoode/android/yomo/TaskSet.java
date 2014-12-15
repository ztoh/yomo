package com.kescoode.android.yomo;

import android.os.Looper;

import java.util.concurrent.Future;

/**
 * 任务串主键
 *
 * @author Kesco Lin
 */
public class TaskSet<T> {

    public static interface PrepareTask {
        void prepare();
    }

    public static interface BackgroundTask<G> {
        G background() throws Exception;
    }

    /**
     * 执行完成的回调
     *
     * @param <G> 泛型必须是实现了可序列化接口
     */
    public static interface UiDoneCallBack<G> {
        void done(G result);
    }

    public static interface UiFailCallBack {
        void fail(YomoError error);
    }

    private TaskSet mNext = null;
    private volatile Future mControl = null;
    private PrepareTask mPrepareTask = null;
    private BackgroundTask<T> mBackgroundTask = null;
    private UiDoneCallBack<T> mDoneCallBack = null;
    private UiFailCallBack mFailCallBack = null;

    private long mDelayMillisecond = 0L;
    private volatile boolean mCanceled = false;
    private Object mTag;

    protected TaskSet() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("Can't not initial taskset in other threads.");
        }
    }

    public final T run() throws Exception {
        if (null == mBackgroundTask) {
            return background();
        } else {
            return mBackgroundTask.background();
        }
    }

    /**
     * 有时候每个线程调度时间不一致，所以在想取消的时候有可能任务已经完成了或者还没进行
     */
    public final void cancel() {
        if (null != mControl) {
            mControl.cancel(true);
        }
        mCanceled = true;
    }

    /* package */ boolean isCanceled() {
        return mCanceled;
    }

    /**
     * 子类可以重写该方法
     */
    public T background() throws Exception {
        return null;
    }

    public final void background(BackgroundTask<T> task) {
        if (null == task) {
            throw new IllegalArgumentException("Task can not be null");
        }
        mBackgroundTask = task;
    }

    /* package */ void done(T result) {
        if (null != mDoneCallBack) {
            mDoneCallBack.done(result);
        }
    }

    public final void done(UiDoneCallBack<T> callBack) {
        if (null == callBack) {
            throw new IllegalArgumentException("Callback can not be null");
        }
        mDoneCallBack = callBack;
    }

    /* package */ void fail(YomoError error) {
        if (null != mFailCallBack) {
            mFailCallBack.fail(error);
        }
    }

    public final void fail(UiFailCallBack callBack) {
        if (null == callBack) {
            throw new IllegalArgumentException("CallBack can not be null");
        }
        mFailCallBack = callBack;
    }

    public static final void throwFailed(Exception cause) throws Exception {
        if (null == cause) {
            throw new RuntimeException("Error thrown from user");
        } else {
            throw cause;
        }
    }

    /**
     * 返回Ui Thread的延迟
     */
    public final void delay(long millisecond) {
        mDelayMillisecond = millisecond;
    }

    public final long delay() {
        return mDelayMillisecond;
    }

    /**
     * 下一个执行{@link TaskSet}
     */
    public final void next(TaskSet next) {
        if (null == next) {
            throw new IllegalArgumentException("Next TaskSet can not be null");
        }
        mNext = next;
    }

    /* package */ TaskSet next() {
        return mNext;
    }

    /* package */ void control(Future future) {
        mControl = future;
    }

    /* package */ void setTag(Object tag) {
        mTag = tag;
    }

    /* package */ Object getTag() {
        return mTag;
    }
}
