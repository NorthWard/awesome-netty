package org.north.netty.zk.utils;

public class OpCode {
    public static final int notification = 0;

    public static final int create = 1;

    public static final int delete = 2;

    public static final int exists = 3;

    public static final int getData = 4;

    public static final int setData = 5;

    public static final int getACL = 6;

    public static final int setACL = 7;

    public static final int getChildren = 8;

    public static final int sync = 9;

    public static final int ping = 11;

    public static final int getChildren2 = 12;

    public static final int check = 13;

    public static final int multi = 14;

    public static final int auth = 100;

    public static final int setWatches = 101;

    public static final int sasl = 102;

    public static final int createSession = -10;

    public static final int closeSession = -11;

    public static final int error = -1;
}
