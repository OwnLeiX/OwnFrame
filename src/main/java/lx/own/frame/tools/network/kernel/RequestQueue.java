package lx.own.frame.tools.network.kernel;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p> </p><br/>
 *
 * @author Lx
 * @date 30/06/2017
 */

public class RequestQueue {
    LinkedBlockingQueue<BaseRequest> mCacheQueue;
    LinkedBlockingQueue<BaseRequest> mNetworkQueue;
}
