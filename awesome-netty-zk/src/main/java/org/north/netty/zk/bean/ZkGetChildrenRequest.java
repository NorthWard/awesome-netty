package org.north.netty.zk.bean;

import java.io.Serializable;

/**
 * @author laihaohua
 */
public class ZkGetChildrenRequest  extends  RequestHeader implements ZkRequest {
    private String path;
    private boolean watch;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isWatch() {
        return watch;
    }

    public void setWatch(boolean watch) {
        this.watch = watch;
    }

    @Override
    public int getRequestId() {
        return super.getXid();
    }
}
