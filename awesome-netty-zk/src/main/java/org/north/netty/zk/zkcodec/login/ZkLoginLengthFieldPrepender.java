package org.north.netty.zk.zkcodec.login;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 利用LengthFieldPrepender 将ZkLoginRequest的ByteBuffer封装成 head-content形式的报文
 * @author laihaohua
 */
public class ZkLoginLengthFieldPrepender extends LengthFieldPrepender implements ZkLoginChannelHandler {
    public ZkLoginLengthFieldPrepender(int lengthFieldLength) {
        super(lengthFieldLength);
    }
}
