package com.kescoode.android.yomo;

import java.util.HashSet;
import java.util.Set;
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
    private final Set<TaskSet<?>> mCurruntTasks = new HashSet<TaskSet<?>>();
    /* package */ Delivery mDelivery;

    private final Object mLock = new Object();

    public TaskQueue() {
        mThreadPool = ThreadPoolTool.newCacheThreadPool();
        mDelivery = new ExecutorDelivery(this);
    }

    public void add(TaskSet task, Object tag) {
        synchronized (mCurruntTasks) {
            task.setTag(tag);
            mCurruntTasks.add(task);
        }
        TaskRunner runner = new TaskRunner(task, mDelivery);
        task.control(mThreadPool.submit(runner));
    }

    public void cancelAll(Object tag) {
        synchronized (mCurruntTasks) {
            for (TaskSet<?> task : mCurruntTasks) {
                if (tag == task.getTag()) {
                    task.cancel();
                }
            }
        }
    }

    /* package */ void finish(TaskSet taskSet) {
        synchronized (mCurruntTasks) {
            mCurruntTasks.remove(taskSet);
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
