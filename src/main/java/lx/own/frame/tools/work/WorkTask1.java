package lx.own.frame.tools.work;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;

import lx.own.frame.tools.work.kernel.BaseWorkTask;

/**
 * <p> </p><br/>
 *
 * @author Lx on 30/06/2017
 */

public abstract class WorkTask1<D> extends BaseWorkTask<D> {

    @Override
    final protected void start() {
        onStarted();
    }

    @Override
    final protected void handleData(D data) throws Exception {
        work(data);
    }

    @Override
    final protected void success() {
        onSucceed();
    }

    @Override
    final protected void fail(Throwable e) {
        onFailed(e);
    }

    @Override
    final protected void finish() {
        onFinished();
    }

    @WorkerThread
    protected abstract void work(D data) throws Exception;

    @MainThread
    protected void onSucceed() {
    }

    @MainThread
    protected void onStarted() {
    }

    @MainThread
    protected void onFailed(Throwable e) {
    }

    @MainThread
    protected void onFinished() {
    }

    final public void execute(D data) throws Exception {
        work(data);
    }
}
