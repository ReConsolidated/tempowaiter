package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.application.menu.MenuService;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/menu/items")
@AllArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/{id}")
    public MenuItemDto getMenuItem(@CurrentUser AppUser currentUser, @PathVariable Long id) {
        return menuService.getMenuItem(currentUser, id);
    }

    @PostMapping("/")
    public MenuItemDto createMenuItem(@CurrentUser AppUser currentUser, @Valid @RequestBody MenuItemDto item) {
        return menuService.createMenuItem(currentUser, item);
    }

    @GetMapping("/")
    public List<MenuItemDto> getMenuItems(@CurrentUser AppUser currentUser) {
        return menuService.listItems(currentUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto item) {
        if (!Objects.equals(id, item.getId())) {
            return ResponseEntity.badRequest().body("Id from path doesn't match item id in request body.");
        }
        return null;
    }


}
