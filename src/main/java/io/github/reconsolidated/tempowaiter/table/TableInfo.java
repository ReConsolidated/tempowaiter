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
    @Setter
    private Long cardId;
    private Long companyId;
    private String tableDisplayName;
    @Setter
    private Long lastCtr;

}
