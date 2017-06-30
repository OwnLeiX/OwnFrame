package lx.own.frame.tools.work;

import android.support.annotation.WorkerThread;

import lx.own.frame.tools.work.kernel.BaseWorkTask;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class WorkTask0<D> extends BaseWorkTask<D> {

    @Override
    final protected void start() {

    }

    @Override
    final protected void run(D data){
        work(data);
    }

    @Override
    final protected void success() {

    }

    @Override
    final protected void fail(Throwable e) {

    }

    @Override
    final protected void finish() {

    }

    final public void execute(D data) {
        work(data);
    }

    @WorkerThread
    protected abstract void work(D data);
}
