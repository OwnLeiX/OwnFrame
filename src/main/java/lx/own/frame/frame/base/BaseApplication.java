package lx.own.frame.frame.base;

import android.app.Application;
import android.content.Context;

import lx.own.frame.tools.network.kernel.NetworkEngine;
import lx.own.frame.tools.work.kernel.WorkEngine;

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
        WorkEngine.init();
        NetworkEngine.init();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        WorkEngine.release();
        NetworkEngine.release();
    }
}
