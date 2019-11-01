package org.north.netty.zk.bean;

import java.io.Serializable;

public class RequestHeader implements Serializable {
    private int xid;
    private int type;

    public int getXid() {
        return xid;
    }

    public void setXid(int xid) {
        this.xid = xid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
