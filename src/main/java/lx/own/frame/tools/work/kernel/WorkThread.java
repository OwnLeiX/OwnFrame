package lx.own.frame.tools.work.kernel;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 02/07/2017
 */

final public class WorkThread extends Thread {
    private static final String TAG = "WorkThread";
    private LinkedBlockingQueue<BaseWorkTask> mTaskQueue;
    private BaseWorkTask mCurrentTask;

    public WorkThread(ThreadGroup group, LinkedBlockingQueue<BaseWorkTask> queue) {
        this(group, queue, null);
    }

    public WorkThread(ThreadGroup group, LinkedBlockingQueue<BaseWorkTask> queue, BaseWorkTask task) {
        super(group, TAG);
        this.mTaskQueue = queue;
        this.mCurrentTask = task;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            if (mCurrentTask == null) {
                try {
                    mCurrentTask = mTaskQueue.take();
                } catch (InterruptedException e) {
                    continue;
                }
            }
            mCurrentTask.run();
            mCurrentTask = null;
        }
    }
}
