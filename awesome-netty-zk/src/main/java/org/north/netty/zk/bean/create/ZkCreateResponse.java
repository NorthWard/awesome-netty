package org.north.netty.zk.bean.create;

import org.north.netty.zk.bean.AbstractZkResonse;
import org.north.netty.zk.bean.ZkResponse;

public class ZkCreateResponse extends AbstractZkResonse implements ZkResponse {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
