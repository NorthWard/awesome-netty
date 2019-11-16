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
package com.north.netty.kafka.enums;

/**
 * Identifiers for all the Kafka APIs
 */
public enum ApiKeys {
    /**
     * 发送消息
     */
    PRODUCE(0, "Produce", (short) 5),

    /**
     * fetch 消息
     */
    FETCH(1, "Fetch", (short)6),
    /**
     * 拉取元数据
     */
    METADATA(3, "Metadata", (short) 1);

    public final short id;

    public final String name;

    public short apiVersion;

    ApiKeys(int id, String name, short apiVersion) {
        this.id = (short) id;
        this.name = name;
        this.apiVersion = apiVersion;
    }


}
