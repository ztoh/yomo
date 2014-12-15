package com.kescoode.android.yomo;

import android.os.Process;

import java.util.concurrent.RejectedExecutionException;

/**
 * 执行{@link TaskSet}的{@link java.lang.Runnable}
 *
 * @author Kesco Lin
 */
/* package */ class TaskRunner implements Runnable {

    private Delivery mDelivery;

    private TaskSet mTask;

    public TaskRunner(TaskSet task, Delivery delivery) {
        mTask = task;
        mDelivery = delivery;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        mDelivery.postRemove(mTask);

        if (mTask.isCanceled()) {
            return;
        }
        try {
            Object result = mTask.run();
            mDelivery.postDone(mTask, result);
        } catch (Exception e) {
            YomoError error = new YomoError("Task failed", new Throwable[]{e});
            mDelivery.postFail(mTask, error);
        }

        if (null != mTask.next() && !mTask.isCanceled()) {
            try {
                mDelivery.postNext(mTask);
            } catch (RejectedExecutionException e) {
                YomoError error = new YomoError("Next task cannot execuete", new Throwable[]{e});
                mDelivery.postFail(mTask, error);
            }
        }
    }
}
