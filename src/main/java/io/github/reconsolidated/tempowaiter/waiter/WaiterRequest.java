package io.github.reconsolidated.tempowaiter.waiter;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class WaiterRequest {
    @Id
    @GeneratedValue(generator = "waiter_request_id_generator")
    private Long id;
    private Long requestedAt;
    private Long putInProgressAt;
    private Long inProgressWaiterAppUserId;
    private Long resolvedAt;
    private Long companyId;
    private Long tableId;
    private Long cardId;
    private String clientSessionId;
    private String type;
    @Enumerated(EnumType.STRING)
    private RequestState state;
}
