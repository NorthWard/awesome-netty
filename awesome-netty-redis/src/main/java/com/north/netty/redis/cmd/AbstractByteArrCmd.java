package com.north.netty.redis.cmd;

import com.north.netty.redis.utils.EncodeUtils;
import com.north.netty.redis.utils.SymbolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public abstract class AbstractByteArrCmd extends AbstractCmd<byte []> {
    /**
     *  构建成的RESP文本是:
     *  "*arrLen\r\n"
     *  +"$cmdLen\r\n" + "cmd\r\n"
     *  +"$param0Len\r\n" + "param0\r\n"
     *  +"$param1Len\r\n" + "param1\r\n"
     *  +"$param2Len\r\n" + "param2\r\n"
     *   ......
     *   +"$paramnLen\r\n" + "paramn\r\n"
     * @return
     */
    @Override
    public byte[] build() {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        /*****************先写 *arrLen\r\n  ***********************/
        byteBuf.writeBytes(SymbolUtils.ARRAY_STAR_BYTE);
        // 整个命令数组长度 1(cmd) + 1(key) + paramList.size()
        int cmdLen = 1 + 1 + (super.paramList == null ? 0 : super.paramList.size());
        byteBuf.writeBytes(EncodeUtils.getBytes(cmdLen));
        byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
        /*****************写 *arrLen\r\n 结束 **********************/

        /*************** "$cmdLen\r\n" + "cmd\r\n"  开始 *******************/
        String cmd = getCmd();
        byte [] bytes = cmd.getBytes();
        // $美元符号
        byteBuf.writeBytes(SymbolUtils.BULK_DOLLAR_BYTE);
        //  cmd的长度
        byteBuf.writeBytes(EncodeUtils.getBytes(bytes.length));
        // CRLF
        byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
        // cmd的内容
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
        /******************* "$cmdLen\r\n" + "cmd\r\n"  结束 **************************/


        /*************** $paramNLen\r\n" + "paramN\r\n"  开始 *******************/
        for(Object o : paramList){
            bytes = EncodeUtils.getBytes(o);
            byteBuf.writeBytes(SymbolUtils.BULK_DOLLAR_BYTE);
            byteBuf.writeBytes(EncodeUtils.getBytes(bytes.length));
            byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
            byteBuf.writeBytes(bytes);
            byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
        }
        /*************** $paramNLen\r\n" + "paramN\r\n"   结束 *******************/
        bytes = new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(bytes);
        byteBuf.release();
        return bytes;
    }
}
