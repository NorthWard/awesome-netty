package com.north.netty.redis.cmd.impl.getcmd.str;

import com.north.netty.redis.cmd.impl.getcmd.AbstractGetCmd;
import com.north.netty.redis.utils.CmdBuildUtils;

public class GetStringCmd extends AbstractGetCmd<String> {
    public GetStringCmd(String key) {
        super(key);
    }

    @Override
    public String build() {
        return CmdBuildUtils.buildString(getCmd(), paramList);
    }
}
