package lx.own.frame.tools.work;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class WorkTask1<D> extends BaseWorkTask<D> {

    @Override
    final void start() {
        onStared();
    }

    @Override
    final void run(D data){
        work(data);
    }

    @Override
    final void success() {
        onSucceed();
    }

    @Override
    final void fail(Throwable e) {
        onFailed(e);
    }

    @Override
    final void finish() {
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
