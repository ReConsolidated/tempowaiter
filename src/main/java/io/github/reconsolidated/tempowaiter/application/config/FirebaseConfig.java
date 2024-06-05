package io.github.reconsolidated.tempowaiter.application.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.InputStream;

@Configuration
@AllArgsConstructor
@Profile("!test")
public class FirebaseConfig {
    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        return FirebaseApp.initializeApp(options);
    }
    @Bean
    GoogleCredentials googleCredentials() {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase.json")) {
            return GoogleCredentials.fromStream(serviceAccount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
