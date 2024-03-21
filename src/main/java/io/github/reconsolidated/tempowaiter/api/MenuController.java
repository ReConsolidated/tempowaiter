package io.github.reconsolidated.tempowaiter.api;

import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.currentUser.CurrentUser;
import io.github.reconsolidated.tempowaiter.menuItem.MenuItemDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/menu/items")
@AllArgsConstructor
public class MenuController {

    @GetMapping("/{id}")
    public MenuItemDto getMenuItem(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/")
    public MenuItemDto createMenuItem(@CurrentUser AppUser currentUser, @RequestBody MenuItemDto item) {
        return null;
    }

    @GetMapping("/")
    public List<MenuItemDto> getMenuItems() {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto item) {
        if (!Objects.equals(id, item.getId())) {
            return ResponseEntity.badRequest().body("Id from path doesn't match item id in request body.");
        }
        return null;
    }


}
