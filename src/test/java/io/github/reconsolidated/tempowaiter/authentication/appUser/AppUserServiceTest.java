package io.github.reconsolidated.tempowaiter.authentication.appUser;

import io.github.reconsolidated.tempowaiter.TestConfig;
import io.github.reconsolidated.tempowaiter.domain.authentication.appUser.AppUserService;
import io.github.reconsolidated.tempowaiter.domain.waitingCompanyAssignment.WaitingCompanyAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestConfig.class)
class AppUserServiceTest {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private WaitingCompanyAssignmentRepository waitingCompanyAssignmentRepository;

    @Test
    void setCompanyId() {
        String email = "email@any.com";
        appUserService.setCompanyId(email, 1L);
        appUserService.setCompanyId(email, 2L);
        var assignment = waitingCompanyAssignmentRepository.findByEmail(email);
        assertTrue(assignment.isPresent());
        assertEquals(assignment.get().getCompanyId(), 2L);
    }
}