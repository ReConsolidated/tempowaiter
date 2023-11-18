package io.github.reconsolidated.tempowaiter.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TableInfo {
    @Id
    @GeneratedValue(generator = "table_info_id_generator")
    private Long tableId;
    private Long cardId;
    private Long companyId;
    @Setter
    private String tableDisplayName;
    @Setter
    private Long lastCtr;

    public void setCardId(Long cardId) {
        this.cardId = cardId;
        this.lastCtr = 0L;
    }

}
