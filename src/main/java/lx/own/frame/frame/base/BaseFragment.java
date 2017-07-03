package lx.own.frame.frame.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {


    protected ViewGroup mContainer;
    protected View mContentView;
    private int mMenuItemId;
    private int mFlags = 0;
    private static final int CARE_MENU_CLICKED = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onInitFuture();
        setHasOptionsMenu((mFlags & CARE_MENU_CLICKED) > 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            this.mContainer = container;
            mContentView = onProvideContentView(inflater, container);
        }
        onInitView(mContentView);
        onInitListener();
        if (savedInstanceState != null)
            onRestoreState(savedInstanceState);
        onInitData(savedInstanceState != null);
        return mContentView;
    }

    @Override
    final public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == mMenuItemId) {
            onMenuClicked(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    final public void onSaveInstanceState(Bundle outState) {
        onSaveState(outState);
    }

    /**
     * 你可以在这里初始化自己的特性
     * {@link #setCareMenuClicked(boolean)} {@link #addCaredMenuItemId(int)}等通常在此调用
     * -提供给子类【复写】
     */
    protected void onInitFuture() {
    }

    /**
     * 提供内容View
     * -子类必须【复写】
     *
     * @param inflater
     * @param container 父容器
     * @return 本Fragment的根布局
     */
    protected abstract View onProvideContentView(LayoutInflater inflater, @Nullable ViewGroup container);

    /**
     * 在{@link #onProvideContentView(LayoutInflater, ViewGroup)}执行完毕后被回调
     * -提供给子类【复写】
     *
     * @param contentView {@link #onProvideContentView(LayoutInflater, ViewGroup)}返回的View
     */
    protected void onInitView(View contentView) {
    }

    /**
     * 在{@link #onInitView(View)} 执行完毕后被回调
     * -提供给子类【复写】
     */
    protected void onInitListener() {
    }

    /**
     * 紧接在{@link #onInitListener()} 之后被回调
     * -提供给子类【复写】
     *
     * @param hasRestoreState 是否由恢复数据
     */
    protected void onInitData(boolean hasRestoreState) {
    }


    /**
     * 如果需要，在这里保存数据
     * -提供给子类【复写】
     */
    protected void onSaveState(Bundle outState) {
    }

    /**
     * 如果你有在{@link #onSaveState(Bundle)}里保存数据
     * 可以在这里恢复自己的数据
     * -提供给子类【复写】
     */
    protected void onRestoreState(@NonNull Bundle savedInstanceState) {
    }
    /**
     * 请求创建菜单
     * -提供给子类【复写】
     *
     * @param menu     菜单
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    /**
     * 自己关心的菜单被点击
     * -提供给子类【复写】
     *
     * @param item MenuItem
     */
    protected void onMenuClicked(MenuItem item) {
    }

    /**
     * 如果需要，调用此方法适配沉浸式布局
     * -提供给子类【调用】
     *
     * @param title 用于数据展示的，置顶的View
     */
    final protected void supportTitleView(View title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title.setFitsSystemWindows(true);
            if (title instanceof ViewGroup) {
                ((ViewGroup) title).setClipToPadding(true);
            } else {
                FragmentActivity dependentActivity = getActivity();
                title.setPadding(title.getPaddingLeft(), title.getPaddingTop() + getStatusBarHeight(dependentActivity), title.getPaddingRight(), title.getPaddingBottom());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            title.setFitsSystemWindows(true);
            if (title instanceof ViewGroup) {
                ((ViewGroup) title).setClipToPadding(true);
            } else {
                FragmentActivity dependentActivity = getActivity();
                title.setPadding(title.getPaddingLeft(), title.getPaddingTop() + getStatusBarHeight(dependentActivity), title.getPaddingRight(), title.getPaddingBottom());
            }
        } else {
            title.setFitsSystemWindows(true);
            if (title instanceof ViewGroup) {
                ((ViewGroup) title).setClipToPadding(true);
            }
        }
    }

    /**
     * <b>如果需要，调用此方法适配沉浸式布局</b></p>
     * -提供给子类【调用】
     *
     * @param viewGroup 在内容根布局内置顶，但是并非用于内容展示的ViewGroup
     */
    final protected void supportViewGroup(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewGroup.setFitsSystemWindows(true);
            viewGroup.setClipToPadding(true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            viewGroup.setFitsSystemWindows(true);
            viewGroup.setClipToPadding(true);
        } else {
            viewGroup.setFitsSystemWindows(true);
            viewGroup.setClipToPadding(true);
        }
    }

    /**
     * 是否关心菜单键按下
     * 在 {@link #onInitFuture()} 中设置
     * -提供给子类【调用】
     *
     * @param care true 关心 | false 不关心
     */
    final protected void setCareMenuClicked(boolean care) {
        if (care) {
            mFlags |= CARE_MENU_CLICKED;
        } else {
            mFlags &= ~CARE_MENU_CLICKED;
        }
    }

    /**
     * 设置自己关心的MenuItemId
     * 尽早设置
     * -提供给子类【调用】
     *
     * @param id 关心的MenuItemId
     */
    final protected void addCaredMenuItemId(int id) {
        this.mMenuItemId = id;
    }

    /**
     * 设置自己关心的MenuItemId
     * 尽早设置
     * -提供给子类【调用】
     *
     * @param id 关心的MenuItemId
     */
    final protected void removeCaredMenuItemId(int id) {
        this.mMenuItemId = id;
    }


    /**
     * 开启Activity的方法
     * -提供给子类【调用】
     * {@link #openActivity(Class, Bundle)}
     *
     * @param toActivity 要开启的Activity的Class对象
     */
    final protected void openActivity(Class<? extends BaseActivity> toActivity) {
        openActivity(toActivity, null);
    }

    /**
     * 开启Activity的方法
     * -提供给子类【调用】
     *
     * @param toActivity 要开启的Activity的Class对象
     * @param parameter  需要传送的数据
     */
    final protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        FragmentActivity dependentActivity = getActivity();
        if (dependentActivity != null) {
            Intent intent = new Intent(dependentActivity, toActivity);
            if (parameter != null) {
                intent.putExtras(parameter);
            }
            startActivity(intent);
        }
    }

    /**
     * 开启Activity的方法
     * -提供给子类【调用】
     *
     * @param toActivity  要开启的Activity的Class对象
     * @param requestCode 请求码
     */
    final protected void openActivityForResult(Class<? extends BaseActivity> toActivity, int requestCode) {
        openActivityForResult(toActivity, requestCode, null);
    }

    /**
     * 开启Activity的方法
     * -提供给子类【调用】
     *
     * @param toActivity  要开启的Activity的Class对象
     * @param requestCode 请求码
     * @param parameter   需要传送的数据
     */
    final protected void openActivityForResult(Class<? extends BaseActivity> toActivity, int requestCode, Bundle parameter) {
        FragmentActivity dependentActivity = getActivity();
        if (dependentActivity != null) {
            Intent intent = new Intent(dependentActivity, toActivity);
            if (parameter != null) {
                intent.putExtras(parameter);
            }
            startActivityForResult(intent, requestCode);
        }
    }

    /**
     * showToast的方法
     * -提供给子类【调用】
     *
     * @param message 消息
     */
    final protected void showShortToast(String message) {
        FragmentActivity dependentActivity = getActivity();
        if (dependentActivity != null) {
            Toast.makeText(dependentActivity, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * showToast的方法
     * -提供给子类【调用】
     *
     * @param resId 文本资源Id
     */
    final protected void showShortToast(@StringRes int resId) {
        FragmentActivity dependentActivity = getActivity();
        if (dependentActivity != null) {
            Toast.makeText(dependentActivity, dependentActivity.getString(resId), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取系统顶部状态栏的高度
     */
    private int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        if (context != null) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = res.getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }
}
