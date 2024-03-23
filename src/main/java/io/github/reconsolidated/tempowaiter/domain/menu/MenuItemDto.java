package io.github.reconsolidated.tempowaiter.domain.menu;

import io.github.reconsolidated.tempowaiter.company.Company;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Long companyId;
    private Long price;
    private Long lowestHistoricalPrice;
    private String description;
    private String imageUrl;
    private boolean upsellingActive;
    private Map<String, String> imagesToSave;

    public static MenuItemDto fromEntity(MenuItem menuItem) {
        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.id = menuItem.getId();
        menuItemDto.name = menuItem.getName();
        menuItemDto.companyId = menuItem.getCompany().getId();
        menuItemDto.price = menuItem.getPrice();
        menuItemDto.lowestHistoricalPrice = menuItem.getLowestHistoricalPrice();
        menuItemDto.description = menuItem.getDescription();
        menuItemDto.imageUrl = menuItem.getImageUrl();
        menuItemDto.upsellingActive = menuItem.isUpsellingActive();
        return menuItemDto;
    }

    public MenuItem toEntity(Company company) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        menuItem.setName(name);
        menuItem.setCompany(company);
        menuItem.setPrice(price);
        menuItem.setLowestHistoricalPrice(lowestHistoricalPrice);
        menuItem.setDescription(description);
        menuItem.setImageUrl(imageUrl);
        menuItem.setUpsellingActive(upsellingActive);
        return menuItem;
    }
}
