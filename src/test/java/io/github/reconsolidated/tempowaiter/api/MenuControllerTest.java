package io.github.reconsolidated.tempowaiter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUser;
import io.github.reconsolidated.tempowaiter.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.company.Company;
import io.github.reconsolidated.tempowaiter.company.CompanyService;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MenuController menuController;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AppUserService appUserService;
    private Company company;
    private AppUser testUser;

    @BeforeEach
    public void setUp() {
        company = companyService.createCompany("test");
        testUser = appUserService.getOrCreateUser("test", "test@test.pl", "Emil", "Testowy");
        appUserService.setCompanyId(testUser.getEmail(), company.getId());
    }

    @Test
    public void testCreateMenuItem() throws Exception {
        // Create a MenuItemDto object
        MenuItemDto menuItemDto = new MenuItemDto();
        // Set properties for menuItemDto

        // Convert menuItemDto to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String menuItemDtoJson = objectMapper.writeValueAsString(menuItemDto);

        // Create UserDetails for testUser
        UserDetails userDetails = appUserService.loadUserByUsername(testUser.getEmail());

        // Perform the POST request with authentication
        mockMvc.perform(MockMvcRequestBuilders.post("/menu/items/")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(menuItemDto.getName())); // Assuming the name of menuItemDto is set
    }

    @Test
    void createMenuItem() {
    }

    @Test
    void getMenuItem() {

    }



    @Test
    void getMenuItems() {
    }

    @Test
    void updateMenuItem() {
    }
}