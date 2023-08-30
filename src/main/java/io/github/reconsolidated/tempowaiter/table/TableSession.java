package io.github.reconsolidated.tempowaiter.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class TableSession {
    @Id
    @GeneratedValue(generator = "table_session_id_generator")
    private Long id;
    private long tableId;
    private long ctr;
    private String sessionId;
    private LocalDateTime expirationDate;

    public TableSession(TableInfo tableInfo, String sessionId, LocalDateTime expirationDate) {
        this.tableId = tableInfo.getTableId();
        this.ctr = tableInfo.getLastCtr();
        this.sessionId = sessionId;
        this.expirationDate = expirationDate;
    }
}
