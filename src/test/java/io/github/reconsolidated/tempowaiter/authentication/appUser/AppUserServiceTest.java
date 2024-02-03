package io.github.reconsolidated.tempowaiter.authentication.appUser;

import io.github.reconsolidated.tempowaiter.waitingCompanyAssignment.WaitingCompanyAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserServiceTest {
    @MockBean
    private JavaMailSender javaMailSender;
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