package org.north.netty.zk.factories;

import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.create.ZkCreateRequest;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenRequest;
import org.north.netty.zk.bean.ZkRequest;
import org.north.netty.zk.registrys.ZkRegistry;
import org.north.netty.zk.zkcodec.createcodec.ZkCreateCodec;
import org.north.netty.zk.zkcodec.getchildren.ZkGetChildrenCodec;

/**
 * codec 工厂
 * @author laihaohua
 */
public class ZkCodecFactories {
    public static ByteToMessageCodec getCodec(ZkRequest zkRequest, ZkRegistry codecRegistry) throws IllegalAccessException {
       if(zkRequest instanceof  ZkGetChildrenRequest){
            return new ZkGetChildrenCodec(codecRegistry);
        }else if(zkRequest instanceof ZkCreateRequest){
            return new ZkCreateCodec(codecRegistry);
       }

        throw new IllegalAccessException("cannot find codec for " + zkRequest.getClass());
    }
}
