package io.github.reconsolidated.tempowaiter.company;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
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

    public Company setCompanyName(Long userId, Long companyId, String companyName) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        if (userId.equals(companyId)) {
            company.setName(companyName);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyMenuLink(Long userId, Long companyId, String menuLink) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        if (userId.equals(companyId)) {
            company.setMenuLink(menuLink);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }
}
