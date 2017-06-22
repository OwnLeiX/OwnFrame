package lx.own.frame.tools.work;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 线程池+等待队列管理工具
 */
public class ThreadPool {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();// 线程池大小
    private volatile static int count = 0;// 工作线程数
    private static LinkedList<QueueTask> TASK_QUEUE = new LinkedList<>();// 等待队列
    private static Handler mHandler;

    public static void init(){

    }

    /**
     * 执行任务
     *
     * @param workTask
     * @return 是否执行，否：放入等待队列
     */
    static <D, R> boolean enqueue(final WorkTask<D, R> workTask, D data) {
        boolean isExecute = false;
        if (workTask != null) {
            if (count < POOL_SIZE) {
                count++;
                isExecute = true;
                Observable.just(data)
                        .subscribeOn(Schedulers.io())
                        .map(workTask)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(workTask);
            } else {
                // 排队
                QueueTask queueTask = new QueueTask();
                queueTask.task = workTask;
                queueTask.data = data;
                TASK_QUEUE.addLast(queueTask);
            }
        }
        return isExecute;
    }

    /**
     * 删除等待任务
     *
     * @param id
     * @return
     */
    public static boolean cancelWaitTask(long id) {
        boolean isCancel = false;
        QueueTask target = null;
        for (QueueTask item : TASK_QUEUE) {
            if (item.task.id == id) {
                target = item;
                break;
            }
        }
        if (target != null) {
            TASK_QUEUE.remove(target);
            isCancel = true;
        }
        return isCancel;
    }

    public static void destroy() {
        TASK_QUEUE.clear();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private static Handler getHandler() {
        if (mHandler == null) {
            synchronized (ThreadPool.class) {
                if (mHandler == null)
                    mHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mHandler;
    }

    public static void notifyTaskCompleted(){
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                count--;
                QueueTask first = TASK_QUEUE.pollFirst();
                if (first != null) {
                    enqueue(first.task, first.data);
                }
            }
        });
    }

    static class QueueTask {
        Object data;
        WorkTask task;
    }
}
