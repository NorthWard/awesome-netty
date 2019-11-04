package com.north.netty.redis.cmd;

import com.north.netty.redis.utils.SymbolUtils;


/**
 * @author laihaohua
 */
public abstract class AbstractStringCmd extends AbstractCmd<String> {
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
    public String build() {
        StringBuilder s = new StringBuilder();
        /*****************先写 *arrLen\r\n  ***********************/
        s.append(SymbolUtils.ARRAY_STAR);
        // 整个命令数组长度 1(cmd) + 1(key) + paramList.size()
        int cmdLen = 1 + 1 + (super.paramList == null ? 0 : super.paramList.size());
        s.append(cmdLen);
        s.append(SymbolUtils.CRLF);
        /*****************写 *arrLen\r\n 结束 **********************/

        /*************** "$cmdLen\r\n" + "cmd\r\n"  开始 *******************/
        String cmd = getCmd();
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
        for(Object o : paramList){
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
