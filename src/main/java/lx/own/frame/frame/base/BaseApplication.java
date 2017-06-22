package lx.own.frame.frame.base;

import android.app.Application;
import android.content.Context;

import lx.own.frame.tools.work.ThreadPool;

/**
 * <b></b>
 * Created on 2017/6/22.
 *
 * @author LeiXun
 */

public class BaseApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ThreadPool.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ThreadPool.destroy();
    }
}
