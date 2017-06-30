package lx.own.frame.tools.work.kernel;

import android.support.annotation.MainThread;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class BaseWorkTask<D> implements Runnable {

    public long id;
    private boolean isFinished = false;
    private D data;
    private WorkEngine engine;

    final public boolean enqueue(D data) {
        engine = WorkEngine.$();
        this.data = data;
        return engine.enqueue(this);
    }

    @Override
    final public void run() {
        if (engine == null)
            throw new IllegalStateException("you should call method 'execute()' or 'enqueue()' instead of 'run()' !");
        isFinished = false;
        try {
            engine.notifyToUiThread(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            });
            run(data);
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
            engine.notifyTaskCompleted();
        }
    }

    protected abstract void run(D data) throws Exception;

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
