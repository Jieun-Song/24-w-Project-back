package org.project.exchange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.user.Dto.SignInRequest;
import org.project.exchange.model.user.Dto.SignInResponse;
import org.project.exchange.model.user.Dto.SignUpRequest;
import org.project.exchange.model.user.Dto.SignUpResponse;
import org.project.exchange.model.user.service.UserService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Validated @RequestBody SignUpRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body((ApiResponse<SignUpResponse>) ApiResponse.createFail(bindingResult));
        }

        SignUpResponse response = userService.signUp(request);
        if (response.getMsg().equals("회원가입 성공")) {
            return ResponseEntity.ok(ApiResponse.createSuccess(response));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body((ApiResponse<SignUpResponse>) ApiResponse.createError(response.getMsg()));
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SignInResponse>> signIn(@Validated @RequestBody SignInRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body((ApiResponse<SignInResponse>) ApiResponse.createFail(bindingResult));
        }

        SignInResponse response = userService.signIn(request);
        if (response.getMsg().equals("로그인 성공")) {
            return ResponseEntity.ok(ApiResponse.createSuccess(response));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body((ApiResponse<SignInResponse>) ApiResponse.createError(response.getMsg()));
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<String>> signOut(@RequestBody Map<String, String> request)
            throws JsonProcessingException {
        String token = request.get("token");
        String response = userService.signOut(token);
        if (response.equals("로그아웃 성공")) {
            return ResponseEntity.ok(ApiResponse.createSuccess(response));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body((ApiResponse<String>) ApiResponse.createError(response));
    }

    // @GetMapping("/kakao/signin")
    // public ResponseEntity<?> kakaoSignIn(@RequestParam String code) {
    //     System.out.println("Kakao SignIn endpoint hit with code: " + code);
    //     SignInResponse response = userService.kakaoSignIn(code);
    //     if (response.getMsg().equals("카카오 로그인 성공")) {
    //         return ResponseEntity.ok(ApiResponse.createSuccess(response));
    //     }
    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(response.getMsg()));
    // }
}
