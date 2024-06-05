package io.github.reconsolidated.tempowaiter.application.menu;

import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUserRole;
import io.github.reconsolidated.tempowaiter.domain.company.Company;
import io.github.reconsolidated.tempowaiter.application.company.CompanyService;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItem;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemRepository;
import io.github.reconsolidated.tempowaiter.infrastracture.fileStorage.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuItemRepository menuItemRepository;
    private final CompanyService companyService;
    private final FileService fileService;

    public MenuItemDto getMenuItem(AppUser currentUser, Long itemId) {
        MenuItemDto dto = menuItemRepository.findById(itemId).map(MenuItemDto::fromEntity).orElseThrow(() ->
                new NoSuchElementException("No menu item found with id " + itemId));
        if (currentUser.getRole().equals(AppUserRole.USER) && !Objects.equals(dto.getCompanyId(), currentUser.getCompanyId())) {
            // return same response as if the item doesn't exist
            throw new NoSuchElementException("No menu item found with id " + itemId);
        }
        return dto;
    }

    public MenuItemDto createMenuItem(AppUser currentUser, MenuItemDto item) {
        if (item.getId() != null) {
            throw new IllegalArgumentException("Id must be null when creating a new menu item.");
        }
        if (!Objects.equals(item.getCompanyId(), currentUser.getCompanyId())) {
            throw new IllegalArgumentException("Company id must match current user's company id.");
        }
        Company company = companyService.getById(item.getCompanyId());
        String imageUrl = fileService.saveFiles("tempowaiter/menu-items/" + item.getCompanyId(), item.getImagesToSave()).get(0);
        item.setImageUrl(imageUrl);
        return MenuItemDto.fromEntity(menuItemRepository.save(item.toEntity(company)));
    }

    public List<MenuItemDto> listItems(Long companyId) {
        return menuItemRepository.findByCompanyId(companyId).stream()
                .map(MenuItemDto::fromEntity)
                .toList();
    }

    public MenuItemDto updateMenuItem(AppUser currentUser, MenuItemDto item) {
        if (item.getId() == null) {
            throw new IllegalArgumentException("Id must not be null when updating a menu item.");
        }
        if (!Objects.equals(item.getCompanyId(), currentUser.getCompanyId())) {
            throw new IllegalArgumentException("Company id must match current user's company id.");
        }
        Company company = companyService.getById(item.getCompanyId());
        MenuItem existingItem = menuItemRepository.findById(item.getId()).orElseThrow(() ->
                new NoSuchElementException("No menu item found with id " + item.getId()));
        if (!Objects.equals(existingItem.getCompany().getId(), item.getCompanyId())) {
            throw new IllegalArgumentException("Menu Item cannot change company id.");
        }
        if (!item.getImagesToSave().isEmpty()) {
            String imageUrl = fileService.saveFiles("tempowaiter/menu-items/" + item.getCompanyId(), item.getImagesToSave()).get(0);
            item.setImageUrl(imageUrl);
        }
        existingItem.setName(item.getName());
        existingItem.setPrice(item.getPrice());
        existingItem.setLowestHistoricalPrice(item.getLowestHistoricalPrice());
        existingItem.setDescription(item.getDescription());
        existingItem.setImageUrl(item.getImageUrl());
        existingItem.setUpsellingActive(item.isUpsellingActive());
        return MenuItemDto.fromEntity(menuItemRepository.save(existingItem));
    }

    public void deleteMenuItem(AppUser currentUser, Long id) {
        MenuItem item = menuItemRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No menu item found with id " + id));
        if (!Objects.equals(item.getCompany().getId(), currentUser.getCompanyId())) {
            throw new IllegalArgumentException("Company id must match current user's company id.");
        }
        menuItemRepository.delete(item);
    }
}
