package io.github.reconsolidated.tempowaiter.infrastracture.api;

import io.github.reconsolidated.tempowaiter.application.menu.MenuService;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/menu/items/{id}")
    public MenuItemDto getMenuItem(@CurrentUser AppUser currentUser, @PathVariable Long id) {
        return menuService.getMenuItem(currentUser, id);
    }

    @PostMapping("/menu/items")
    public MenuItemDto createMenuItem(@CurrentUser AppUser currentUser, @Valid @RequestBody MenuItemDto item) {
        return menuService.createMenuItem(currentUser, item);
    }

    @GetMapping("/menu/items")
    public List<MenuItemDto> getMenuItems(@CurrentUser AppUser currentUser) {
        return menuService.listItems(currentUser.getCompanyId());
    }

    @PatchMapping("/menu/items/{id}")
    public MenuItemDto updateMenuItem(@CurrentUser AppUser currentUser,
                                            @PathVariable Long id,
                                            @RequestBody MenuItemDto item) {
        return menuService.updateMenuItem(currentUser, item);
    }

    @DeleteMapping("/menu/items/{id}")
    public void deleteMenuItem(@CurrentUser AppUser currentUser, @PathVariable Long id) {
        menuService.deleteMenuItem(currentUser, id);
    }


}
