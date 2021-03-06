package org.north.netty.zk.factories;

import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.create.ZkCreateRequest;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenRequest;
import org.north.netty.zk.bean.ZkRequest;
import org.north.netty.zk.registrys.ZkCaches;
import org.north.netty.zk.zkcodec.createcodec.ZkCreateCodec;
import org.north.netty.zk.zkcodec.getchildren.ZkGetChildrenCodec;

/**
 * codecs 工厂
 * @author laihaohua
 */
public class ZkCodecFactories {
    public static ByteToMessageCodec getCodec(ZkRequest zkRequest, ZkCaches codecRegistry) throws IllegalAccessException {
       if(zkRequest instanceof  ZkGetChildrenRequest){
            return new ZkGetChildrenCodec(codecRegistry);
        }else if(zkRequest instanceof ZkCreateRequest){
            return new ZkCreateCodec(codecRegistry);
       }

        throw new IllegalAccessException("cannot find codecs for " + zkRequest.getClass());
    }
}
