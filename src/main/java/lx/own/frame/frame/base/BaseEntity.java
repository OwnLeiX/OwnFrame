package lx.own.frame.frame.base;

import org.json.JSONObject;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 01/07/2017
 */

public interface BaseEntity {
    void initSelf(JSONObject jsonObject);

    boolean assertSelfNonNull();
}
