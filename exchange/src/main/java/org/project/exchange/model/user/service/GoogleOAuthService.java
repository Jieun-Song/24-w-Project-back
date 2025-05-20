package org.project.exchange.model.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final ObjectMapper objectMapper;

    /**
     * 구글 ID 토큰을 디코딩하여 사용자 정보를 추출
     *
     * @param idToken 프론트에서 받은 Google ID Token
     * @return 이메일, 이름 등이 포함된 사용자 정보 Map
     */
    public Map<String, Object> decodeIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid ID token format.");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);

            log.debug("Decoded Google ID Token Payload: {}", payload);

            return payload;
        } catch (Exception e) {
            log.error("Failed to decode Google ID token", e);
            throw new RuntimeException("Invalid Google ID token", e);
        }
    }
}

// @Service
// @RequiredArgsConstructor
// public class GoogleOAuthService {

//     private final WebClient webClient;

//     public Map<String, Object> getUserInfo(String accessToken) {
//         return webClient.get()
//                 .uri("https://www.googleapis.com/oauth2/v3/userinfo")
//                 .headers(h -> h.setBearerAuth(accessToken))
//                 .retrieve()
//                 .bodyToMono(Map.class)
//                 .block();
//     }
// }
