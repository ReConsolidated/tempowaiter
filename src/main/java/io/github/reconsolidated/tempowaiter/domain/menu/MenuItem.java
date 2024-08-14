package io.github.reconsolidated.tempowaiter.domain.menu;

import io.github.reconsolidated.tempowaiter.domain.company.Company;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Double price;
    private Double lowestHistoricalPrice;
    @Column(length = 1000)
    private String description;
    private String imageUrl;
    private boolean upsellingActive;
}
