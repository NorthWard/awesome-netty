package org.north.netty.zk.utils;

public enum  CreateMode {
    /**
     * 永久性节点
     */
    PERSISTENT (0),
    /**
     * 永久性顺序节点
     */
    PERSISTENT_SEQUENTIAL (2),
    /**
     * 临时节点
     */
    EPHEMERAL (1),
    /**
     *  临时顺序节点
     */
    EPHEMERAL_SEQUENTIAL (3);
    private int flag;
    CreateMode(int flag){
       this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
