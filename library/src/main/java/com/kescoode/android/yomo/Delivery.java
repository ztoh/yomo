package com.kescoode.android.yomo;

import java.util.concurrent.RejectedExecutionException;

/**
 * 后台执行消息处理
 *
 * @author Kesco Lin
 */
/* package */ interface Delivery {

    void postRemove(TaskSet set);

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
