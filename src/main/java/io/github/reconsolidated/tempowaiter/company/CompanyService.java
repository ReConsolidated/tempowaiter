package io.github.reconsolidated.tempowaiter.company;

import io.github.reconsolidated.tempowaiter.table.TableService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final TableService tableService;

    public CompanyService(CompanyRepository companyRepository, @Lazy TableService tableService) {
        this.companyRepository = companyRepository;
        this.tableService = tableService;
    }

    public Company createCompany(String name) {
        Company company = new Company();
        company.setName(name);
        return companyRepository.save(company);
    }

    public CompanyListDto listCompanies() {
        List<Company> companies = companyRepository.findAll();
        return new CompanyListDto(companies, tableService);
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

    public Company setCompanyGoogleReviewLink(Long userId, Long companyId, String googleReviewLink) {
        Company company = getById(companyId);
        if (userId.equals(companyId)) {
            company.setGoogleReviewLink(googleReviewLink);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyTripadvisorLink(Long userId, Long companyId, String tripadvisorLink) {
        Company company = getById(companyId);
        if (userId.equals(companyId)) {
            company.setTripadvisorLink(tripadvisorLink);
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

    public Company addCompanyBackgroundImage(Long userCompanyId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userCompanyId.equals(companyId)) {
            company.getBackgroundImages().add(content);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company removeCompanyBackgroundImage(Long userCompanyId, Long companyId, int imageId) {
        Company company = getById(companyId);
        if (userCompanyId.equals(companyId)) {
            try {
                company.getBackgroundImages().remove(imageId);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Image with id %d doesn't exist!".formatted(imageId));
            }

            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyFacebookLink(Long userCompanyId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userCompanyId.equals(companyId)) {
            company.setFacebookLink(content);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyInstagramLink(Long userCompanyId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userCompanyId.equals(companyId)) {
            company.setInstagramLink(content);
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }

    public Company setCompanyTiktokLink(Long userCompanyId, Long companyId, String content) {
        Company company = getById(companyId);
        if (userCompanyId.equals(companyId)) {
            company.setTiktokLink(content);
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


    public Company updateCompanyBackgroundImage(Long userCompanyId, Long companyId, Integer imageId, String content) {
        Company company = getById(companyId);
        if (userCompanyId.equals(companyId)) {
            try {
                company.getBackgroundImages().set(imageId, content);
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Image with id %d doesn't exist!".formatted(imageId));
            }
            return companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("You are not the owner of this company");
        }
    }
}
