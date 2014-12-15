package com.kescoode.android.yomo;

import com.kescoode.android.yomo.toolbox.TaskSet;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 管理Task的先进先出队列和内部维护一个线程池
 *
 * @author Kesco Lin
 * @since 2014-12-03
 */
public class TaskQueue {

    private static final String LOG_TAG = "TaskQueue";

    private final ThreadPoolExecutor mConcurrentPool;
    private final ThreadPoolExecutor mSerialPool;
    private Delivery mDelivery;

    private final Object mLock = new Object();

    public TaskQueue() {
        mConcurrentPool = ThreadPoolTool.newCacheThreadPool();
        mSerialPool = ThreadPoolTool.newSingleThreadPool();
        mDelivery = new ExecutorDelivery(this);
    }

    public void add(TaskSet task) {
        synchronized (mLock) {
            TaskRunner runner = new TaskRunner(task, mDelivery);
            if (TaskSet.ExecutorType.Concurrence == task.type()) {
                task.control(mConcurrentPool.submit(runner));
            } else if (TaskSet.ExecutorType.Serial == task.type()) {
                task.control(mSerialPool.submit(runner));
            }
        }
    }

    public void shutdown() {
        synchronized (mLock) {
            if (!mConcurrentPool.isShutdown()) {
                mConcurrentPool.shutdownNow();
            }
            if (!mSerialPool.isShutdown()) {
                mSerialPool.shutdownNow();
            }
        }
    }

}
