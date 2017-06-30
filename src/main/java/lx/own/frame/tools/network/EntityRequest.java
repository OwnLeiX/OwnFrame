package lx.own.frame.tools.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;

import lx.own.frame.frame.base.BaseEntity;
import lx.own.frame.tools.network.exception.AssertNonNullException;
import lx.own.frame.tools.network.io.StreamReader;
import lx.own.frame.tools.network.kernel.BaseRequest;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public class EntityRequest<R extends BaseEntity> extends BaseRequest<R> {
    private EntityRequest(Builder builder) {
        super(builder);
    }

    @SuppressWarnings(value = {"unchecked"})
    @Override
    protected R handleResponse(InputStream is, Charset charset) throws IOException, IllegalAccessException, InstantiationException, JSONException, AssertNonNullException {
        String jsonString = StreamReader.readString(is, charset);
        R response = ((Class<R>) ((ParameterizedType) (callback.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0])
                .newInstance();
        response.initSelf(new JSONObject(jsonString));
        if (!response.assertSelfNonNull())
            throw new AssertNonNullException();
        return response;
    }


    public static class Builder<R extends BaseEntity> extends BaseRequest.BaseBuilder<EntityRequest<R>, EntityRequestCallback<R>, R> {

        public Builder() {
            super();
        }

        @Override
        final public EntityRequest build() {
            return new EntityRequest(this);
        }

    }

    public static abstract class EntityRequestCallback<R extends BaseEntity> extends BaseCallback<R> {
    }
}
