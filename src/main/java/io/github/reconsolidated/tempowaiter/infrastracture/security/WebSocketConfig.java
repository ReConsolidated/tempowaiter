package io.github.reconsolidated.tempowaiter.infrastracture.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry
                                               registry) {
        registry.addEndpoint("/public/websocket-entry")
                .setAllowedOrigins("http://localhost", "http://localhost:4200", "http://tempowaiter.pl",
                        "https://tempowaiter.pl", "https://tempo-front.fly.dev", "https://tempo-front-staging.fly.dev").withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/public");
    }
}