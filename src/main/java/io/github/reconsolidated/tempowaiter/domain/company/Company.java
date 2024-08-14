package io.github.reconsolidated.tempowaiter.domain.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Company {
    @Id
    @GeneratedValue(generator = "companies")
    @SequenceGenerator(name = "companies", allocationSize = 1)
    private Long id;
    private String name;
    @Column(length = 1000)
    private String menuLink;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(length = 1000)
    private List<String> backgroundImages = new ArrayList<>();
    @Column(length = 1000)
    private String facebookLink;
    @Column(length = 1000)
    private String instagramLink;
    @Column(length = 1000)
    private String tiktokLink;
    @Column(length = 1000)
    private String googleReviewLink;
    @Column(length = 1000)
    private String tripadvisorLink;
    private LocalDateTime lastViewedRequests;
    private Boolean isOrderingActivated = false;

    public Boolean getIsOrderingActivated() {
        if (isOrderingActivated == null) {
            return false;
        }
        return isOrderingActivated;
    }
}
