package org.north.netty.zk.utils;

import io.netty.buffer.ByteBuf;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SerializeUtils {
    public static byte[] toByteArray(Object obj){
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(byteArrayOS);
            stream.writeObject(obj);
            stream.close();
            return byteArrayOS.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object byteArrayToObj(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public static void writeStringToBuffer(String msg, ByteBuf out){
        if (msg == null) {
            out.writeInt(-1);
            return;
        }
        byte [] bytes = msg.getBytes(StandardCharsets.UTF_8);
        // 字符串的长度
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
    public static String readStringToBuffer( ByteBuf in){
        int strLen = in.readInt();
        if(strLen < 0){
            return null;
        }
        byte [] bytes = new byte[strLen];
        in.readBytes(bytes);
        String s = new String(bytes, StandardCharsets.UTF_8);
        return s;
    }

    public static void writeByteArrToBuffer(byte [] buffer, ByteBuf out){
        if (buffer == null) {
            out.writeInt(-1);
            return;
        }
        // 字符串的长度
        out.writeInt(buffer.length);
        out.writeBytes(buffer);
    }

}
