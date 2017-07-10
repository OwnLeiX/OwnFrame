package lx.own.frame.tools.network.kernel;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import lx.own.frame.tools.network.config.Schedulers;
import lx.own.frame.tools.network.config.RequestError;
import lx.own.frame.tools.network.config.RequestHeader;
import lx.own.frame.tools.network.config.RequestMethod;
import lx.own.frame.tools.network.exception.AssertNonNullException;
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
    private Handler mCallbackHandler;

    public NetworkEngine(Charset charset, int connectTimeout, int readTimeout) {
        this.charset = charset;
        this.mConnectTimeout = connectTimeout;
        this.mReadTimeout = readTimeout;
        this.mCallbackHandler = new Handler(Looper.getMainLooper());
    }

    <R> R execute(BaseRequest<R> request) throws Exception {
        R response = null;
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
        return response;
    }

    <R> void enqueue(BaseRequest<R> request) {
        if (request == null)
            return;
        String finalUrl = request.url;
        final BaseRequest.BaseCallback<R> callback = request.callback;
        if (callback == null)
            return;
        final RequestMethod method = request.method;
        final Schedulers scheduler = request.schedulers;
        try {
            postToCallbackThread(buildStartedCallback(callback), scheduler);
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
            if (responseCode == 200) {
                final R response = request.handleResponse(connection.getInputStream(), charset);
                postToCallbackThread(buildSucceedCallback(callback, response), scheduler);
            }
            //Fixme EntityRequest IOException, IllegalAccessException, InstantiationException, JSONException, AssertNonNullException
            //Fixme BytesRequest IOException
        } catch (InstantiationException e) {

        } catch (MalformedURLException e) {

        } catch (IllegalAccessException e) {

        } catch (IOException e) {

        } catch (JSONException e) {

        } catch (AssertNonNullException e) {

        } catch (Exception e) {

        } finally {
            postToCallbackThread(buildFinishedCallback(callback), scheduler);
        }
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

    private <R> Runnable buildSucceedCallback(final BaseRequest.BaseCallback<R> callback, final R response) {
        return new Runnable() {
            @Override
            public void run() {
                callback.onSucceed(response);
            }
        };
    }

    private Runnable buildFailedCallback(final BaseRequest.BaseCallback callback, final RequestError error) {
        return new Runnable() {
            @Override
            public void run() {
                callback.onFailed(error);
            }
        };
    }

    private Runnable buildStartedCallback(final BaseRequest.BaseCallback callback) {
        return new Runnable() {
            @Override
            public void run() {
                callback.onStarted();
            }
        };
    }

    private Runnable buildFinishedCallback(final BaseRequest.BaseCallback callback) {
        return new Runnable() {
            @Override
            public void run() {
                callback.onFinished();
            }
        };
    }

    private void postToCallbackThread(Runnable r, Schedulers thread) {
        if (thread == Schedulers.MainThread) {
            mCallbackHandler.post(r);
        } else if (thread == Schedulers.BackgroundThread) {

        } else if (thread == Schedulers.CurrentThread) {

        }
    }

    private void destroy() {

    }
}
