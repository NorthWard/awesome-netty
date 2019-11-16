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


import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all the client-server errors--those errors that must be sent from the server to the client. These
 * are thus part of the protocol. The names can be changed but the error code cannot.
 *
 * Note that client library will convert an unknown error code to the non-retriable Exception if the client library
 * version is old and does not recognize the newly-added error code. Therefore when a new server-side error is added,
 * we may need extra logic to convert the new error code to another existing error code before sending the response back to
 * the client if the request version suggests that the client may not recognize the new error code.
 *
 * Do not add exceptions that occur only on the client or only on the server here.
 */
public enum Errors {
    UNKNOWN_SERVER_ERROR(-1, "The server experienced an unexpected error when processing the request",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NONE(0, null,
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return null;
            }
        }),
    OFFSET_OUT_OF_RANGE(1, "The requested offset is not within the range of offsets maintained by the server.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    CORRUPT_MESSAGE(2, "This message has failed its CRC checksum, exceeds the valid size, or is otherwise corrupt.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    UNKNOWN_TOPIC_OR_PARTITION(3, "This server does not host this topic-partition.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_FETCH_SIZE(4, "The requested fetch size is invalid.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    LEADER_NOT_AVAILABLE(5, "There is no leader for this topic-partition as we are in the middle of a leadership election.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NOT_LEADER_FOR_PARTITION(6, "This server is not the leader for that topic-partition.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    REQUEST_TIMED_OUT(7, "The request timed out.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    BROKER_NOT_AVAILABLE(8, "The broker is not available.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    REPLICA_NOT_AVAILABLE(9, "The replica is not available for the requested topic-partition",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    MESSAGE_TOO_LARGE(10, "The request included a message larger than the max message size the server will accept.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    STALE_CONTROLLER_EPOCH(11, "The controller moved to another broker.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    OFFSET_METADATA_TOO_LARGE(12, "The metadata field of the offset request was too large.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NETWORK_EXCEPTION(13, "The server disconnected before a response was received.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    COORDINATOR_LOAD_IN_PROGRESS(14, "The coordinator is loading and hence can't process requests.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    COORDINATOR_NOT_AVAILABLE(15, "The coordinator is not available.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NOT_COORDINATOR(16, "This is not the correct coordinator.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_TOPIC_EXCEPTION(17, "The request attempted to perform an operation on an invalid topic.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    RECORD_LIST_TOO_LARGE(18, "The request included message batch larger than the configured segment size on the server.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NOT_ENOUGH_REPLICAS(19, "Messages are rejected since there are fewer in-sync replicas than required.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NOT_ENOUGH_REPLICAS_AFTER_APPEND(20, "Messages are written to the log, but to fewer in-sync replicas than required.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_REQUIRED_ACKS(21, "Produce request specified an invalid value for required acks.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    ILLEGAL_GENERATION(22, "Specified group generation id is not valid.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INCONSISTENT_GROUP_PROTOCOL(23,
            "The group member's supported protocols are incompatible with those of existing members" +
                " or first group member tried to join with empty protocol type or empty protocol list.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_GROUP_ID(24, "The configured groupId is invalid",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    UNKNOWN_MEMBER_ID(25, "The coordinator is not aware of this member.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_SESSION_TIMEOUT(26,
            "The session timeout is not within the range allowed by the broker " +
            "(as configured by group.min.session.timeout.ms and group.max.session.timeout.ms).",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    REBALANCE_IN_PROGRESS(27, "The group is rebalancing, so a rejoin is needed.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_COMMIT_OFFSET_SIZE(28, "The committing offset data size is not valid",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    TOPIC_AUTHORIZATION_FAILED(29, "Topic authorization failed.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    GROUP_AUTHORIZATION_FAILED(30, "Group authorization failed.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    CLUSTER_AUTHORIZATION_FAILED(31, "Cluster authorization failed.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_TIMESTAMP(32, "The timestamp of the message is out of acceptable range.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    UNSUPPORTED_SASL_MECHANISM(33, "The broker does not support the requested SASL mechanism.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    ILLEGAL_SASL_STATE(34, "Request is not valid given the current SASL state.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    UNSUPPORTED_VERSION(35, "The version of API is not supported.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    TOPIC_ALREADY_EXISTS(36, "Topic with this name already exists.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_PARTITIONS(37, "Number of partitions is invalid.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_REPLICATION_FACTOR(38, "Replication-factor is invalid.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_REPLICA_ASSIGNMENT(39, "Replica assignment is invalid.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_CONFIG(40, "Configuration is invalid.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    NOT_CONTROLLER(41, "This is not the correct controller for this cluster.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_REQUEST(42, "This most likely occurs because of a request being malformed by the " +
                "client library or the message was sent to an incompatible broker. See the broker logs " +
                "for more details.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    UNSUPPORTED_FOR_MESSAGE_FORMAT(43, "The message format version on the broker does not support the request.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    POLICY_VIOLATION(44, "Request parameters do not satisfy the configured policy.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    OUT_OF_ORDER_SEQUENCE_NUMBER(45, "The broker received an out of order sequence number",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    DUPLICATE_SEQUENCE_NUMBER(46, "The broker received a duplicate sequence number",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_PRODUCER_EPOCH(47, "Producer attempted an operation with an old epoch. Either there is a newer producer " +
            "with the same transactionalId, or the producer's transaction has been expired by the broker.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_TXN_STATE(48, "The producer attempted a transactional operation in an invalid state",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_PRODUCER_ID_MAPPING(49, "The producer attempted to use a producer id which is not currently assigned to " +
            "its transactional id",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    INVALID_TRANSACTION_TIMEOUT(50, "The transaction timeout is larger than the maximum value allowed by " +
                "the broker (as configured by max.transaction.timeout.ms).",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    CONCURRENT_TRANSACTIONS(51, "The producer attempted to update a transaction " +
                "while another concurrent operation on the same transaction was ongoing",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    TRANSACTION_COORDINATOR_FENCED(52, "Indicates that the transaction coordinator sending a WriteTxnMarker " +
            "is no longer the current coordinator for a given producer",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
        }),
    TRANSACTIONAL_ID_AUTHORIZATION_FAILED(53, "Transactional Id authorization failed",
                                                  new ApiExceptionBuilder() {
        @Override
        public Exception build(String message) {
            return new Exception(message);
        }
    }),
    SECURITY_DISABLED(54, "Security features are disabled.", new ApiExceptionBuilder() {
        @Override
        public Exception build(String message) {
            return new Exception(message);
        }
    }),
    OPERATION_NOT_ATTEMPTED(55, "The broker did not attempt to execute this operation. This may happen for batched RPCs " +
            "where some operations in the batch failed, causing the broker to respond without trying the rest.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
    }),
    KAFKA_STORAGE_ERROR(56, "Disk error when trying to access log file on the disk.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
    }),
    LOG_DIR_NOT_FOUND(57, "The user-specified log directory is not found in the broker config.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
    }),
    SASL_AUTHENTICATION_FAILED(58, "SASL Authentication failed.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
    }),
    UNKNOWN_PRODUCER_ID(59, "This exception is raised by the broker if it could not locate the producer metadata " +
            "associated with the producerId in question. This could happen if, for instance, the producer's records " +
            "were deleted because their retention time had elapsed. Once the last records of the producerId are " +
            "removed, the producer's metadata is removed from the broker, and future appends by the producer will " +
            "return this exception.",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
    }),
    REASSIGNMENT_IN_PROGRESS(60, "A partition reassignment is in progress",
        new ApiExceptionBuilder() {
            @Override
            public Exception build(String message) {
                return new Exception(message);
            }
    });

    private interface ApiExceptionBuilder {
        Exception build(String message);
    }


    private static Map<Class<?>, Errors> classToError = new HashMap<>();
    private static Map<Short, Errors> codeToError = new HashMap<>();

    static {
        for (Errors error : Errors.values()) {
            codeToError.put(error.code(), error);
            if (error.exception != null)
                classToError.put(error.exception.getClass(), error);
        }
    }

    private final short code;
    private final ApiExceptionBuilder builder;
    private final Exception exception;

    Errors(int code, String defaultExceptionString, ApiExceptionBuilder builder) {
        this.code = (short) code;
        this.builder = builder;
        this.exception = builder.build(defaultExceptionString);
    }

    /**
     * An instance of the exception
     */
    public Exception exception() {
        return this.exception;
    }

    /**
     * Create an instance of the Exception that contains the given error message.
     *
     * @param message    The message string to set.
     * @return           The exception.
     */
    public Exception exception(String message) {
        if (message == null) {
            // If no error message was specified, return an exception with the default error message.
            return exception;
        }
        // Return an exception with the given error message.
        return builder.build(message);
    }

    /**
     * Returns the class name of the exception or null if this is {@code Errors.NONE}.
     */
    public String exceptionName() {
        return exception == null ? null : exception.getClass().getName();
    }

    /**
     * The error code for the exception
     */
    public short code() {
        return this.code;
    }



    /**
     * Get a friendly description of the error (if one is available).
     * @return the error message
     */
    public String message() {
        if (exception != null)
            return exception.getMessage();
        return toString();
    }

    /**
     * Throw the exception if there is one
     */
    public static Errors forCode(short code) {
        Errors error = codeToError.get(code);
        if (error != null) {
            return error;
        } else {
            return UNKNOWN_SERVER_ERROR;
        }
    }

    /**
     * Return the error instance associated with this exception or any of its superclasses (or UNKNOWN if there is none).
     * If there are multiple matches in the class hierarchy, the first match starting from the bottom is used.
     */
    public static Errors forException(Throwable t) {
        Class<?> clazz = t.getClass();
        while (clazz != null) {
            Errors error = classToError.get(clazz);
            if (error != null)
                return error;
            clazz = clazz.getSuperclass();
        }
        return UNKNOWN_SERVER_ERROR;
    }

}
