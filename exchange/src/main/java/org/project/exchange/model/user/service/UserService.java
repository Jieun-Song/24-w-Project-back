package org.project.exchange.model.user.service;

import org.project.exchange.model.user.Dto.SignInRequest;
import org.project.exchange.model.user.Dto.SignInResponse;
import org.project.exchange.model.user.Dto.SignUpRequest;
import org.project.exchange.model.user.Dto.SignUpResponse;
import org.project.exchange.model.user.KakaoUser;
import org.project.exchange.model.user.RefreshToken;
import org.project.exchange.model.user.User;
import org.project.exchange.model.user.repository.RefreshTokenRepository;
import org.project.exchange.model.user.repository.UserRepository;
import org.project.exchange.config.TokenProvider;
import org.project.exchange.model.auth.repository.PermissionRepository;
import org.project.exchange.model.auth.service.EmailService;
import org.project.exchange.model.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.Date;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final PermissionService permissionService; // 약관 동의 관리
    private final EmailService emailService; // 이메일 인증 관리
//    private final KakaoService kakaoService; // 카카오 로그인 관리
    
    @Transactional
    public void sendOtpToEmail(String email) {
        emailService.setEmail(email);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest request, String otp, List<Boolean> agreedTerms) {

        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            return SignUpResponse.builder()
                    .msg("이메일이 이미 존재합니다.")
                    .build();
        }

        // 이메일 인증 코드 확인
        if (!emailService.checkAuthNumber(request.getUserEmail(), otp)) {
            return SignUpResponse.builder()
                    .msg("이메일 인증 코드가 유효하지 않습니다.")
                    .build();
        }

        // 비밀번호 형식 확인
        if (!isValidPassword(request.getUserPassword())) {
            return SignUpResponse.builder()
                    .msg("비밀번호 형식이 올바르지 않습니다. 비밀번호는 8자 이상 16자 이하, 문자, 숫자, 특수문자를 포함해야 합니다.")
                    .build();
        }

        // 사용자 생성
        User user = User.builder()
                .userName(request.getUserName())
                .userDateOfBirth(request.getUserDateOfBirth())
                .userGender(request.isUserGender())
                .userEmail(request.getUserEmail())
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
                .userCreatedAt(new Date(System.currentTimeMillis()))
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .build();

        userRepository.save(user);

        // 약관 동의 저장
        permissionService.saveAgreedTerms(user, agreedTerms);

        return SignUpResponse.builder()
                .msg("회원가입 성공")
                .build();
    }

    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        
        User user = userRepository.findByUserEmail(request.getUserEmail());
        if (user == null) {
            return SignInResponse.builder()
                    .msg("이메일이 존재하지 않습니다.")
                    .build();
        }
        if (!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())) {
            return SignInResponse.builder()
                    .msg("비밀번호가 일치하지 않습니다.")
                    .build();
        }

        // 약관 동의 여부 확인
        if (!permissionService.hasAgreedToRequiredTerms(user)) {
            return SignInResponse.builder()
                    .msg("필수 약관에 동의하지 않았습니다.")
                    .build();
        }

        // 토큰 생성
        String accessToken = tokenProvider.createToken(user);
        String refreshToken = tokenProvider.createRefreshToken();

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findById(user.getUserId());
        if (oldRefreshToken.isEmpty()) {
            RefreshToken newRefreshToken = RefreshToken.builder()
                    .tokenId(user.getUserId())
                    .refreshToken(refreshToken)
                    .User(user)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        } else {
            RefreshToken newRefreshToken = oldRefreshToken.get().toBuilder()
                    .refreshToken(refreshToken)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        }

        return SignInResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .msg("로그인 성공")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public String signOut(String token) throws JsonProcessingException {
        // 토큰으로 사용자 확인
        String username = tokenProvider.validateTokenAndGetSubject(token);
        User user = userRepository.findByUserEmail(username);
        if (user == null || refreshTokenRepository.findById(user.getUserId()).isEmpty()) {
            return "로그아웃 실패";
        }

        try {
            // Refresh Token 삭제
            refreshTokenRepository.deleteById(user.getUserId());
        } catch (Exception e) {
            return "로그아웃 실패";
        }

        return "로그아웃 성공";
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,16}$";
        return Pattern.matches(passwordPattern, password);
    }

//    @Transactional
//    public SignInResponse kakaoSignIn(String code) {
//    System.out.println("Received Kakao code: " + code);

    // 카카오 인증 코드를 사용하여 액세스 토큰을 얻음
//    String accessToken = kakaoService.getKakaoAccessToken(code);
//    if (accessToken == null || accessToken.isEmpty()) {
//    throw new RuntimeException("카카오 인증 코드가 만료되었거나 유효하지 않습니다. 다시 로그인해주세요.");
//    }
//    System.out.println("Received Kakao access token: " + accessToken);
//
//    // 카카오 유저 정보를 저장 또는 업데이트
//    KakaoUser kakaoUser = kakaoService.saveOrUpdateKakaoUser(accessToken);
//    if (kakaoUser == null) {
//    throw new RuntimeException("카카오 사용자 정보를 가져오는 데 실패했습니다.");
//    }
//    System.out.println("Kakao user retrieved or updated: " + kakaoUser);
//
//    User user = kakaoUser.getUser();
//    if (user == null) {
//    throw new RuntimeException("해당 카카오 사용자에 대한 유저 정보가 없습니다.");
//    }
//    System.out.println("User associated with Kakao user: " + user);
//
//    // User 객체를 기반으로 토큰 생성
//    String accessTokenJwt = tokenProvider.createToken(user);
//    String refreshTokenJwt = tokenProvider.createRefreshToken();
//    System.out.println("Generated JWT access token: " + accessTokenJwt + ",refresh token: " + refreshTokenJwt);
//
//    // Refresh Token 저장
//    Optional<RefreshToken> oldRefreshToken =
//    refreshTokenRepository.findById(user.getUserId());
//    if (oldRefreshToken.isEmpty()) {
//    RefreshToken newRefreshToken = RefreshToken.builder()
//    .tokenId(user.getUserId())
//    .refreshToken(refreshTokenJwt)
//    .User(user)
//    .build();
//    refreshTokenRepository.save(newRefreshToken);
//    } else {
//    RefreshToken newRefreshToken = oldRefreshToken.get().toBuilder()
//    .refreshToken(refreshTokenJwt)
//    .build();
//    refreshTokenRepository.save(newRefreshToken);
//    }
//
//    return SignInResponse.builder()
//    .userId(user.getUserId())
//    .userName(user.getUserName())
//    .msg("카카오 로그인 성공")
//    .accessToken(accessTokenJwt)
//    .refreshToken(refreshTokenJwt)
//    .kakaoAccessToken(accessToken)
//    .build();
//    }
}
