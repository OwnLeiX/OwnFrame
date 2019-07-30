package lx.own.frame.frame.mvp.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import lx.own.frame.frame.base.BaseFragment;
import lx.own.frame.frame.mvp.BaseModel;
import lx.own.frame.frame.mvp.BasePresenter;
import lx.own.frame.frame.mvp.BaseView;
import lx.own.frame.tools.utils.TUtil;

public abstract class BaseFrameFragment<P extends BasePresenter, M extends BaseModel> extends BaseFragment implements BaseView {

    protected P mPresenter;
    protected M mModel;

    @Override
    final public void onAttach(Context context) {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        mPresenter.setVM(this, mModel);
        super.onAttach(context);
    }

    @Override
    final public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter != null)
            mPresenter.onIViewCreated();
    }

    @Override
    public void onStart() {
        if (mPresenter != null)
            mPresenter.onIViewStarted();
        super.onStart();
    }

    @Override
    public void onResume() {
        if (mPresenter != null)
            mPresenter.onIViewResumed();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mPresenter != null)
            mPresenter.onIViewPaused();
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mPresenter != null)
            mPresenter.onIViewStopped();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null)
            mPresenter.onIViewDestroyed();
        super.onDestroyView();
    }

    @Override
    public void onTimeConsumingTaskStarted() {

    }

    @Override
    public void onTimeConsumingTaskFinished() {

    }
}
