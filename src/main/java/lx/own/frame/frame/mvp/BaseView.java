package lx.own.frame.frame.mvp;

public interface BaseView {
    void onRequestStart();
    void onRequestError(String msg);
    void onRequestEnd();
    void onInternetError();
}
