package io.github.reconsolidated.tempowaiter.table;

import io.github.reconsolidated.tempowaiter.waiter.RequestState;
import lombok.Value;

@Value
public class CallState {
    Long tableId;
    String type;
    RequestState requestState;
}
