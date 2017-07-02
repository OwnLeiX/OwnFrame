package lx.own.frame.tools.network.config;

import java.util.HashMap;
import java.util.Set;

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
        if (key != null && value != null)
            mHeaderMap.put(key, value);
        return this;
    }

    public String get(String key) {
        return mHeaderMap.get(key);
    }

    public Set<String> keySet() {
        return mHeaderMap.keySet();
    }
}
