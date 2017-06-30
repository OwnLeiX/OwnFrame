package lx.own.frame.tools.network;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import lx.own.frame.tools.network.io.StreamReader;
import lx.own.frame.tools.network.kernel.BaseRequest;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

final public class BytesRequest extends BaseRequest<byte[]> {

    private BytesRequest(Builder builder) {
        super(builder);
    }

    @Override
    protected byte[] handleResponse(InputStream is, Charset charset) throws IOException {
        return StreamReader.readBytes(is);
    }

    public static class Builder extends BaseBuilder<BytesRequest, ByteRequestCallback, byte[]> {

        public Builder() {
            super();
        }

        @Override
        final public BytesRequest build() {
            return new BytesRequest(this);
        }

    }

    public static abstract class ByteRequestCallback extends BaseCallback<byte[]> {
    }
}
