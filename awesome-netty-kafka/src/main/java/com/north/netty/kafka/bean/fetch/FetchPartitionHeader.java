package com.north.netty.kafka.bean.fetch;

import java.io.Serializable;
import java.util.List;

public class FetchPartitionHeader implements Serializable {
    private Integer partition;
    private Short errorCode;
    private Long highWaterMark;
    private Long lastStableOffset;
    private Long logStartOffset;
    private List<AbortedTransaction> abortedTransactions;

}
