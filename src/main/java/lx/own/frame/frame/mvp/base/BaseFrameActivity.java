package lx.own.frame.frame.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import lx.own.frame.frame.base.BaseActivity;
import lx.own.frame.frame.mvp.BaseModel;
import lx.own.frame.frame.mvp.BasePresenter;
import lx.own.frame.frame.mvp.BaseView;
import lx.own.frame.tools.utils.TUtil;


public abstract class BaseFrameActivity<P extends BasePresenter, M extends BaseModel> extends BaseActivity implements BaseView {

    protected P mPresenter;
    protected M mModel;

    @Override
    final protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        mPresenter.setVM(this, mModel);
        super.onCreate(savedInstanceState);
        if (mPresenter != null)
            mPresenter.onIViewCreated();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null)
            mPresenter.onIViewStarted();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.onIViewResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.onIViewPaused();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null)
            mPresenter.onIViewStopped();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.onIViewDestroyed();
        super.onDestroy();
    }

    @Override
    public void onTimeConsumingTaskStarted() {

    }

    @Override
    public void onTimeConsumingTaskFinished() {

    }
}
