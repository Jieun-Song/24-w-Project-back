package org.project.exchange.model.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.user.Dto.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class GoogleOAuthService {

    private final ObjectMapper objectMapper;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestTemplate restTemplate;

    @Autowired
    public GoogleOAuthService(
            ObjectMapper objectMapper,
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
            @Value("${spring.security.oauth2.client.registration.google.client-secret}") String clientSecret,
            @Value("${google.oauth.redirect-uri}") String redirectUri,
            RestTemplateBuilder restTemplateBuilder) {
        this.objectMapper = objectMapper;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.restTemplate = restTemplateBuilder.build();
    }

    /** auth code → access/refresh/ID 토큰 교환 */
    public TokenResponse exchangeAuthCode(String authCode) {
        String url = "https://oauth2.googleapis.com/token";
        Map<String, String> req = Map.of(
                "grant_type", "authorization_code",
                "client_id", clientId,
                "client_secret", clientSecret,
                "redirect_uri", redirectUri,
                "code", authCode);
        return restTemplate.postForObject(url, req, TokenResponse.class);
    }

    /** 구글 리프레시 토큰 취소 */
    public void revokeToken(String refreshToken) {
        String url = "https://oauth2.googleapis.com/revoke?token=" + refreshToken;
        restTemplate.postForLocation(url, null);
    }

    /** ID 토큰 디코딩 */
    public Map<String, Object> decodeIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3)
                throw new IllegalArgumentException("Invalid ID token format.");

            String payloadJson = new String(
                    Base64.getUrlDecoder().decode(parts[1]),
                    StandardCharsets.UTF_8);
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
            log.debug("Decoded Google ID Token Payload: {}", payload);
            return payload;
        } catch (Exception e) {
            log.error("Failed to decode Google ID token", e);
            throw new RuntimeException("Invalid Google ID token", e);
        }
    }
}
