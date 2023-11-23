package io.github.reconsolidated.tempowaiter.company;

import io.github.reconsolidated.tempowaiter.card.Card;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Company> listCompanies() {
        return companyRepository.findAll();
    }

    public Company setCompanyName(Long companyId, String companyName) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        company.setName(companyName);
        return companyRepository.save(company);
    }
}
