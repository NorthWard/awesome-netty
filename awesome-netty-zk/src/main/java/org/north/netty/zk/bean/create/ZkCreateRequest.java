package org.north.netty.zk.bean.create;

import org.north.netty.zk.bean.RequestHeader;
import org.north.netty.zk.bean.ZkRequest;

import java.util.List;

public class ZkCreateRequest  extends RequestHeader implements ZkRequest {
    private String path;
    private byte[] data;
    private java.util.List<ZkAcl> acl;
    private int flags;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public List<ZkAcl> getAcl() {
        return acl;
    }

    public void setAcl(List<ZkAcl> acl) {
        this.acl = acl;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public int getRequestId() {
        return getXid();
    }
}
