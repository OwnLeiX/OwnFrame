package lx.own.frame.tools.network.kernel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import lx.own.frame.tools.network.config.RequestHeader;
import lx.own.frame.tools.network.config.RequestMethod;
import lx.own.frame.tools.work.kernel.WorkEngine;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public class NetworkEngine {

    private static NetworkEngine mInstance;
    private static final int DEFAULT_TIMEOUT = 5000;
    private static final Charset DEFAULT_Charset = Charset.defaultCharset();

    public static void init() {
        initCustom(DEFAULT_Charset, DEFAULT_TIMEOUT, DEFAULT_TIMEOUT);
    }

    public static void initCustom(Charset charset, int connectTimeout, int readTimeout) {
        if (mInstance == null) {
            synchronized (WorkEngine.class) {
                if (mInstance == null)
                    mInstance = new NetworkEngine(charset, connectTimeout, readTimeout);
            }
        }
    }

    static NetworkEngine $() {
        checkInit();
        return mInstance;
    }

    private static void checkInit() {
        if (mInstance == null)
            throw new IllegalStateException("you didn't call WorkEngine.init() or you have called WorkEngine.release() before !");
    }

    public static void release() {
        if (mInstance != null) {
            synchronized (WorkEngine.class) {
                if (mInstance != null) {
                    mInstance.destroy();
                    mInstance = null;
                }
            }
        }
    }

    private Charset charset;
    private int mConnectTimeout = 5000;
    private int mReadTimeout = 5000;

    public NetworkEngine(Charset charset, int connectTimeout, int readTimeout) {
        this.charset = charset;
        this.mConnectTimeout = connectTimeout;
        this.mReadTimeout = readTimeout;
    }

    <R> R execute(BaseRequest<R> request) throws Exception {
        R response = null;
        try {
            String finalUrl = request.url;
            final RequestMethod method = request.method;
            if (method == RequestMethod.Get)
                finalUrl = request.url + "?" + request.params.buildStr();
            URL url = new URL(finalUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            configureHeaders(request.headers, connection);
            connection.setConnectTimeout(mConnectTimeout);
            connection.setReadTimeout(mReadTimeout);
            connection.setRequestMethod(method.getValue());
            switch (method) {
                case Get:
                    break;
                case Post:
                    byte[] params = request.params.buildBytes(charset);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", String.valueOf(params.length));
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(params);
                    break;
            }
            final int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                response = request.handleResponse(connection.getInputStream(), charset);
        } catch (MalformedURLException e) {
            //Fixme url格式非法 回调失败
        } catch (IOException e) {
            //Fixme openConnection()失败 回调
            //Fixme request.handleResponse()方法部分异常 回调失败
        } catch (Exception e) {
            //Fixme request.handleResponse()方法部分异常 回调失败
        }
        return response;
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

    private void destroy() {

    }
}
