package io.github.reconsolidated.tempowaiter.company;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Company {
    @Id
    @GeneratedValue(generator = "companies")
    private Long id;
    private String name;
}
