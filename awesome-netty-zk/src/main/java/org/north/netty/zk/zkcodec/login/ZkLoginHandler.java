package org.north.netty.zk.zkcodec.login;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.north.netty.zk.bean.ZkLoginResp;

import java.util.List;

/**
 * @author laihaohua
 */
public class ZkLoginHandler extends MessageToMessageDecoder<ZkLoginResp> implements ZkLoginChannelHandler {
    private Gson gson = new Gson();
    @Override
    protected void decode(ChannelHandlerContext ctx, ZkLoginResp msg, List<Object> out) throws Exception {
        System.out.println( "msg = [" + gson.toJson(msg) + "]");
    }
}
