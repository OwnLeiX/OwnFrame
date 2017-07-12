package lx.own.frame.tools.network.config;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public enum  RequestError {
    IllegalAccessError(0,"Entity类的构造方法是私有的"),
    InstantiationError(0,"Entity类没有无参构造方法"),
    UrlError(0,"Url协议、格式或路径错误"),
    IOError(1,"流读取错误"),
    JSONError(1,"服务器返回的数据格式不是JSON格式"),
    AssertNonNullError(0,"Entity类的checkNull不通过"),
    UnknownError(-1,"未知错误");
    private int errorCode;
    private String errorMessage;

    private RequestError(int cod, String msg) {
        this.errorCode = cod;
        this.errorMessage = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
