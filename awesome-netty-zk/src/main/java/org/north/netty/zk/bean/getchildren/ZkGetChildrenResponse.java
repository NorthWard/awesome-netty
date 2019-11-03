package org.north.netty.zk.bean.getchildren;

import org.north.netty.zk.bean.AbstractZkResonse;
import org.north.netty.zk.bean.ZkResponse;

import java.util.List;

/**
 * @author laihaohua
 */
public class ZkGetChildrenResponse extends AbstractZkResonse implements ZkResponse {
    private java.util.List<String> children;

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
