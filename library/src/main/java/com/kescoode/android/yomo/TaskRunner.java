package com.kescoode.android.yomo;

import android.os.Process;

import com.kescoode.android.yomo.toolbox.TaskSet;

import java.util.concurrent.RejectedExecutionException;

/**
 * 执行{@link com.kescoode.android.yomo.toolbox.TaskSet}的{@link java.lang.Runnable}
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
        try {
            Object result = mTask.run();
            mDelivery.postDone(mTask, result);
        } catch (Exception e) {
            YomoError error = new YomoError("Task failed", new Throwable[]{e});
            mDelivery.postFail(mTask, error);
        }

        if (null != mTask.next()) {
            try {
                mDelivery.postNext(mTask);
            } catch (RejectedExecutionException e) {
                YomoError error = new YomoError("Next task cannot execuete", new Throwable[]{e});
                mDelivery.postFail(mTask, error);
            }
        }
    }
}
