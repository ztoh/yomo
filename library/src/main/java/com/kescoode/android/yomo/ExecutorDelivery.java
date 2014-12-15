package com.kescoode.android.yomo;

import android.os.Handler;
import android.os.Looper;

import com.kescoode.android.yomo.toolbox.TaskSet;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/**
 * {@link com.kescoode.android.yomo.Delivery}的实现
 *
 * @author Kesco Lin
 */
public class ExecutorDelivery implements Delivery {

    private final Handler mUiHandler;
    private final Executor mPostExecutor;
    private TaskQueue mQueue;

    public ExecutorDelivery(TaskQueue queue) {
        mUiHandler = new Handler(Looper.getMainLooper());
        mPostExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                if (command instanceof DelayedRunner) {
                    postToUi((DelayedRunner)command);
                } else {
                    throw new RuntimeException("Command is not a delayedRunner");
                }
            }
        };
        mQueue = queue;
    }

    private void postToUi(DelayedRunner command) {
        long millisecond = command.getTask().delay();
        if (0L == millisecond) {
            mUiHandler.post(command);
        } else {
            mUiHandler.postDelayed(command, millisecond);
        }
    }

    @Override
    public <T> void postDone(TaskSet<T> set, final T args) {
        mPostExecutor.execute(new DelayedRunner(set) {
            @Override
            public void run() {
                getTask().done(args);
            }
        });
    }

    @Override
    public void postFail(TaskSet set, final YomoError error) {
        mPostExecutor.execute(new DelayedRunner(set) {
            @Override
            public void run() {
                getTask().fail(error);
            }
        });
    }

    @Override
    public void postNext(TaskSet set) throws RejectedExecutionException {
        TaskSet next = set.next();
        next.type(set.type());
        mQueue.add(next);
    }

    private abstract class DelayedRunner implements Runnable {
        private TaskSet mTask;

        public DelayedRunner(TaskSet taskSet) {
            mTask = taskSet;
        }

        public TaskSet getTask() {
            return mTask;
        }
    }
}
