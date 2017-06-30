package lx.own.frame.tools.network.config;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public enum RequestMethod {
    Get("GET"), Post("POST"), Put("PUT"), Delete("DELETE");

    private String value;

    private RequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
