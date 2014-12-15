package com.kescoode.android.yomo.toolbox;

import android.os.Looper;

import com.kescoode.android.yomo.YomoError;

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
        G background();
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

    public static enum ExecutorType {
        Serial, Concurrence
    }

    private TaskSet mNext = null;
    private Future mControl;
    private PrepareTask mPrepareTask = null;
    private BackgroundTask<T> mBackgroundTask = null;
    private UiDoneCallBack<T> mDoneCallBack = null;
    private UiFailCallBack mFailCallBack = null;

    private ExecutorType mType = ExecutorType.Concurrence;

    private long mDelayMillisecond = 0L;

    protected TaskSet() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException("Can't not initial taskset in other threads.");
        }
    }

    public final T run() {
        if (null == mBackgroundTask) {
            return background();
        } else {
            return mBackgroundTask.background();
        }
    }

    public final void cancel() {

    }

    /**
     * 子类可以重写该方法
     */
    public T background() {
        return null;
    }

    public final void background(BackgroundTask<T> task) {
        if (null == task) {
            throw new IllegalArgumentException("Task can not be null");
        }
        mBackgroundTask = task;
    }

    public final void done(T result) {
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

    public final void fail(YomoError error) {
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
     * 下一个执行{@link com.kescoode.android.yomo.toolbox.TaskSet}
     */
    public final void next(TaskSet next) {
        if (null == next) {
            throw new IllegalArgumentException("Next TaskSet can not be null");
        }
        mNext = next;
    }

    public final TaskSet next() {
        return mNext;
    }

    public final void control(Future future) {
        mControl = future;
    }

    public final void type(ExecutorType type) {
        mType = type;
    }

    public final ExecutorType type() {
        return mType;
    }

}
