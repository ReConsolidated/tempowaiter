package io.github.reconsolidated.tempowaiter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public FirebaseApp firebaseApp() {
        return Mockito.mock(FirebaseApp.class);
    }

    @Bean
    @Primary
    FirebaseMessaging firebaseMessaging() {
        return Mockito.mock(FirebaseMessaging.class);
    }

    @Bean
    @Primary
    GoogleCredentials googleCredentials() {
        return Mockito.mock(GoogleCredentials.class);
    }
}
