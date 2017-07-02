package lx.own.frame.tools.work.kernel;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.LinkedBlockingQueue;

public class WorkEngine {
    private static final String TAG = "WorkEngine";
    private static WorkEngine mInstance;
    private static final int DEFAULT_SIZE = Runtime.getRuntime().availableProcessors();

    public static void init() {
        initCustom(DEFAULT_SIZE);
    }

    public static void initCustom(int maxSize) {
        if (mInstance == null) {
            synchronized (WorkEngine.class) {
                if (mInstance == null)
                    mInstance = new WorkEngine(maxSize);
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

    static WorkEngine $() {
        checkInit();
        return mInstance;
    }

    static void checkInit() {
        if (mInstance == null)
            throw new IllegalStateException("you didn't call WorkEngine.init() or you have called WorkEngine.release() before !");
    }

    private LinkedBlockingQueue<BaseWorkTask> mTaskQueue;// 等待队列
    private ThreadGroup mThreads;//work线程组
    private int mMaxSize;//最大线程数
    private Handler mCallbackHandler;

    private WorkEngine(int poolSize) {
        if (mInstance != null)
            throw new IllegalStateException("you should not call WorkEngine.init() WorkEngine.initCustom() twice without WorkEngine.release()!");
        mThreads = new ThreadGroup(TAG);
        mThreads.setDaemon(true);
        mTaskQueue = new LinkedBlockingQueue<>();// 等待队列
        mMaxSize = poolSize;
    }

    boolean enqueue(final BaseWorkTask task) {
        boolean isExecute = false;
        if (task != null) {
            if (mThreads.activeCount() < mMaxSize) {
                isExecute = true;
                if (mTaskQueue.isEmpty()) {
                    mTaskQueue.offer(task);
                } else {
                    buildThread(task);
                }
            } else {
                isExecute = mTaskQueue.isEmpty();
                if (!mTaskQueue.offer(task)) {
                    isExecute = true;
                    notifyToUiThread(new Runnable() {
                        @Override
                        public void run() {
                            task.fail(new IllegalStateException("WorkTask Queue is filled!"));
                            task.release();
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

    private boolean buildThread(BaseWorkTask task) {
        boolean returnValue = false;
        try {
            new WorkThread(mThreads, mTaskQueue, task).start();
            returnValue = true;
        } catch (Exception e) {
            returnValue = false;
        }
        return returnValue;
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
        mThreads.interrupt();
        mThreads = null;
        mTaskQueue.clear();
        mTaskQueue = null;
        if (mCallbackHandler != null)
            mCallbackHandler.removeCallbacksAndMessages(null);
        mCallbackHandler = null;
    }
}
