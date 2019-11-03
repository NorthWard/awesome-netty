package org.north.netty.zk.bean;

public abstract  class AbstractZkResonse {
    int xid;
    long zxid;
    int err;

    public void setXid(int xid) {
        this.xid = xid;
    }

    public void setZxid(long zxid) {
        this.zxid = zxid;
    }

    public void setErr(int err) {
        this.err = err;
    }
}
