package lx.own.frame.tools.network.kernel;

import java.nio.charset.Charset;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public abstract class BaseParams {
    byte[] buildByt(Charset charset) {
        return buildBytes(charset);
    }

    String buildStr() {
        return buildString();
    }

    protected abstract byte[] buildBytes(Charset charset);

    protected abstract String buildString();
}
