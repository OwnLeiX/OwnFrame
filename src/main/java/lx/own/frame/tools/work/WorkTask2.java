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
    final protected void handleData(D data) {
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

    @WorkerThread
    protected abstract R work(D data);

    protected void onStared() {
    }

    @MainThread
    protected void onSucceed(R result) {
    }

    @MainThread
    protected void onFailed(Throwable e) {
    }

    @MainThread
    protected void onFinished() {
    }

    final public R execute(D data) {
        return work(data);
    }

}
