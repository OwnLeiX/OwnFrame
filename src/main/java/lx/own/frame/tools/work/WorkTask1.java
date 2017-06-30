package lx.own.frame.tools.work;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import lx.own.frame.tools.work.kernel.BaseWorkTask;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class WorkTask1<D> extends BaseWorkTask<D> {

    @Override
    final protected void start() {
        onStared();
    }

    @Override
    final protected void run(D data){
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

    @MainThread
    protected void onStared() {
    }

    @WorkerThread
    protected abstract void work(D data);

    @MainThread
    protected abstract void onSucceed();

    @MainThread
    protected abstract void onFailed(Throwable e);

    @MainThread
    protected void onFinished() {
    }

    final public void execute(D data) {
        work(data);
    }
}
