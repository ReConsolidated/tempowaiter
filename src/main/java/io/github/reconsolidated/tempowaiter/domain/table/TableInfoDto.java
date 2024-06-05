package io.github.reconsolidated.tempowaiter.domain.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TableInfoDto {
    private Long tableId;
    private Long cardId;
    private Long companyId;
    private String tableDisplayName;
    private Long lastCtr;
    private String companyName;
    private String menuLink;
}
