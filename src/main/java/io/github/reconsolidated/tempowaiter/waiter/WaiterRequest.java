package io.github.reconsolidated.tempowaiter.waiter;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WaiterRequest {
    @Id
    @GeneratedValue(generator = "waiter_request_id_generator")
    @SequenceGenerator(name = "waiter_request_id_generator", allocationSize = 1)
    private Long id;
    private Long requestedAt;
    private Long putInProgressAt;
    private Long inProgressWaiterAppUserId;
    private Long resolvedAt;
    private Long companyId;
    private LocalDateTime lastNotificationAt;
    private Long tableId;
    private String tableName;
    private Long cardId;
    private LocalDateTime emailReportedAt;
    private String type;
    @Column(length = 1000)
    private String additionalData;
    @Enumerated(EnumType.STRING)
    private RequestState state;
}
