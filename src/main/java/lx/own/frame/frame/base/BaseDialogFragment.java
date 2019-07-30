package lx.own.frame.frame.base;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;

/**
 * <b></b>
 * Created on 2017/6/19.
 *
 * @author LeiXun
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private final String KeyOfContentViewResId = "KeyOfContentViewResId";
    private final String KeyOfOuterTouchCloseable = "KeyOfOuterTouchCloseable";
    private final String KeyOfCustomGravity = "KeyOfCustomGravity";
    private int mContentViewResId = 0;
    private boolean isOuterTouchCloseable = false;//默认外部区域.点击不能关闭
    private int mCustomGravity;

    @Override
    public void setArguments(Bundle args) {
        Bundle innerArgs = (args == null ? new Bundle() : new Bundle(args));
        innerArgs.putInt(KeyOfContentViewResId, this.mContentViewResId);
        innerArgs.putBoolean(KeyOfOuterTouchCloseable, this.isOuterTouchCloseable);
        innerArgs.putInt(KeyOfCustomGravity, this.mCustomGravity);
        super.setArguments(innerArgs);
    }

    private int getContentViewResId(Bundle inputBundle) {
        int returnValue = 0;
        if (inputBundle != null) {
            returnValue = inputBundle.getInt(KeyOfContentViewResId);
        } else {
            Bundle innerBundle = this.getArguments();
            if (innerBundle != null)
                returnValue = innerBundle.getInt(KeyOfContentViewResId);
        }
        return returnValue;
    }

    private boolean getOuterTouchCloseable(Bundle inputBundle) {
        boolean returnValue = false;
        if (inputBundle != null) {
            returnValue = inputBundle.getBoolean(KeyOfOuterTouchCloseable);
        } else {
            Bundle innerBundle = this.getArguments();
            if (innerBundle != null)
                returnValue = innerBundle.getBoolean(KeyOfOuterTouchCloseable);
        }
        return returnValue;
    }

    private int getCustomGravity(Bundle inputBundle) {
        int returnValue = 0;
        if (inputBundle != null) {
            returnValue = inputBundle.getInt(KeyOfCustomGravity, Gravity.CENTER);
        } else {
            Bundle innerBundle = this.getArguments();
            if (innerBundle != null)
                returnValue = innerBundle.getInt(KeyOfCustomGravity, Gravity.CENTER);
        }
        return returnValue;
    }

    @Override
    final public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.isOuterTouchCloseable = this.getOuterTouchCloseable(savedInstanceState);
        this.mCustomGravity = this.getCustomGravity(savedInstanceState);
        View returnValue;
        int subViewResId = this.getContentViewResId(savedInstanceState);
        returnValue = inflater.inflate(subViewResId, container);
        this.onInitView(returnValue, savedInstanceState);
        return returnValue;
    }

    protected abstract void onInitView(View returnValue, Bundle savedInstanceState);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //以下逻辑.处理.实现全屏显示
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        //透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //强制全屏尺寸
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //设置自定义位置
        getDialog().getWindow().setGravity(getCustomGravity(savedInstanceState));

        getDialog().setCanceledOnTouchOutside(this.isOuterTouchCloseable);
    }


    /**
     * 设置布局资源id
     *
     * @param layoutResId 布局文件资源id
     */
    public synchronized BaseDialogFragment setContentViewResId(@LayoutRes int layoutResId) {
        this.mContentViewResId = layoutResId;
        this.setArguments(this.getArguments());
        return this;
    }

    /**
     * 点击面板.外部区域是否可以关闭界面
     *
     * @param isOuterTouchCloseable true：可以,false:不可以
     */
    public synchronized BaseDialogFragment setOuterTouchCloseable(boolean isOuterTouchCloseable) {
        this.isOuterTouchCloseable = isOuterTouchCloseable;
        this.setArguments(this.getArguments());
        return this;
    }

    /**
     * 设置自定义的对齐方式
     *
     * @param customGravity 对齐方式{@link Gravity#TOP} or {@link Gravity#CENTER} or {@link Gravity#BOTTOM}
     */
    public synchronized BaseDialogFragment setCustomGravity(int customGravity) {
        this.mCustomGravity = customGravity;
        this.setArguments(this.getArguments());
        return this;
    }
}
