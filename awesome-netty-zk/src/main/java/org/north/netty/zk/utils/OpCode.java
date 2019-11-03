package org.north.netty.zk.utils;

public class OpCode {
    public static final int NOTIFICATION = 0;

    public static final int CREATE = 1;

    public static final int DELETE = 2;

    public static final int EXISTS = 3;

    public static final int GET_DATA = 4;

    public static final int SET_DATA = 5;

    public static final int GETACL = 6;

    public static final int SETACL = 7;

    public static final int GET_CHILDREN = 8;

    public static final int SYNC = 9;

    public static final int PING = 11;

    public static final int GET_CHILDREN2 = 12;

    public static final int CHECK = 13;

    public static final int MULTI = 14;

    public static final int AUTH = 100;

    public static final int SET_WATCHES = 101;

    public static final int SASL = 102;

    public static final int CREATE_SESSION = -10;

    public static final int CLOSE_SESSION = -11;

    public static final int ERROR = -1;
}
