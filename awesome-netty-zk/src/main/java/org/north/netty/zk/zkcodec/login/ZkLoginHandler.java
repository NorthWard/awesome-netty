package org.north.netty.zk.zkcodec.login;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.north.netty.zk.bean.login.ZkLoginResp;
import org.north.netty.zk.registrys.ZkRegistry;
import org.north.netty.zk.zkcodec.ZkCodec;

import java.util.List;

/**
 * @author laihaohua
 */
public class ZkLoginHandler extends MessageToMessageDecoder<ZkLoginResp>{
    private Gson gson = new Gson();
    public static final String LOGIN_LENGTH_FIELD_BASED_FRAME_DECODER = "LOGIN_LENGTH_FIELD_BASED_FRAME_DECODER";
    public static final String LOGIN_LENGTH_FIELD_PREPENDER = "LOGIN_LENGTH_FIELD_PREPENDER";
    public static final String ZK_LOGIN_CODEC = "ZK_LOGIN_CODEC";
    public static final String ZK_LOGIN_HANDLER = "ZK_LOGIN_HANDLER";

    private volatile boolean isLogon;
    private volatile ZkLoginResp zkLoginResp;
    private ZkRegistry codecRegistry;
    public ZkLoginHandler(ZkRegistry codecRegistry){
        this.codecRegistry = codecRegistry;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ZkLoginResp zkLoginResp, List<Object> out) throws Exception {
        System.out.println( "msg = [" + gson.toJson(zkLoginResp) + "]");
        // remove两个login专用的handler
        ctx.pipeline().remove(ZK_LOGIN_CODEC);
        ctx.pipeline().remove(ZK_LOGIN_HANDLER);
        // 新增通用的ZkCodec
        ctx.pipeline().addLast(new ZkCodec(codecRegistry));
        // 返回响应体
        setZkLoginResp(zkLoginResp);
        // 返回登录成功的标记
        setIsLogon(true);

    }

    public boolean getIsLogon() {
        return isLogon;
    }

    public void setIsLogon(boolean isLogon) {
        this.isLogon = isLogon;
    }

    public ZkLoginResp getZkLoginResp() {
        return zkLoginResp;
    }

    public void setZkLoginResp(ZkLoginResp zkLoginResp) {
        this.zkLoginResp = zkLoginResp;
    }
}
