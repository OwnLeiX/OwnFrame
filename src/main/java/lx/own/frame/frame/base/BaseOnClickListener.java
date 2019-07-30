package lx.own.frame.frame.base;

import android.view.View;

/**
 * <b></b>
 * Created on 2017/6/19.
 *
 * @author LeiXun
 */

public class BaseOnClickListener implements View.OnClickListener{
    private long mClickInterval = 500;
    private long mPreClickTime;
    private View mPreClickView;

    public BaseOnClickListener() {
    }

    public BaseOnClickListener(long interval) {
        this.mClickInterval = interval;
    }

    @Override
    final public void onClick(View v) {
        final long timeMillis = System.currentTimeMillis();
        if (timeMillis - mPreClickTime > mClickInterval) {
            this.onValidClick(v);
            mPreClickView = v;
            mPreClickTime = timeMillis;
        }else if (v == mPreClickView){
            this.onDoubleClick(v);
        }
    }

    protected void onDoubleClick(View v) {

    }

    protected void onValidClick(View v) {

    }
}
