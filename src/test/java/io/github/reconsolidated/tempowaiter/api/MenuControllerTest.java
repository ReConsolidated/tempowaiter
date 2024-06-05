package io.github.reconsolidated.tempowaiter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reconsolidated.tempowaiter.TestConfig;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUserDto;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.domain.company.Company;
import io.github.reconsolidated.tempowaiter.application.company.CompanyService;
import io.github.reconsolidated.tempowaiter.domain.menu.MenuItemDto;
import io.github.reconsolidated.tempowaiter.infrastracture.api.MenuController;
import io.github.reconsolidated.tempowaiter.infrastracture.security.JwtAuthenticationToken;
import io.github.reconsolidated.tempowaiter.infrastracture.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
@Import(TestConfig.class)
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MenuController menuController;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private JwtService jwtService;
    private Company company;
    private AppUserDto testUser;

    @BeforeEach
    public void setUp() {
        company = companyService.createCompany("test");
        testUser = appUserService.register("test@test.pl", "Emil");
        appUserService.setCompanyId(testUser.getEmail(), company.getId());
    }

    @Test
    public void testCreateMenuItem() throws Exception {
        // Create a MenuItemDto object
        MenuItemDto menuItemDto = new MenuItemDto();
        menuItemDto.setName("Test");
        menuItemDto.setCompanyId(company.getId());
        // Set properties for menuItemDto

        // Convert menuItemDto to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String menuItemDtoJson = objectMapper.writeValueAsString(menuItemDto);

        JwtAuthenticationToken principal = new JwtAuthenticationToken(jwtService.generateToken(testUser.getEmail()));
        principal.setEmail(testUser.getEmail());

        menuController.createMenuItem(appUserService.getUser(testUser.getEmail()).get(), menuItemDto);

        // Perform the POST request with authentication
        mockMvc.perform(MockMvcRequestBuilders.post("/menu/items/")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(principal))
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