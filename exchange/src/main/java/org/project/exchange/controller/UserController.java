package org.project.exchange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.user.KakaoUser;
import org.project.exchange.model.user.User;
import org.project.exchange.model.user.Dto.FindPasswordRequest;
import org.project.exchange.model.user.Dto.KakaoLoginRequest;
import org.project.exchange.model.user.Dto.ResetNameResponse;
import org.project.exchange.model.user.Dto.SignInRequest;
import org.project.exchange.model.user.Dto.SignInResponse;
import org.project.exchange.model.user.Dto.SignUpRequest;
import org.project.exchange.model.user.Dto.SignUpResponse;
import org.project.exchange.model.user.Dto.UpdateUserInfoRequest;
import org.project.exchange.model.user.Dto.UserInfoResponse;
import org.project.exchange.model.user.repository.UserRepository;
import org.project.exchange.model.user.service.GoogleOAuthService;
import org.project.exchange.model.user.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j; // 📌 log 사용을 위한 Lombok 어노테이션

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final GoogleOAuthService googleOAuthService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(
            @Validated @RequestBody SignUpRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.createFail(bindingResult));
        }
        //회원가입 진행
        SignUpResponse userResponse = userService.signUp(request, request.getOtp(), request.getAgreedTerms());

        // ✅ JSON 직렬화 확인을 위한 로그 추가
        ApiResponse<SignUpResponse> response = ApiResponse.createSuccessWithMessage(userResponse, "회원가입 성공");
        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(response);
            System.out.println("✅ 직렬화된 API 응답: " + jsonResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if ("회원가입 성공".equals(userResponse.getMsg())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(ApiResponse.createError(userResponse.getMsg()));
    }


        // 로그인
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<?>> signIn(
            @Validated @RequestBody SignInRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body((ApiResponse<SignInResponse>) ApiResponse.createFail(bindingResult));
        }

        SignInResponse response = userService.signIn(request);
        if ("로그인 성공".equals(response.getMsg())) {
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(response, "로그인 성공"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createError(response.getMsg()));
    }

    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<?>> signOut(@RequestBody Map<String, String> request)
            throws JsonProcessingException {
        String token = request.get("token");
        String response = userService.signOut(token);
        if ("로그아웃 성공".equals(response)) {
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(response, "로그아웃 성공"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createError(response));
    }

    //카카오 로그인
    @PostMapping("/kakao/signin")
    public ResponseEntity<ApiResponse<?>> kakaoSignIn(@RequestBody KakaoLoginRequest request) {
        log.info("🔍 Raw Request Body: " + request);
        log.info("🔍 Kakao SignIn endpoint hit with token: " + request.getAccessToken());

        if (request.getAccessToken() == null || request.getAccessToken().isEmpty()) {
            log.error("❌ 카카오 로그인 실패: 토큰이 없습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError("카카오 액세스 토큰이 없습니다."));
        }

        SignInResponse response = userService.kakaoSignIn(request.getAccessToken());

        if ("카카오 로그인 성공".equals(response.getMsg())) {
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(response, "카카오 로그인 성공"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(response.getMsg()));
    }

    // 아이디 찾기 - 이름, 생년월일
    @GetMapping("/find-id")
    public ResponseEntity<ApiResponse<?>> findId(
            @RequestParam String userName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate userDateOfBirth) {

        String userEmail = userService.findId(userName, userDateOfBirth);
        if (userEmail != null) {
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(userEmail, "아이디 찾기 성공"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.createError("아이디 찾기 실패"));
    }

    // 비밀번호 찾기 (임시 비밀번호 발급)
    @PostMapping("/find-password")
    public ResponseEntity<ApiResponse<?>> findPassword(@RequestBody FindPasswordRequest request) {
        String result = userService.findPassword(request.getUserEmail(), request.getUserName());
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(result, result));
    }

    // 비밀번호 재설정 - 이메일, 현재 비밀번호, 새 비밀번호, 비밀번호 확인
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        if (userEmail == null || currentPassword == null || newPassword == null || confirmPassword == null) {
            return ResponseEntity.badRequest().body(ApiResponse.createError("필수 입력값이 누락되었습니다."));
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(ApiResponse.createError("새 비밀번호와 비밀번호 확인이 일치하지 않습니다."));
        }

        String result = userService.resetPassword(userEmail, currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(result, result));
    }

    

    // 이름 재설정
    @PostMapping("/reset-name")
    public ResponseEntity<ApiResponse<?>> resetName(@RequestBody Map<String, String> request) {
        String userEmail = request.get("userEmail");
        String newName = request.get("newName");

        if (userEmail == null || newName == null) {
            return ResponseEntity.badRequest().body(ApiResponse.createError("필수 입력값이 누락되었습니다."));
        }
        
        ResetNameResponse response = userService.resetName(userEmail, newName);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(response, response.getMsg()));
    }
    
    // 아이디로 사용자 정보 조회
    @GetMapping("/user-info")
    public ResponseEntity<ApiResponse<?>> getUserInfo(@RequestHeader("Authorization") String token) {
        // 토큰 앞에 "Bearer " 붙어 있다면 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            UserInfoResponse userInfo = userService.getUserInfoFromToken(token);
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(userInfo, "사용자 정보 조회 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.createError("토큰 인증 실패 또는 사용자 조회 실패: " + e.getMessage()));
        }
    }

    // 아이디로 사용자 환율조회
    @GetMapping("/user-currency")
    public ResponseEntity<ApiResponse<Long>> getUserCurrency(@RequestHeader("Authorization") String token) {
        // 토큰 앞에 "Bearer " 붙어 있다면 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            Long userCurrency = userService.getUserCurrency(token);
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(userCurrency, "사용자 정보 조회 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body((ApiResponse<Long>) ApiResponse.createError("토큰 인증 실패 또는 사용자 조회 실패: " + e.getMessage()));
        }
    }


    // 회원정보 수정하기 - 생년월일, 이름 (이메일은 변경 불가)
    @PostMapping("/update-user-info")
    public ResponseEntity<ApiResponse<?>> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest request) {
        try {
            UserInfoResponse updatedUser = userService.updateUserInfo(request);

            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.createError("회원정보 갱신 결과가 null입니다."));
            }

            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(updatedUser, "회원정보가 성공적으로 변경되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.createError(e.getMessage()));
        }
    }

    // 일반회원 탈퇴
    @PostMapping("/withdrawal")
    public ResponseEntity<ApiResponse<?>> withdrawal(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String password = request.get("password");
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.createError("비밀번호가 필요합니다."));
        }

        try {
            String result = userService.deleteUser(token, password);
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(result, "회원 탈퇴 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.createError("회원 탈퇴 실패: " + e.getMessage()));
        }
    }

    // 카카오 회원 탈퇴
    @PostMapping("/kakao/withdrawal")
    public ResponseEntity<ApiResponse<?>> kakaoWithdrawal(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            userService.deleteKakaoUser(token); 

            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage("카카오 회원 탈퇴 성공", "카카오 회원 탈퇴 성공"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.createError("카카오 회원 탈퇴 실패: " + e.getMessage()));
        }
    }

    // 구글 로그인
    @PostMapping("/google/signin")
    public ResponseEntity<ApiResponse<?>> googleLogin(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        if (idToken == null || idToken.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.createError("ID token 누락"));
        }

        Map<String, Object> userInfo = googleOAuthService.decodeIdToken(idToken);
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        SignInResponse response = userService.googleSignIn(email, name);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(response, "구글 로그인 성공"));
    }
    
} 