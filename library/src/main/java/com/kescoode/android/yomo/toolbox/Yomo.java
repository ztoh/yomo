package com.kescoode.android.yomo.toolbox;

import com.kescoode.android.yomo.TaskQueue;

/**
 * Yomo异步库开关类
 *
 * @author Kesco Lin
 * @since 2014-12-03
 */
public class Yomo {

    private static final TaskQueue QUEUE = new TaskQueue();

    private Yomo() {
        /* Empty */
    }

    public static void concurrence(TaskSet task) {
        task.type(TaskSet.ExecutorType.Concurrence);
        QUEUE.add(task);
    }
}
