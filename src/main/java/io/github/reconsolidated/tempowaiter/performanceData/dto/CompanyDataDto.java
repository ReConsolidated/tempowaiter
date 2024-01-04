package io.github.reconsolidated.tempowaiter.performanceData.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CompanyDataDto<T> {
    private Long companyId;
    private String companyName;
    private List<T> data;

    public CompanyDataDto(Long companyId, String companyName, List<T> data) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.data = data;
    }
}
