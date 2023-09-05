package io.github.reconsolidated.tempowaiter.company;

import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(String name) {
        Company company = new Company();
        company.setName(name);
        return companyRepository.save(company);
    }

}
