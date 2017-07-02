package lx.own.frame.tools.work.kernel;

import android.support.annotation.MainThread;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class BaseWorkTask<D> {

    private boolean isFinished;
    private D data;
    private WorkEngine engine;

    final public boolean enqueue(D data) {
        engine = WorkEngine.$();
        this.data = data;
        return engine.enqueue(this);
    }


    final void run() {
        isFinished = false;
        try {
            engine.notifyToUiThread(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            handleData(data);
            engine.notifyToUiThread(new Runnable() {
                @Override
                public void run() {
                    success();
                }
            });
        } catch (final Exception e) {
            engine.notifyToUiThread(new Runnable() {
                @Override
                public void run() {
                    fail(e);
                }
            });
        } finally {
            isFinished = true;
            engine.notifyToUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            data = null;
            engine = null;
        }
    }

    final void release(){
        isFinished = true;
        data = null;
        engine = null;
    }

    protected abstract void handleData(D data) throws Exception;

    @MainThread
    protected abstract void start();

    @MainThread
    protected abstract void success();

    @MainThread
    protected abstract void fail(Throwable e);

    @MainThread
    protected abstract void finish();

    final public boolean isFinished() {
        return isFinished;
    }
}
