package lx.own.frame.frame.mvp;

public abstract class BasePresenter<M, V> {
    protected M mModel;
    protected V mView;

    final public void setVM(V view, M model) {
        this.mView = view;
        this.mModel = model;
    }

    public void onIViewCreated(){}

    public void onIViewStarted(){}

    public void onIViewResumed(){}

    public void onIViewPaused(){}

    public void onIViewStopped(){}

    public void onIViewDestroyed() {
    }
}
