package io.github.reconsolidated.tempowaiter.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Company {
    @Id
    @GeneratedValue(generator = "companies")
    private Long id;
    private String name;
    private String menuLink;
    private String backgroundImage;
    private String facebookLink;
    private String instagramLink;
}
