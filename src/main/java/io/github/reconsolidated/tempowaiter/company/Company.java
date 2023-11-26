package io.github.reconsolidated.tempowaiter.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
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
    @Column(length = 1000)
    private String menuLink;
    @Column(length = 1000)
    private String backgroundImage;
    @Column(length = 1000)
    private String facebookLink;
    @Column(length = 1000)
    private String instagramLink;
}
