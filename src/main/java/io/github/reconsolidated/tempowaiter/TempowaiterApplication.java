package io.github.reconsolidated.tempowaiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories
@EnableWebMvc
@Configuration
public class TempowaiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TempowaiterApplication.class, args);
	}

}
