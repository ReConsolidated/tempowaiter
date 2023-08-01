package io.github.reconsolidated.tempowaiter.table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TableInfo {
    @Id
    @GeneratedValue(generator = "table_info_id_generator")
    private Long tableId;
    private Long companyId;
    private String tableDisplayName;
    @Setter
    private Long lastCtr;


}
