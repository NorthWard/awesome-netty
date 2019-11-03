package org.north.netty.zk.bean;

import java.io.Serializable;

public interface ZkRequest extends Serializable {
    int getRequestId();
}
