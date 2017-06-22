package lx.own.frame.tools.work;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import rx.Subscriber;
import rx.functions.Func1;

/**
 * <b></b>
 * Created on 2017/6/16.
 *
 * @author LeiXun
 */

public abstract class WorkTask<D, R> extends Subscriber<R> implements Func1<D, R> {
    // 使用id去标识任务，当存在排队等待的任务时可以依据id进行删除操作。
    public long id;
    private boolean isCompleted = false;

    @Override
    final public R call(D data) {
        isCompleted = false;
        R returnValue = null;
        // 耗时工作
        returnValue = work(data);
        isCompleted = true;
        // 完成任务后通知主线程，线程池有一个空余线程,并试图从等待队列中获取下载任务
        ThreadPool.notifyTaskCompleted();
        return returnValue;
    }

    @Override
    final public void onNext(R result) {
        if (result != null)
            onSuccess(result);
        else
            onError(new IllegalStateException("任务在work中返回了null"));
    }

    @Override
    public void onStart() {
    }

    @WorkerThread
    protected abstract R work(D d);

    @MainThread
    protected void onSuccess(@NonNull R r) {
    }

    @Override
    @MainThread
    public void onError(Throwable e) {
    }

    @MainThread
    @Override
    public void onCompleted() {
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    final public boolean enqueue(D data){
        return ThreadPool.enqueue(this,data);
    }
}
