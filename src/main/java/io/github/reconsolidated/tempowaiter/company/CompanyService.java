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
        Company company = getById(companyId);
        if (userId.equals(companyId)) {
            company.setMenuLink(menuLink);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company getById(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(
                () -> new IllegalArgumentException(
                        "Company with id %d doesn't exist!".formatted(companyId))
        );
    }

    public Company setCompanyBackgroundImage(Long userId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userId.equals(companyId)) {
            company.setBackgroundImage(content);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyFacebookLink(Long userId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userId.equals(companyId)) {
            company.setFacebookLink(content);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyInstagramLink(Long userId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userId.equals(companyId)) {
            company.setInstagramLink(content);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company getCompany(Long currentUserCompanyId, Long companyId) {
        if (currentUserCompanyId.equals(companyId)) {
            return getById(companyId);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }
}
