package lx.own.frame.frame.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import lx.own.frame.frame.base.BaseFragment;
import lx.own.frame.frame.mvp.BaseModel;
import lx.own.frame.frame.mvp.BasePresenter;
import lx.own.frame.frame.mvp.BaseView;
import lx.own.frame.tools.utils.TUtil;

public abstract class BaseFrameFragment<P extends BasePresenter, M extends BaseModel> extends BaseFragment implements BaseView {

    protected P mPresenter;
    protected M mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = TUtil.getT(this,0);
        mModel = TUtil.getT(this,1);
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void onRequestError(String msg) {
        showShortToast(msg);
    }

    @Override
    public void onInternetError() {
        showShortToast("InternetError");
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.onIViewDestroyed();
        super.onDestroy();
    }
}
