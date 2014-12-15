package com.kescoode.android.yomo;

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

    public static void add(TaskSet task,Object tag) {
        QUEUE.add(task,tag);
    }
}
