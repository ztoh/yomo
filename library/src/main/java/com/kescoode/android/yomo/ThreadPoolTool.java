package com.kescoode.android.yomo;

import android.os.Build;
import android.os.Process;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池工具类
 *
 * @author Kesco Lin
 */
/* package */ class ThreadPoolTool {

    private static final String LOG_TAG = "ThreadPoolTool";
    private static final int CPU_CORE = Runtime.getRuntime().availableProcessors();

    private ThreadPoolTool() {
    }

    public static ThreadPoolExecutor newCacheThreadPool() {
        ThreadPoolExecutor executor =  new ThreadPoolExecutor(CPU_CORE,availableCoreNumbers(),
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new FailedExecuteHandler());

        allowCoreThreadTimeout(executor, true);
        return executor;
    }

    public static ThreadPoolExecutor newFixThreadPool(int threadSize) {
        if (0 >= threadSize) {
            throw new RuntimeException("Pool Size is small than zero!");
        }

        return new ThreadPoolExecutor(threadSize, threadSize,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new FailedExecuteHandler());
    }

    public static ThreadPoolExecutor newSingleThreadPool() {
        return newFixThreadPool(1);
    }

    /**
     * 获取线程池最佳线程数，目前以8为标准
     *
     * @return 线程数
     */
    private static int availableCoreNumbers() {
        int cores = CPU_CORE * 2;
        return cores < 8 ? 8 : cores;
    }

    private static void allowCoreThreadTimeout(ThreadPoolExecutor executor, boolean value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            executor.allowCoreThreadTimeOut(value);
        }
    }

    private static final class FailedExecuteHandler implements RejectedExecutionHandler {
        private FailedExecuteHandler() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Log.w(LOG_TAG, "Background Task: " + r + "has not done!");
        }
    }

}
