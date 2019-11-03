package org.north.netty.zk.factories;

import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.ZkGetChildrenRequest;
import org.north.netty.zk.bean.ZkRequest;
import org.north.netty.zk.zkcodec.getchildren.ZkGetChildrenCodec;

/**
 * codec 工厂
 * @author laihaohua
 */
public class ZkCodecFactories {
    public static ByteToMessageCodec getCodec(Class<? extends ZkRequest> clazz) throws IllegalAccessException, InstantiationException {
       if(ZkGetChildrenRequest.class.isAssignableFrom(clazz)){
            return new ZkGetChildrenCodec();
        }

        throw new IllegalAccessException("cannot find codec for " + clazz);
    }
}
