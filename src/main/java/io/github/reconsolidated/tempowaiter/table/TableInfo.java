package io.github.reconsolidated.tempowaiter.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TableInfo {
    @Id
    @GeneratedValue(generator = "table_info_id_generator")
    @SequenceGenerator(name = "table_info_id_generator", allocationSize = 1)
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
