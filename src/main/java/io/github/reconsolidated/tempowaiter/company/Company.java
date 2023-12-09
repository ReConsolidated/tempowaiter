package io.github.reconsolidated.tempowaiter.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Company {
    @Id
    @GeneratedValue(generator = "companies")
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
}
