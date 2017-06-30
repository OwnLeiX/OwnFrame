package lx.own.frame.tools.work;

import android.support.annotation.MainThread;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

abstract class BaseWorkTask<D> implements Runnable {

    public long id;
    private boolean isFinished = false;
    private D data;

    final public boolean enqueue(D data) {
        this.data = data;
        return ThreadPool.enqueue(this);
    }

    @Override
    final public void run() {
        isFinished = false;
        try {
            ThreadPool.postToUi(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            run(data);
            ThreadPool.postToUi(new Runnable() {
                @Override
                public void run() {
                    success();
                }
            });
        } catch (final Exception e) {
            ThreadPool.postToUi(new Runnable() {
                @Override
                public void run() {
                    fail(e);
                }
            });
        } finally {
            isFinished = true;
            ThreadPool.postToUi(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            ThreadPool.notifyTaskCompleted();
        }

    }

    abstract void run(D data) throws Exception;

    @MainThread
    abstract void start();

    @MainThread
    abstract void success();

    @MainThread
    abstract void fail(Throwable e);

    @MainThread
    abstract void finish();

    final public boolean isFinished() {
        return isFinished;
    }

    final public boolean cancel() {
        return ThreadPool.cancelWaitTask(this);
    }
}
