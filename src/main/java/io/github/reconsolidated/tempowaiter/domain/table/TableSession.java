package io.github.reconsolidated.tempowaiter.domain.table;


import io.github.reconsolidated.tempowaiter.domain.company.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TableSession {
    @Id
    @GeneratedValue(generator = "table_session_id_generator")
    @SequenceGenerator(name = "table_session_id_generator", allocationSize = 1)
    private Long id;
    private long tableId;
    private long cardId;
    private long ctr;
    private String sessionId;
    private Long startedAt;
    private Long lastRequestAt;
    private boolean isOverwritten = false;
    private LocalDateTime expirationDate;
    @ManyToOne
    private Company company;

    public TableSession(TableInfo tableInfo, Company company, String sessionId, LocalDateTime expirationDate, boolean isOverwritten) {
        this.cardId = tableInfo.getCardId();
        this.ctr = tableInfo.getLastCtr();
        this.tableId = tableInfo.getTableId();
        this.startedAt = System.currentTimeMillis();
        this.sessionId = sessionId;
        this.expirationDate = expirationDate;
        this.isOverwritten = isOverwritten;
        this.company = company;
    }
}
