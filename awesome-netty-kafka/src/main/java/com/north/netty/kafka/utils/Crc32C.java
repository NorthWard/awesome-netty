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

import java.util.zip.Checksum;

/**
 * A class that can be used to compute the CRC32C (Castagnoli) of a ByteBuffer or array of bytes.
 *
 * We use java.util.zip.CRC32C (introduced in Java 9) if it is available and fallback to PureJavaCrc32C, otherwise.
 * java.util.zip.CRC32C is significantly faster on reasonably modern CPUs as it uses the CRC32 instruction introduced
 * in SSE4.2.
 *
 * NOTE: This class is intended for INTERNAL usage only within Kafka.
 */
public final class Crc32C {

    private static final ChecksumFactory CHECKSUM_FACTORY = new PureJavaChecksumFactory();


    private Crc32C() {}

    /**
     * Compute the CRC32C (Castagnoli) of the segment of the byte array given by the specified size and offset
     *
     * @param bytes The bytes to checksum
     * @param offset the offset at which to begin the checksum computation
     * @param size the number of bytes to checksum
     * @return The CRC32C
     */
    public static long compute(byte[] bytes, int offset, int size) {
        Checksum crc = create();
        crc.update(bytes, offset, size);
        return crc.getValue();
    }


    public static Checksum create() {
        return CHECKSUM_FACTORY.create();
    }

    private interface ChecksumFactory {
        Checksum create();
    }

    private static class PureJavaChecksumFactory implements ChecksumFactory {
        @Override
        public Checksum create() {
            return new PureJavaCrc32C();
        }
    }
}
