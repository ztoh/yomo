package com.kescoode.android.yomo;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 管理Task的先进先出队列和内部维护一个线程池
 *
 * @author Kesco Lin
 * @since 2014-12-03
 */
/* package */ class TaskQueue {

    private static final String LOG_TAG = "TaskQueue";

    private final ThreadPoolExecutor mThreadPool;
    private Delivery mDelivery;

    private final Object mLock = new Object();

    public TaskQueue() {
        mThreadPool = ThreadPoolTool.newCacheThreadPool();
        mDelivery = new ExecutorDelivery(this);
    }

    public void add(TaskSet task) {
        synchronized (mLock) {
            TaskRunner runner = new TaskRunner(task, mDelivery);
            task.control(mThreadPool.submit(runner));
        }
    }

    public void shutdown() {
        synchronized (mLock) {
            if (!mThreadPool.isShutdown()) {
                mThreadPool.shutdownNow();
            }
        }
    }

}
