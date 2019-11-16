/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.north.netty.kafka.utils;

import io.netty.buffer.ByteBuf;

/**
 * 可变长度变量的转化工具
 *
 * @author kafka
 */
public final class VarLengthUtils {

    public static final int NULL_VARINT_SIZE_BYTES = VarLengthUtils.sizeOfVarint(-1);

    private VarLengthUtils() {}


    /**
     * Read an integer stored in variable-length format using zig-zag decoding from
     * ByteBuf
     *
     * @param buffer The buffer to read from
     * @return The integer read
     *
     * @throws IllegalArgumentException if variable-length value does not terminate after 5 bytes have been read
     */
    public static int readVarint(ByteBuf buffer) {
        int value = 0;
        int i = 0;
        int b;
        while (((b = buffer.readByte()) & 0x80) != 0) {
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 28) {
                throw new IllegalArgumentException("illegal value : " + value);
            }
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }


    /**
     * Read a long stored in variable-length format using zig-zag decoding from
     * ByteBuf
     *
     * @param buffer The buffer to read from
     * @return The long value read
     *
     * @throws IllegalArgumentException if variable-length value does not terminate after 10 bytes have been read
     */
    public static long readVarlong(ByteBuf buffer)  {
        long value = 0L;
        int i = 0;
        long b;
        while (((b = buffer.readByte()) & 0x80) != 0) {
            value |= (b & 0x7f) << i;
            i += 7;
            if (i > 63) {
                throw new IllegalArgumentException("illegal value : " + value);
            }
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }


    public static void writeVarint(int value, ByteBuf out){
        int v = (value << 1) ^ (value >> 31);
        while ((v & 0xffffff80) != 0L) {
            out.writeByte((v & 0x7f) | 0x80);
            v >>>= 7;
        }
        out.writeByte((byte) v);
    }



    public static void writeVarlong(long value, ByteBuf out){
        long v = (value << 1) ^ (value >> 63);
        while ((v & 0xffffffffffffff80L) != 0L) {
            out.writeByte(((int) v & 0x7f) | 0x80);
            v >>>= 7;
        }
        out.writeByte((byte) v);
    }



    /**
     * Number of bytes needed to encode an integer in variable-length format.
     *
     * @param value The signed value
     */
    public static int sizeOfVarint(int value) {
        int v = (value << 1) ^ (value >> 31);
        int bytes = 1;
        while ((v & 0xffffff80) != 0L) {
            bytes += 1;
            v >>>= 7;
        }
        return bytes;
    }

    /**
     * Get the length for UTF8-encoding a string without encoding it first
     *
     * @param s The string to calculate the length for
     * @return The length when serialized
     */
    public static int utf8Length(CharSequence s) {
        int count = 0;
        for (int i = 0, len = s.length(); i < len; i++) {
            char ch = s.charAt(i);
            if (ch <= 0x7F) {
                count++;
            } else if (ch <= 0x7FF) {
                count += 2;
            } else if (Character.isHighSurrogate(ch)) {
                count += 4;
                ++i;
            } else {
                count += 3;
            }
        }
        return count;
    }

    /**
     * Number of bytes needed to encode a long in variable-length format.
     *
     * @param value The signed value
     */
    public static int sizeOfVarlong(long value) {
        long v = (value << 1) ^ (value >> 63);
        int bytes = 1;
        while ((v & 0xffffffffffffff80L) != 0L) {
            bytes += 1;
            v >>>= 7;
        }
        return bytes;
    }

}
