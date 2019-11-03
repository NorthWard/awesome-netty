package org.north.netty.zk.bean.create;

import java.io.Serializable;

public class ZkAclId implements Serializable {
    private String scheme;
    private String id;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
