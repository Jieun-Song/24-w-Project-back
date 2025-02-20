package org.project.exchange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.user.Dto.SignInRequest;
import org.project.exchange.model.user.Dto.SignInResponse;
import org.project.exchange.model.user.Dto.SignUpRequest;
import org.project.exchange.model.user.Dto.SignUpResponse;
import org.project.exchange.model.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

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

    @GetMapping("/kakao/signin")
    public ResponseEntity<ApiResponse<?>> kakaoSignIn(@RequestParam String code) {
        System.out.println("Kakao SignIn endpoint hit with code: " + code);
        SignInResponse response = userService.kakaoSignIn(code);
        if ("카카오 로그인 성공".equals(response.getMsg())) {
            return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(response, "카카오 로그인 성공"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(response.getMsg()));
    }
}
