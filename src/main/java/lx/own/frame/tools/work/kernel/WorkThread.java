package lx.own.frame.tools.work.kernel;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 02/07/2017
 */

public class WorkThread implements Runnable {
    private LinkedBlockingQueue<BaseWorkTask> mTaskQueue;
    private BaseWorkTask mCurrentTask;

    public WorkThread(LinkedBlockingQueue<BaseWorkTask> queue) {
        mTaskQueue = queue;
    }

    public WorkThread(LinkedBlockingQueue<BaseWorkTask> queue, BaseWorkTask task) {
        this.mTaskQueue = queue;
        this.mCurrentTask = task;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
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
