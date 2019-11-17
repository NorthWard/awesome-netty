package org.north.netty.zk.zkcodec;

import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.registrys.ZkCaches;

public abstract class ZkAbstractCodec<T> extends ByteToMessageCodec<T> {
    protected ZkCaches codecRegistry;
    public ZkAbstractCodec(ZkCaches codecRegistry){
            this.codecRegistry = codecRegistry;
    }
}
