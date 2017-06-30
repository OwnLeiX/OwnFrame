package lx.own.frame.tools.work.kernel;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class WorkEngine {

    private static WorkEngine mInstance;
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();// 线程池大小

    public static void init() {
        initCustom(POOL_SIZE);
    }

    public static void initCustom(int poolSize) {
        if (mInstance == null) {
            synchronized (WorkEngine.class) {
                if (mInstance == null)
                    mInstance = new WorkEngine(poolSize);
            }
        }
    }

    public static void release() {
        if (mInstance != null) {
            synchronized (WorkEngine.class) {
                if (mInstance != null) {
                    mInstance.destroy();
                    mInstance = null;
                }
            }
        }
    }

    static WorkEngine $(){
        checkInit();
        return mInstance;
    }

    static void checkInit() {
        if (mInstance == null)
            throw new IllegalStateException("you didn't call WorkEngine.init() or you have called WorkEngine.release() before !");
    }

    private volatile int mCurrentCount = 0;// 工作线程数
    private LinkedList<BaseWorkTask> mTaskQueue;// 等待队列
    private ThreadPoolExecutor mPool;// 线程池
    private int mPoolSize;
    private Handler mCallbackHandler;

    private WorkEngine(int poolSize) {
        if (mInstance != null)
            throw new IllegalStateException("you should not call WorkEngine.init() WorkEngine.initCustom() twice without WorkEngine.release()!");
        mPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
        mTaskQueue = new LinkedList<>();// 等待队列
        mPoolSize = poolSize;
    }

    <D> boolean enqueue(final BaseWorkTask<D> task) {
        boolean isExecute = false;
        if (task != null) {
            if (mCurrentCount < mPoolSize) {
                mCurrentCount++;
                isExecute = true;
                mPool.execute(task);
            } else {
                boolean succeed = mTaskQueue.offer(task);
                if (!succeed) {
                    isExecute = true;
                    notifyToUiThread(new Runnable() {
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

    void notifyToUiThread(Runnable r) {
        if (r != null)
            getHandler().post(r);
    }

    void notifyTaskCompleted() {
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                mCurrentCount--;
                BaseWorkTask first = mTaskQueue.pollFirst();
                if (first != null) {
                    enqueue(first);
                }
            }
        });
    }

    private Handler getHandler() {
        if (mCallbackHandler == null) {
            synchronized (this) {
                if (mCallbackHandler == null)
                    mCallbackHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mCallbackHandler;
    }

    private void destroy() {
        mTaskQueue.clear();
        mPool.shutdownNow();
        if (mCallbackHandler != null)
            mCallbackHandler.removeCallbacksAndMessages(null);
        mCallbackHandler = null;
        mTaskQueue = null;
        mPool = null;
    }
}
