package lx.own.frame.frame.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import lx.own.frame.tools.utils.ThemeUtil;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String INTENT_KEY_THEME_CHANGED = "theme_is_changed";

    private static final int FLAG_DOUBLE_CLICK_QUIT = 1;//双击返回键finish
    private static final int FLAG_IMMERSED_STATUS_BAR = 1 << 2;//沉浸式状态栏

    private long mExitTime;//用于双击退出的时间记录
    private View mContentView;//内容根布局
    private int mFlags = 0;//flags

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        onInitFuture();
        if ((mFlags & FLAG_IMMERSED_STATUS_BAR) > 0)
            supportStatusBar();
        mContentView = LayoutInflater.from(this).inflate(onProvideContentViewId(), (ViewGroup) getWindow().getDecorView(), false);
        if ((mFlags & FLAG_IMMERSED_STATUS_BAR) > 0)
            supportContentView(mContentView);
        super.onCreate(savedInstanceState);
        if (mContentView != null) {
            setContentView(mContentView);
            onInitView(mContentView);
            onInitListener();
            if (savedInstanceState != null)
                onRestoreState(savedInstanceState);
            onInitData(savedInstanceState != null);
        }
    }

    /**
     * {@link #setContentView(int)}{@link #setContentView(View)}或者{@link #addContentView(View, ViewGroup.LayoutParams)}方法执行完毕时就会调用该方法
     */
    @Override
    final public void onContentChanged() {
    }

    /**
     * onPostCreate方法是指onCreate方法彻底执行完毕的回调，onPostResume类似，
     * 这两个方法官方说法是一般不会重写，
     * 现在知道的做法也就只有在使用ActionBarDrawerToggle的使用在onPostCreate需要在屏幕旋转时候等同步下状态。
     */
    @Override
    final protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    final protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null)
            onSaveState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if ((mFlags & FLAG_DOUBLE_CLICK_QUIT) > 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showShortToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 提供根布局layoutId
     * -子类必须【复写】
     */
    @LayoutRes
    protected abstract int onProvideContentViewId();

    /**
     * 你可以在这里初始化自己的特性
     * 这个方法在onCreate()内部的setTheme()之后会被回调
     * -提供给子类【复写】
     */
    protected void onInitFuture() {
    }

    /**
     * 如果需要，在这里保存数据
     * -提供给子类【复写】
     *
     * @param outState 用以存储数据的载体
     */
    protected void onSaveState(Bundle outState) {
    }

    /**
     * 如果你有在{@link #onSaveState(Bundle)}里保存数据
     * 可以在这里恢复自己的数据
     * -提供给子类【复写】
     *
     * @param savedInstanceState 恢复的数据载体
     */
    protected void onRestoreState(Bundle savedInstanceState) {
    }

    /**
     * 在{@link #setContentView(int)}执行完毕后被回调
     * -提供给子类【复写】
     */
    protected void onInitView(View root) {
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
     */
    protected void onInitData(boolean hasRestoreState) {
    }

    /**
     * 获取自己设置的根布局的方法
     * -提供给子类【调用】
     *
     * @return {@link #setContentView(View)}中设置的View
     */
    final protected View getContentView() {
        return mContentView;
    }

    /**
     * 自动转换类型的{@link #findViewById(int)}方法
     * -提供给子类【调用】
     *
     * @param resId id
     */
    @SuppressWarnings(value = "unchecked")
    final protected <T> T bindView(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 如果需要，调用此方法适配沉浸式布局
     * 配合{@link #setImmersedStatus(boolean)}一起使用
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
                title.setPadding(title.getPaddingLeft(), title.getPaddingTop() + getStatusBarHeight(this), title.getPaddingRight(), title.getPaddingBottom());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            title.setFitsSystemWindows(true);
            if (title instanceof ViewGroup) {
                ((ViewGroup) title).setClipToPadding(true);
            } else {
                title.setPadding(title.getPaddingLeft(), title.getPaddingTop() + getStatusBarHeight(this), title.getPaddingRight(), title.getPaddingBottom());
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
     * 配合{@link #setImmersedStatus(boolean)}一起使用
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
     * 设置是否需要双击退出 缺省值为false
     * {@link #FLAG_DOUBLE_CLICK_QUIT}
     * -提供给子类【调用】
     *
     * @param doubleClickExit true 双击back退出此Activity | false 单击back退出
     */
    final protected void setDoubleClickExit(boolean doubleClickExit) {
        if (doubleClickExit) {
            mFlags |= FLAG_DOUBLE_CLICK_QUIT;
        } else {
            mFlags &= ~FLAG_DOUBLE_CLICK_QUIT;
        }
    }

    /**
     * 设置是否需要沉浸式状态栏样式的跟布局
     * {@link #FLAG_IMMERSED_STATUS_BAR}
     * 如果使用了{@link lx.own.frame.R.style#SupportTheme} 则此方法无效
     * -提供给子类【调用】
     *
     * @param immersed true 沉浸式 | false 非沉浸式
     */
    final protected void setImmersedStatus(boolean immersed) {
        if (immersed) {
            mFlags |= FLAG_IMMERSED_STATUS_BAR;
        } else {
            mFlags &= ~FLAG_IMMERSED_STATUS_BAR;
        }
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
        Intent intent = new Intent(this, toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivity(intent);
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
        Intent intent = new Intent(this, toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * showToast的方法
     * -提供给子类【调用】
     *
     * @param message 消息
     */
    final protected void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * showToast的方法
     * -提供给子类【调用】
     *
     * @param resId 文本资源id
     */
    final protected void showShortToast(@StringRes int resId) {
        showShortToast(getString(resId));
    }

    /**
     * 切换主题的方法
     * -提供给子类【调用】
     *
     * @param theme 定义的主题
     */
    final protected void changeTheme(String theme) {
        if (theme.equals(ThemeUtil.getTheme()))
            return;
        ThemeUtil.setTheme(theme);
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(INTENT_KEY_THEME_CHANGED, true);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    /**
     * 初始化主题的方法
     * 必须结合{@link ThemeUtil}使用
     */
    private void initTheme() {
        String theme = ThemeUtil.getTheme();
        setTheme(theme);
    }

    /**
     * 设置主题的方法，在{@link #onCreate(Bundle)}中会被调用
     * 切换主题后Activity肯定会重启，如果是改变主题的重启，{@link #getIntent()}中能取到key为
     * {@link #INTENT_KEY_THEME_CHANGED}的flag
     *
     * @param theme 约定的定义主题的字符串
     */
    private void setTheme(String theme) {
        switch (theme) {
            default:
                break;
        }
        if (getIntent().getBooleanExtra(INTENT_KEY_THEME_CHANGED, false))
            showShortToast("切换主题成功");
    }

    /**
     * 为本页面适配沉浸式布局的方法
     */
    private void supportStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//为什么会无效？
        }
    }

    /**
     * 为内容根布局适配沉浸式的方法
     *
     * @param contentView 本页面的内容根布局
     */
    private void supportContentView(View contentView) {
        if (contentView == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            contentView.setFitsSystemWindows(false);
            if (contentView instanceof ViewGroup) {
                ((ViewGroup) contentView).setClipToPadding(false);
            } else {
                contentView.setPadding(contentView.getPaddingLeft(), contentView.getPaddingTop() + getStatusBarHeight(this), contentView.getPaddingRight(), contentView.getBottom());
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentView.setFitsSystemWindows(false);
            if (contentView instanceof ViewGroup) {
                ((ViewGroup) contentView).setClipToPadding(false);
            } else {
                contentView.setPadding(contentView.getPaddingLeft(), contentView.getPaddingTop() + getStatusBarHeight(this), contentView.getPaddingRight(), contentView.getBottom());
            }
        } else {
            contentView.setFitsSystemWindows(true);
            if (contentView instanceof ViewGroup)
                ((ViewGroup) contentView).setClipToPadding(true);
        }
    }

    /**
     * 获取系统顶部状态栏的高度
     *
     * @param context 上下文，此处仅使用 this。
     */
    private int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 隐藏系统底部导航栏的方法
     */
    private void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_IMMERSIVE; // hide status bar
        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**
     * 获取当前主题的View背景color
     *
     * @return ARGB
     */
    @ColorInt
    private int getThemeViewBgColor() {
        switch (ThemeUtil.getTheme()) {
            default:
                return 0xFFFF0000;
        }
    }
}
