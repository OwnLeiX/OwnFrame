package lx.own.frame.tools.network.kernel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import lx.own.frame.tools.network.config.RequestHeader;
import lx.own.frame.tools.network.config.RequestMethod;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public class NetworkEngine {
    private Charset charset;
    private int mConnectTimeout = 5000;
    private int mReadTimeout = 5000;


    public static void init() {

    }

    public static void release() {
    }


    <R> R execute(BaseRequest<R> request) {
        try {
            String finalUrl = request.url;
            if (request.method == RequestMethod.Get)
                finalUrl = request.url + "?" + request.params.buildStr();
            URL url = new URL(finalUrl);
            URLConnection connection = url.openConnection();
            configureHeaders(request.headers, connection);
            connection.setConnectTimeout(mConnectTimeout);
            connection.setReadTimeout(mReadTimeout);
            //Fixme
        } catch (MalformedURLException e) {
            //Fixme url格式非法 回调失败
        } catch (IOException e) {
            //Fixme openConnection()失败 回调
        }
        return null;
    }

    void enqueue(BaseRequest request) {

    }

    private void configureHeaders(RequestHeader headers, URLConnection connection) {
        if (headers == null || connection == null)
            return;
        Set<String> keys = headers.keySet();
        Iterator<String> keysIterator = keys.iterator();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            String value = headers.get(key);
            connection.setRequestProperty(key, value);
        }
    }
}
