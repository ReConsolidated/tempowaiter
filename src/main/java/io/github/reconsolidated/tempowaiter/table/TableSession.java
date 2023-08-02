package io.github.reconsolidated.tempowaiter.table;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@NoArgsConstructor
public class TableSession {
    @Id
    @GeneratedValue(generator = "table_session_id_generator")
    private Long id;
    private long tableId;
    private long ctr;
    private String sessionId;
    private Date expirationDate;

    public TableSession(TableInfo tableInfo, String sessionId) {
        this.tableId = tableInfo.getTableId();
        this.ctr = tableInfo.getLastCtr();
        this.sessionId = sessionId;
    }
}
