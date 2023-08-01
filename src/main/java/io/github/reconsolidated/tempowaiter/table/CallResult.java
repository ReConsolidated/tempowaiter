package io.github.reconsolidated.tempowaiter.table;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CallResult {
    Long tableId;
    String type;
    LocalDateTime expiresAt;
}
