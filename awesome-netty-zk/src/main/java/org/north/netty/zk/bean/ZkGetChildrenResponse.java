package org.north.netty.zk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author laihaohua
 */
public class ZkGetChildrenResponse implements ZkResponse {
    private java.util.List<String> children;

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
