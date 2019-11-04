package com.north.netty.redis.cmd.impl.getcmd.binary;

import com.north.netty.redis.cmd.impl.getcmd.AbstractGetCmd;
import com.north.netty.redis.utils.CmdBuildUtils;

public class GetBinaryCmd extends AbstractGetCmd<byte[]> {

    public GetBinaryCmd(byte[] key) {
        super(key);
    }

    @Override
    public byte[] build() {
        return CmdBuildUtils.buildBinary(getCmd(), paramList);
    }
}
