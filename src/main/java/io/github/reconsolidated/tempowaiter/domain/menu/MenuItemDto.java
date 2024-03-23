package io.github.reconsolidated.tempowaiter.domain.menu;

import io.github.reconsolidated.tempowaiter.company.Company;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MenuItemDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Long companyId;

    public static MenuItemDto fromEntity(MenuItem menuItem) {
        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.id = menuItem.getId();
        menuItemDto.name = menuItem.getName();
        menuItemDto.companyId = menuItem.getCompany().getId();
        return menuItemDto;
    }

    public MenuItem toEntity(Company company) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        menuItem.setName(name);
        menuItem.setCompany(company);
        return menuItem;
    }
}
