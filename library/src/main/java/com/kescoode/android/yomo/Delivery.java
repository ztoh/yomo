package com.kescoode.android.yomo;

import com.kescoode.android.yomo.toolbox.TaskSet;

import java.util.concurrent.RejectedExecutionException;

/**
 * 后台执行消息处理
 *
 * @author Kesco Lin
 */
public interface Delivery {

    /**
     * 成功之后的回调
     */
    <T> void postDone(TaskSet<T> set, T args);

    /**
     * 失败的回调
     */
    void postFail(TaskSet set, YomoError error);

    /**
     * 推送下一个任务
     */
    void postNext(TaskSet set) throws RejectedExecutionException;

}
