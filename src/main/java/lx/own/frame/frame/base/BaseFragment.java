package lx.own.frame.frame.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mContentView == null) {
            this.mContainer = container;
            mContentView = requestContentView(inflater, container, savedInstanceState);
            onInitView(mContentView);
            onInitData();
            onInitFuture();
            onInitListener();
        }
        return mContentView;
    }

    protected abstract View requestContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected void onInitView(View contentView) {
    }

    protected void onInitFuture() {
    }

    protected void onInitListener() {
    }

    protected void onInitData() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);
    }

    /**
     * 设置自己关心的MenuItemId
     */
    final protected void setMenuItemId(int id) {
        this.mMenuItemId = id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == mMenuItemId) {
            onMenuClicked(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    protected void onMenuClicked(MenuItem item) {
    }


    final protected void openActivity(Class<? extends BaseActivity> toActivity, Bundle parameter) {
        Intent intent = new Intent(getActivity(), toActivity);
        if (parameter != null) {
            intent.putExtras(parameter);
        }
        startActivity(intent);

    }

    final protected void showShortToast(String message) {
        FragmentActivity dependentActivity = getActivity();
        if (dependentActivity != null) {
            Toast.makeText(dependentActivity,message,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
