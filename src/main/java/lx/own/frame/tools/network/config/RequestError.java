package lx.own.frame.tools.network.config;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public enum  RequestError {
    ;
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
