package io.github.reconsolidated.tempowaiter.company;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

}
