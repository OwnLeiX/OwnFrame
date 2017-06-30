package lx.own.frame.tools.network.kernel;

import java.io.InputStream;
import java.nio.charset.Charset;

import lx.own.frame.tools.network.config.CacheMode;
import lx.own.frame.tools.network.config.RequestError;
import lx.own.frame.tools.network.config.RequestHeader;
import lx.own.frame.tools.network.config.RequestMethod;
import lx.own.frame.tools.network.config.RequestParams;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class BaseRequest<R> {
    protected RequestMethod method;
    protected CacheMode cacheMode;
    protected RequestParams params;
    protected RequestHeader headers;
    protected BaseCallback<R> callback;
    protected String url;

    protected <Q extends BaseRequest<R>, C extends BaseCallback<R>> BaseRequest(BaseBuilder<Q, C, R> builder) {
        this.method = builder.method;
        this.cacheMode = builder.cacheMode;
        this.params = builder.params;
        this.headers = builder.headers;
        this.callback = builder.callback;
        this.url = builder.url;
    }

    protected abstract R handleResponse(InputStream is, Charset charset) throws Exception;

    final void start() {
        callback.onStarted();
    }

    final void success(R response) {
        callback.onSucceed(response);
    }

    final void fail(RequestError error) {
        callback.onFailed(error);
    }

    final void finish() {
        callback.onFinished();
    }

    public void enqueue() {
    }

    public static abstract class BaseBuilder<Q extends BaseRequest<R>, C extends BaseCallback<R>, R> {
        private RequestMethod method = RequestMethod.Get;
        private CacheMode cacheMode = CacheMode.None;
        private RequestParams params;
        private RequestHeader headers;
        private C callback;
        private String url;

        protected BaseBuilder() {
        }

        public BaseBuilder<Q, C, R> setMethod(RequestMethod method) {
            this.method = method;
            return this;
        }

        public BaseBuilder<Q, C, R> setCacheMode(CacheMode cacheMode) {
            this.cacheMode = cacheMode;
            return this;
        }

        public BaseBuilder<Q, C, R> setParams(RequestParams params) {
            this.params = params;
            return this;
        }

        public BaseBuilder<Q, C, R> setHeaders(RequestHeader headers) {
            this.headers = headers;
            return this;
        }

        public BaseBuilder<Q, C, R> setUrl(String url) {
            this.url = url;
            return this;
        }

        public BaseBuilder<Q, C, R> setCallback(C callback) {
            this.callback = callback;
            return this;
        }

        public abstract Q build();
    }

    public static abstract class BaseCallback<R> {
        public abstract void onStarted();

        public abstract void onSucceed(R response);

        public abstract void onFailed(RequestError error);

        public abstract void onFinished();
    }
}
