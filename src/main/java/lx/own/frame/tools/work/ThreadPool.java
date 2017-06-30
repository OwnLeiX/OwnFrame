package lx.own.frame.tools.work;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池+等待队列管理工具
 */
public class ThreadPool {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();// 线程池大小
    private volatile static int count = 0;// 工作线程数
    private static LinkedList<BaseWorkTask> TASK_QUEUE;// 等待队列
    private static ThreadPoolExecutor THREAD_POOL;// 线程池
    private static Handler mCallbackHandler;

    public static void init() {
        THREAD_POOL = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);
        TASK_QUEUE = new LinkedList<>();// 等待队列
    }

    public static void release() {
        TASK_QUEUE.clear();
        if (mCallbackHandler != null) {
            mCallbackHandler.removeCallbacksAndMessages(null);
            mCallbackHandler = null;
        }
    }


    static <D> boolean enqueue(final BaseWorkTask<D> task) {
        boolean isExecute = false;
        if (task != null) {
            if (count < POOL_SIZE) {
                count++;
                isExecute = true;
                THREAD_POOL.execute(task);
            } else {
                boolean succeed = TASK_QUEUE.offer(task);
                if (!succeed) {
                    isExecute = true;
                    postToUi(new Runnable() {
                        @Override
                        public void run() {
                            task.fail(new IllegalStateException("WorkTask Queue is filled!"));
                        }
                    });
                }
            }
        }
        return isExecute;
    }

    static void postToUi(Runnable r) {
        if (r != null)
            getHandler().post(r);
    }

    static boolean cancelWaitTask(BaseWorkTask task) {
        return TASK_QUEUE.remove(task);
    }

    static void notifyTaskCompleted() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                count--;
                BaseWorkTask first = TASK_QUEUE.pollFirst();
                if (first != null) {
                    enqueue(first);
                }
            }
        });
    }

    private static Handler getHandler() {
        if (mCallbackHandler == null) {
            synchronized (ThreadPool.class) {
                if (mCallbackHandler == null)
                    mCallbackHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mCallbackHandler;
    }
}
