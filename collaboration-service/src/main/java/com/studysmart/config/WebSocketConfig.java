package com.studysmart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register STOMP endpoints for clients to connect to
        registry.addEndpoint("/ws-connect")
                .setAllowedOriginPatterns("*")  // For development, restrict in production
                .withSockJS();  // Fallback for browsers without WebSocket support
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory message broker for sending messages
        // Prefix for application-handled endpoints (controllers)
        registry.setApplicationDestinationPrefixes("/app");

        // Prefix for broker-handled destinations
        registry.enableSimpleBroker("/topic", "/queue");

        // /topic for broadcasting (one-to-many)
        // /queue for point-to-point messaging (one-to-one)
    }
}