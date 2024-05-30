package io.github.reconsolidated.tempowaiter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestConfig.class)
class TempowaiterApplicationTests {
	@Test
	void contextLoads() {
	}

}
