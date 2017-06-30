package lx.own.frame.tools.network.config;

import java.util.HashMap;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 01/07/2017
 */

public class RequestHeader {
    private HashMap<String, String> mHeaderMap;

    public RequestHeader() {
        mHeaderMap = new HashMap<>();
    }

    public RequestHeader add(String key, String value) {
        mHeaderMap.put(key, value);
        return this;
    }
}
