//package org.project.exchange.model.user.service;
//
//import org.project.exchange.model.user.KakaoUser;
//import org.project.exchange.model.user.repository.KakaoUserRepository;
//import org.project.exchange.model.user.repository.UserRepository;
//import org.project.exchange.model.user.User;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.sql.Date;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class KakaoService {
//
//    @Value("${kakao.api_key}")
//    private String kakaoApiKey;
//
//    @Value("${kakao.redirect_uri}")
//    private String kakaoRedirectUri;
//
//    private final KakaoUserRepository kakaoUserRepository;
//    private final UserRepository userRepository;
//
//    public String getKakaoAccessToken(String code) {
//        String accessToken = "";
//        String requestURL = "https://kauth.kakao.com/oauth/token";
//
//        try {
//            URL url = new URL(requestURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//            conn.setDoOutput(true);
//
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//            StringBuilder sb = new StringBuilder();
//            sb.append("grant_type=authorization_code");
//            sb.append("&client_id=").append(kakaoApiKey);
//            sb.append("&redirect_uri=").append(kakaoRedirectUri);
//            sb.append("&code=").append(code);
//
//            bufferedWriter.write(sb.toString());
//            bufferedWriter.flush();
//
//            int responseCode = conn.getResponseCode();
//            log.info("responseCode : " + responseCode);
//
//            if (responseCode == 200) {
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                StringBuilder result = new StringBuilder();
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    result.append(line);
//                }
//                log.info("response body : " + result);
//
//                JsonElement element = JsonParser.parseString(result.toString());
//                accessToken = element.getAsJsonObject().get("access_token").getAsString();
//
//                bufferedReader.close();
//            } else if (responseCode == 400) {
//                throw new RuntimeException("카카오 인증 코드가 만료되었거나 유효하지 않습니다. 다시 로그인해주세요.");
//            } else {
//                log.error("Failed to get access token: " + responseCode);
//            }
//
//            bufferedWriter.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return accessToken;
//    }
//
//    public KakaoUser saveOrUpdateKakaoUser(String accessToken) {
//        HashMap<String, Object> userInfo = getUserInfo(accessToken);
//
//        if (userInfo.isEmpty()) {
//            throw new RuntimeException("Failed to get user info from Kakao");
//        }
//
//        String kakaoId = (String) userInfo.get("kakaoId");
//        String nickname = (String) userInfo.get("nickname");
//
//        Optional<KakaoUser> optionalKakaoUser = kakaoUserRepository.findByKakaoId(kakaoId);
//        KakaoUser kakaoUser;
//
//        if (optionalKakaoUser.isPresent()) {
//            kakaoUser = optionalKakaoUser.get();
//            log.info("기존 유저 업데이트: " + kakaoUser.getNickname());
//        } else {
//            User newUser = User.builder()
//                    .userName(nickname)
//                    .userEmail(kakaoId + "@kakao.com")
//                    .userPassword("") // 빈 문자열로 설정하여 null 문제를 회피
//                    .userGender(true)
//                    .userDateOfBirth(Date.valueOf(LocalDate.now()))
//                    .userCreatedAt(new Date(System.currentTimeMillis()))
//                    .userUpdatedAt(new Date(System.currentTimeMillis()))
//                    .build();
//
//            userRepository.save(newUser);
//
//            kakaoUser = KakaoUser.builder()
//                    .kakaoId(kakaoId)
//                    .nickname(nickname)
//                    .user(newUser)
//                    .build();
//
//            log.info("새 유저 생성: " + kakaoUser.getNickname());
//        }
//
//        return kakaoUserRepository.save(kakaoUser);
//    }
//
//    public HashMap<String, Object> getUserInfo(String accessToken) {
//        HashMap<String, Object> userInfo = new HashMap<>();
//        String postURL = "https://kapi.kakao.com/v2/user/me";
//
//        try {
//            URL url = new URL(postURL);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//
//            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
//
//            int responseCode = conn.getResponseCode();
//            log.info("responseCode : " + responseCode);
//
//            if (responseCode == 200) {
//                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                StringBuilder result = new StringBuilder();
//
//                while ((line = br.readLine()) != null) {
//                    result.append(line);
//                }
//                log.info("response body : " + result);
//
//                JsonElement element = JsonParser.parseString(result.toString());
//                JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
//
//                String kakaoId = element.getAsJsonObject().get("id").getAsString();
//                String nickname = properties.get("nickname").getAsString();
//
//                userInfo.put("kakaoId", kakaoId);
//                userInfo.put("nickname", nickname);
//
//                br.close();
//            } else {
//                log.error("Failed to get user info: " + responseCode);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return userInfo;
//    }
//}
