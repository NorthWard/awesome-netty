package org.north.netty.zk.zkcodec;

import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.registrys.ZkRegistry;

public abstract class ZkAbstractCodec<T> extends ByteToMessageCodec<T> {
    protected ZkRegistry codecRegistry;
    public ZkAbstractCodec(ZkRegistry codecRegistry){
            this.codecRegistry = codecRegistry;
    }
}
