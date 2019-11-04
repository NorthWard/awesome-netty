package com.north.netty.redis.utils;

import com.north.netty.redis.utils.EncodeUtils;
import com.north.netty.redis.utils.SymbolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.List;

/**
 * @author laihaohua
 */
public class CmdBuildUtils {
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
     public static byte[] buildBinary(String cmd, List<byte []> paramList) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        /*****************先写 *arrLen\r\n  ***********************/
        byteBuf.writeBytes(SymbolUtils.ARRAY_STAR_BYTE);
        // 整个命令数组长度 1(cmd) + paramList.size()
        int cmdLen = 1 + (paramList == null ? 0 : paramList.size());
        byteBuf.writeBytes(EncodeUtils.getBytes(cmdLen));
        byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
        /*****************写 *arrLen\r\n 结束 **********************/

        /*************** "$cmdLen\r\n" + "cmd\r\n"  开始 *******************/
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
        for(byte [] bs : paramList){
            byteBuf.writeBytes(SymbolUtils.BULK_DOLLAR_BYTE);
            byteBuf.writeBytes(EncodeUtils.getBytes(bs.length));
            byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
            byteBuf.writeBytes(bs);
            byteBuf.writeBytes(SymbolUtils.CRLF_BYTE);
        }
        /*************** $paramNLen\r\n" + "paramN\r\n"   结束 *******************/
        bytes = new byte[byteBuf.writerIndex()];
        byteBuf.readBytes(bytes);
        byteBuf.release();
        return bytes;
    }

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
    public static  String buildString(String cmd, List<String> paramList) {
        StringBuilder s = new StringBuilder();
        /*****************先写 *arrLen\r\n  ***********************/
        s.append(SymbolUtils.ARRAY_STAR);
        // 整个命令数组长度 1(cmd) + paramList.size()
        int cmdLen = 1 + (paramList == null ? 0 : paramList.size());
        s.append(cmdLen);
        s.append(SymbolUtils.CRLF);
        /*****************写 *arrLen\r\n 结束 **********************/

        /*************** "$cmdLen\r\n" + "cmd\r\n"  开始 *******************/
        byte [] bytes = cmd.getBytes();
        // $美元符号
        s.append(SymbolUtils.BULK_DOLLAR);
        //  cmd的长度
        s.append(bytes.length);
        // CRLF
        s.append(SymbolUtils.CRLF);
        // cmd的内容
        s.append(cmd);
        s.append(SymbolUtils.CRLF);
        /******************* "$cmdLen\r\n" + "cmd\r\n"  结束 **************************/

        /*************** $paramNLen\r\n" + "paramN\r\n"  开始 *******************/
        for(String o : paramList){
            bytes = o.toString().getBytes();
            s.append(SymbolUtils.BULK_DOLLAR);
            s.append(bytes.length);
            s.append(SymbolUtils.CRLF);
            s.append(o);
            s.append(SymbolUtils.CRLF);
        }
        /*************** $paramNLen\r\n" + "paramN\r\n"   结束 *******************/
        return s.toString();
    }
}
