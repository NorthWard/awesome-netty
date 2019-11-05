package com.north.netty.redis.cmd.impl.getcmd.binary;

import com.north.netty.redis.cmd.CmdResp;
import com.north.netty.redis.cmd.impl.getcmd.AbstractGetCmd;
import com.north.netty.redis.utils.CmdBuildUtils;

import java.util.Arrays;

/**
 * @author laihaohua
 */
public class GetBinaryCmd extends AbstractGetCmd<byte[]>  implements CmdResp<byte[], byte[]> {

    public GetBinaryCmd(byte[] key) {
        super(key);
    }

    @Override
    public byte[] build() {
        return CmdBuildUtils.buildBinary(getCmd(), paramList);
    }

    @Override
    public byte[] parseResp(byte[] resp) {
        if(resp == null){
            return null;
        }
        int index = 1;
        int len  = (int)resp[index++] - 48;
        if(len <= 0){
            return null;
        }
        // 跳过 \r\n
        index += 2;
        return Arrays.copyOfRange(resp, index, index + len);
    }
}
