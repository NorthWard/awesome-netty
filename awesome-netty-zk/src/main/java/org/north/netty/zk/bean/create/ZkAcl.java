package org.north.netty.zk.bean.create;

import java.io.Serializable;

public class ZkAcl implements Serializable {
    private int perms;
    private ZkAclId id;

    public int getPerms() {
        return perms;
    }

    public void setPerms(int perms) {
        this.perms = perms;
    }

    public ZkAclId getId() {
        return id;
    }

    public void setId(ZkAclId id) {
        this.id = id;
    }
}
