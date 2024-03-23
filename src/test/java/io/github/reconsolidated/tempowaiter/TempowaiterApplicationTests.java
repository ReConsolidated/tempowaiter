package io.github.reconsolidated.tempowaiter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class TempowaiterApplicationTests {
	@MockBean
	private JavaMailSender javaMailSender;
	@Test
	void contextLoads() {
	}

}
