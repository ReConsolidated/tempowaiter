package io.github.reconsolidated.tempowaiter.domain.menu;

import io.github.reconsolidated.tempowaiter.company.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class MenuItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Company company;

    private String name;
    private Long price;
    private Long lowestHistoricalPrice;
    private String description;
    private String imageUrl;
}
