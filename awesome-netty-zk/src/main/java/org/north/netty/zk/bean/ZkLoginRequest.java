package org.north.netty.zk.bean;

import java.io.Serializable;

/**
 * @author laihaohua
 */
public class ZkLoginRequest implements Serializable {
    private Integer protocolVersion;
    private Long lastZxidSeen;
    private int timeout;
    private Long sessionId;
    private String password;

    public Integer getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Integer protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Long getLastZxidSeen() {
        return lastZxidSeen;
    }

    public void setLastZxidSeen(Long lastZxidSeen) {
        this.lastZxidSeen = lastZxidSeen;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
