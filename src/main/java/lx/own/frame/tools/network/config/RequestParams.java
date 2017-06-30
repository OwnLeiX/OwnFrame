package lx.own.frame.tools.network.config;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import lx.own.frame.tools.network.kernel.BaseParams;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public class RequestParams extends BaseParams{
    private HashMap<String, String> mParamMap;

    public RequestParams() {
        mParamMap = new HashMap<>();
    }

    public RequestParams add(String key, String value) {
        mParamMap.put(key, value);
        return this;
    }

    @Override
    final protected byte[] buildBytes(Charset charset) {
        String string = buildString();
        return string.getBytes(charset);
    }

    @Override
    final protected String buildString() {
        String content = "";
        Set<String> keys = mParamMap.keySet();
        if (keys != null) {
            StringBuilder contentBuilder = new StringBuilder();
            Iterator<String> keysIterator = keys.iterator();
            if (keysIterator.hasNext()) {
                String key = keysIterator.next();
                String value = mParamMap.get(key);
                contentBuilder.append(key);
                contentBuilder.append("=");
                contentBuilder.append(value);
                while (keysIterator.hasNext()) {
                    key = keysIterator.next();
                    value = mParamMap.get(key);
                    contentBuilder.append("&");
                    contentBuilder.append(key);
                    contentBuilder.append("=");
                    contentBuilder.append(value);
                }
            }
            content = contentBuilder.toString();
        }
        return content;
    }
}
