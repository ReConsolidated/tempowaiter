package io.github.reconsolidated.tempowaiter.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private Long companyId;
    @ElementCollection
    private List<Long> cardIds = new ArrayList<>();
    private String tableDisplayName;
    @Setter
    private Long lastCtr;

}
