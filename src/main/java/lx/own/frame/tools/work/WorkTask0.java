package lx.own.frame.tools.work;

import android.support.annotation.WorkerThread;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class WorkTask0<D> extends BaseWorkTask<D> {

    @Override
    final void start() {

    }

    @Override
    final void run(D data){
        work(data);
    }

    @Override
    final void success() {

    }

    @Override
    final void fail(Throwable e) {

    }

    @Override
    final void finish() {

    }

    final public void execute(D data) {
        work(data);
    }

    @WorkerThread
    protected abstract void work(D data);
}
