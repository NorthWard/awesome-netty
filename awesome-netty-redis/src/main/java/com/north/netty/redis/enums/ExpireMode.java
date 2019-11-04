package com.north.netty.redis.enums;

public enum ExpireMode {
    /**
     * 过期时间是毫秒
     */
    PX("PX"),
    /**
     * 过期时间是秒
     */
    EX("EX");
    private String type;
    ExpireMode(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
