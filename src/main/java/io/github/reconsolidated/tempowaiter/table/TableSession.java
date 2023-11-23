package io.github.reconsolidated.tempowaiter.table;


import io.github.reconsolidated.tempowaiter.company.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TableSession {
    @Id
    @GeneratedValue(generator = "table_session_id_generator")
    private Long id;
    private long tableId;
    private long cardId;
    private long ctr;
    private String sessionId;
    // whether there is a newer session that overwrote this one
    private Long startedAt;
    private Long lastRequestAt;
    private boolean isOverwritten = false;
    private LocalDateTime expirationDate;
    private String companyName;
    private String menuLink;

    public TableSession(TableInfo tableInfo, Company company, String sessionId, LocalDateTime expirationDate, boolean isOverwritten) {
        this.cardId = tableInfo.getCardId();
        this.ctr = tableInfo.getLastCtr();
        this.tableId = tableInfo.getTableId();
        this.startedAt = System.currentTimeMillis();
        this.sessionId = sessionId;
        this.expirationDate = expirationDate;
        this.isOverwritten = isOverwritten;
        this.companyName = company.getName();
        this.menuLink = company.getMenuLink();
    }
}
