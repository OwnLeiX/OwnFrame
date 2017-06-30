package lx.own.frame.tools.work;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import lx.own.frame.tools.work.kernel.BaseWorkTask;

/**
 * <b></b>
 * Created on 2017/6/16.
 *
 * @author LeiXun
 */

public abstract class WorkTask2<D, R> extends BaseWorkTask<D> {

    private R result;

    @Override
    final protected void start() {
        onStared();
    }

    @Override
    final protected void run(D data) {
        result = work(data);
    }

    @Override
    final protected void success() {
        onSucceed(result);
    }

    @Override
    final protected void fail(Throwable e) {
        onFailed(e);
    }

    @Override
    final protected void finish() {
        onFinished();
    }

    protected void onStared() {
    }

    @WorkerThread
    protected abstract R work(D data);

    @MainThread
    protected abstract void onSucceed(R result);

    @MainThread
    protected abstract void onFailed(Throwable e);

    @MainThread
    protected void onFinished() {
    }

    final public R execute(D data) {
        return work(data);
    }

}
