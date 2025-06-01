package org.project.exchange.model.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.project.exchange.config.TokenProvider;
import org.project.exchange.model.auth.repository.AuthRepository;
import org.project.exchange.model.auth.repository.PermissionRepository;
import org.project.exchange.model.auth.repository.SystemLogRepository;
import org.project.exchange.model.auth.service.EmailService;
import org.project.exchange.model.auth.service.PermissionService;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.repository.CurrencyRepository;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.repository.ListsRepository;
import org.project.exchange.model.product.repository.ProductRepository;
import org.project.exchange.model.user.Dto.ResetNameResponse;
import org.project.exchange.model.user.Dto.SignInRequest;
import org.project.exchange.model.user.Dto.SignInResponse;
import org.project.exchange.model.user.Dto.SignUpRequest;
import org.project.exchange.model.user.Dto.SignUpResponse;
import org.project.exchange.model.user.Dto.TokenResponse;
import org.project.exchange.model.user.Dto.UpdateUserInfoRequest;
import org.project.exchange.model.user.Dto.UserInfoResponse;
import org.project.exchange.model.user.GoogleUser;
import org.project.exchange.model.user.KakaoUser;
import org.project.exchange.model.user.RefreshToken;
import org.project.exchange.model.user.User;
import org.project.exchange.model.user.repository.GoogleUserRepository;
import org.project.exchange.model.user.repository.KakaoUserRepository;
import org.project.exchange.model.user.repository.RefreshTokenRepository;
import org.project.exchange.model.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j; 

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final PermissionRepository permissionRepository;
    private final SystemLogRepository systemLogRepository;
    private final ListsRepository listsRepository;
    private final ProductRepository productRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final PermissionService permissionService; 
    private final EmailService emailService; 
    private final KakaoService kakaoService;
    private final Random random = new Random();
    private final CurrencyRepository currencyRepository;
    private final GoogleUserRepository googleUserRepository;
    private final GoogleOAuthService googleOAuthService;



    // 📌 비밀번호 패턴 (영문, 숫자, 특수문자 포함, 8~16자)
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,16}$";
    
    @Transactional
    public void sendOtpToEmail(String email) {
        emailService.setEmail(email);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest request, String otp, List<Boolean> agreedTerms) {
        String normalizedEmail = request.getUserEmail().trim().toLowerCase(Locale.getDefault());

        // 이메일 중복 체크
        if (userRepository.existsByUserEmail(normalizedEmail)) {
            return SignUpResponse.builder()
                    .msg("이메일이 이미 존재합니다.")
                    .build();
        }

        // 이메일 인증 코드 확인
        if (!emailService.checkAuthNumber(normalizedEmail, otp)) {
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

        // 기본 통화 조회 및 검증
        Optional<Currency> currencyOpt = currencyRepository.findById(request.getDefaultCurrencyId());
        if (currencyOpt.isEmpty()) {
            return SignUpResponse.builder()
                    .msg("기본 통화 ID가 유효하지 않습니다.")
                    .build();
        }
        Currency defaultCurrency = currencyOpt.get();

        // 사용자 생성
        User user = User.builder()
                .userName(request.getUserName())
                .userDateOfBirth(request.getUserDateOfBirth())
                .userGender(request.isUserGender())
                .userEmail(normalizedEmail)
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
                .userCreatedAt(new Date(System.currentTimeMillis()))
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .defaultCurrency(defaultCurrency) 
                .build();

        userRepository.save(user);

        // 필수 약관 동의 확인
        if (!permissionService.hasAgreedToRequiredTerms(user)) {
            return SignUpResponse.builder()
                    .msg("필수 약관에 동의해야 회원가입이 가능합니다.")
                    .build();
        }
        // 약관 동의 저장
        permissionService.saveAgreedTerms(user, agreedTerms);

        return SignUpResponse.builder()
                .msg("회원가입 성공")
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userGender(user.isUserGender())
                .userDateOfBirth(user.getUserDateOfBirth().toString())
                .defaultCurrencyId(user.getDefaultCurrency().getCurrencyId()) // 기본 통화 정보 추가
                .build();
    }

    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        if (request == null || request.getUserEmail() == null || request.getUserPassword() == null) {
            return SignInResponse.builder()
                    .msg("이메일 또는 비밀번호가 제공되지 않았습니다.")
                    .build();
        }

        String normalizedEmail = request.getUserEmail().trim().toLowerCase(Locale.getDefault());
        User user = userRepository.findByUserEmail(normalizedEmail);

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


        String accessToken = tokenProvider.createToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        refreshTokenRepository.save(
                refreshTokenRepository.findById(user.getUserId())
                        .map(existingToken -> existingToken.toBuilder().refreshToken(refreshToken).build())
                        .orElseGet(() -> RefreshToken.builder()
                                .tokenId(user.getUserId())
                                .refreshToken(refreshToken)
                                .User(user)
                                .build()));

        return SignInResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .msg("로그인 성공")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    
    @Transactional
    public TokenResponse refreshToken(String refreshToken, String oldAccessToken) throws JsonProcessingException {
        tokenProvider.validateRefreshToken(refreshToken, oldAccessToken);

        String subject = tokenProvider.decodeJwtPayloadSubject(oldAccessToken);
        long userId = Long.parseLong(subject.split(":")[0]);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다. id=" + userId));

        String newAccess = tokenProvider.recreateAccessToken(oldAccessToken);
        String newRefresh = tokenProvider.createRefreshToken(user);

        RefreshToken entity = RefreshToken.builder()
                .tokenId(userId)
                .User(user)
                .refreshToken(newRefresh)
                .build();
        refreshTokenRepository.save(entity);

        return new TokenResponse(newAccess, newRefresh, null);
    }

    @Transactional
    public String signOut(String token) throws JsonProcessingException {
        String username = tokenProvider.validateTokenAndGetSubject(token).toLowerCase(Locale.getDefault());

        Optional<User> userOptional = userRepository.findByUserEmailOptional(username);
        if (userOptional.isEmpty() || refreshTokenRepository.findById(userOptional.get().getUserId()).isEmpty()) {
            return "로그아웃 실패";
        }

        try {
            refreshTokenRepository.deleteById(userOptional.get().getUserId());
        } catch (Exception e) {
            return "로그아웃 실패";
        }

        return "로그아웃 성공";
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,16}$";
        return Pattern.matches(passwordPattern, password);
    }

    // 카카오 로그인
    @Transactional
    public SignInResponse kakaoSignIn(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("카카오 액세스 토큰이 유효하지 않습니다.");
        }

        log.info("Received Kakao access token: {}", accessToken);

        String kakaoId = kakaoService.extractKakaoId(accessToken);
        boolean isFirstKakao = kakaoUserRepository.findByKakaoId(kakaoId).isEmpty();
        KakaoUser kakaoUser = kakaoService.saveOrUpdateKakaoUser(accessToken);

        if (kakaoUser == null) {
            throw new RuntimeException("카카오 사용자 정보가 없습니다.");
        }

        User user = kakaoUser.getUser();
        if (user.getDefaultCurrency() == null) {
            Currency defaultCurrency = currencyRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("기본 통화 정보가 없습니다."));
            user = user.toBuilder()
                    .defaultCurrency(defaultCurrency)
                    .build();
            userRepository.save(user);
        }
        if (user == null) {
            throw new RuntimeException("해당 카카오 사용자에 대한 유저 정보가 없습니다.");
        }

        log.info("User associated with Kakao user: {}", user);

        String jwtAccessToken = tokenProvider.createToken(user);
        String jwtRefreshToken = tokenProvider.createRefreshToken(user);

        refreshTokenRepository.save(
                new RefreshToken(user.getUserId(), user, jwtRefreshToken));

        return SignInResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .msg("카카오 로그인 성공")
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .firstSocialLogin(isFirstKakao)
                .socialProvider("kakao")
                .build();
    }

    // 아이디(이메일)찾기 - 이름, 생년월일로
    @Transactional
    public String findId(String userName, LocalDate userDateOfBirth) {
        User user = userRepository.findByUserNameAndUserDateOfBirth(userName, userDateOfBirth);
        if (user == null) {
            return "일치하는 사용자 정보가 없습니다.";
        }
        return user.getUserEmail();
    }

    // 비밀번호 찾기 (임시 비밀번호 발급)
    @Transactional
    public String findPassword(String userEmail, String userName) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null || !user.getUserName().equals(userName)) {
            return "일치하는 사용자 정보가 없습니다.";
        }

        // 임시 비밀번호 생성
        String tempPassword = generateValidRandomPassword();
        user = user.toBuilder()
                .userPassword(passwordEncoder.encode(tempPassword))
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(user);

        // 임시 비밀번호 이메일 전송
        emailService.sendTemporaryPassword(userEmail, tempPassword);

        return "임시 비밀번호가 이메일로 전송되었습니다.";
    }

    // 비밀번호 재설정 - 이메일로유저 정보 확인 후, 현재 비밀번호가 일치하는지 확인, 새 비밀번호 설정
    @Transactional
    public String resetPassword(String userEmail, String currentPassword, String newPassword) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return "일치하는 사용자 정보가 없습니다.";
        }

        if (!passwordEncoder.matches(currentPassword, user.getUserPassword())) {
            return "현재 비밀번호가 일치하지 않습니다.";
        }

        // 비밀번호 형식 확인
        if (!isValidPassword(newPassword)) {
            return "비밀번호 형식이 올바르지 않습니다. 비밀번호는 8자 이상 16자 이하, 문자, 숫자, 특수문자를 포함해야 합니다.";
        }

        user = user.toBuilder()
                .userPassword(passwordEncoder.encode(newPassword))
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(user);

        return "비밀번호가 성공적으로 변경되었습니다.";
    }


    //  랜덤 비밀번호 생성 (비밀번호 규칙 적용)
    private String generateValidRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password;
        do {
            password = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                password.append(characters.charAt(random.nextInt(characters.length())));
            }
        } while (!isValidPassword(password.toString())); // 규칙 만족할 때까지 반복
        return password.toString();
    }

    // 이름 재설정
    @Transactional
    public ResetNameResponse resetName(String userEmail, String newName) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResetNameResponse.builder()
                    .msg("일치하는 사용자 정보가 없습니다.")
                    .build();
        }

        user = user.toBuilder()
                .userName(newName)
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(user);

        return ResetNameResponse.builder()
                .userEmail(userEmail)
                .msg("이름이 성공적으로 변경되었습니다.")
                .userName(newName)
                .build();
    }

    @Transactional
    public UserInfoResponse updateUserInfo(UpdateUserInfoRequest req) {
        User before = userRepository.findByUserEmail(req.getUserEmail());
        if (before == null)
            throw new RuntimeException("사용자를 찾을 수 없습니다.");

        User.UserBuilder builder = before.toBuilder();

        boolean changed = false;
        if (req.getUserName() != null) {
            builder.userName(req.getUserName());
            changed = true;
        }
        if (req.getUserDateOfBirth() != null) {
            Date dob = Date.valueOf(LocalDate.parse(req.getUserDateOfBirth()));
            builder.userDateOfBirth(dob);
            changed = true;
        }
        if (req.getUserPassword() != null) {
            builder.userPassword(passwordEncoder.encode(req.getUserPassword()));
            changed = true;
        }
        if (req.getDefaultCurrencyId() != null) {
            Currency c = currencyRepository.findById(req.getDefaultCurrencyId())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 통화 ID"));
            builder.defaultCurrency(c);
            changed = true;
        }
        if (!changed) {
            log.info("ℹ️ 변경된 항목 없음");
        }

        User updated = builder
                .userUpdatedAt(new Date(System.currentTimeMillis()))
                .build();

        userRepository.save(updated);

        return UserInfoResponse.builder()
                .userId(updated.getUserId())
                .userEmail(updated.getUserEmail())
                .userName(updated.getUserName())
                .userDateOfBirth(updated.getUserDateOfBirth().toLocalDate().toString())
                .isKakaoUser(kakaoUserRepository.findByUser(updated).isPresent())
                .isGoogleUser(updated.getUserEmail().contains("@gmail.com"))
                .defaultCurrencyId(updated.getDefaultCurrency().getCurrencyId())
                .build();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfoFromToken(String accessToken) {
        String subject = tokenProvider.validateTokenAndGetSubject(accessToken);
        log.info("🔑 Token subject: {}", subject);

        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("토큰 subject 형식이 올바르지 않습니다.");
        }

        String userEmail = parts[1]; // 이메일만 사용
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        String formattedDate = user.getUserDateOfBirth()
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .userDateOfBirth(formattedDate)
                .isKakaoUser(kakaoUserRepository.findByUser(user).isPresent())  
                .isGoogleUser(user.getUserEmail().contains("@gmail.com")) // 구글 이메일인지 확인
                .defaultCurrencyId(user.getDefaultCurrency().getCurrencyId()) // 기본 통화 정보 추가  
                .build();
    }
    @Transactional(readOnly = true)
    public Long getUserCurrency(String accessToken) {
        String subject = tokenProvider.validateTokenAndGetSubject(accessToken);
        log.info("🔑 Token subject: {}", subject);

        // 토큰 subject에서 이메일만 추출 (형식: userId:userEmail)
        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("토큰 subject 형식이 올바르지 않습니다.");
        }

        String userEmail = parts[1]; // 이메일만 사용
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
        return user.getDefaultCurrency().getCurrencyId();
    }

    @Transactional
    public String deleteUser(String token, String password) {
        String subject = tokenProvider.validateTokenAndGetSubject(token);
        String userEmail = subject.split(":")[1];

        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        refreshTokenRepository.deleteById(user.getUserId());

        kakaoUserRepository.findByUser(user).ifPresent(kakaoUserRepository::delete);
        googleUserRepository.findByUser(user).ifPresent(googleUserRepository::delete);        

        authRepository.deleteAllByUser(user);

        permissionRepository.deleteAllByUser(user);

        systemLogRepository.deleteAllByUser(user);

        List<Lists> userLists = listsRepository.findAllByUser(user);
        for (Lists list : userLists) {
            productRepository.deleteAllByLists(list); // 연결된 상품 먼저 제거
            listsRepository.delete(list);
        }

        userRepository.delete(user);

        return "회원 탈퇴 성공";
    }

    @Transactional
    public void deleteKakaoUser(String token) {
        try {
            String subject = tokenProvider.decodeJwtPayloadSubject(token); // "userId:userEmail"
            String userEmail = subject.split(":")[1]; // 이메일 추출

            User user = userRepository.findByUserEmail(userEmail);
            if (user == null) {
                throw new RuntimeException("사용자를 찾을 수 없습니다.");
            }

            Optional<KakaoUser> optionalKakaoUser = kakaoUserRepository.findByUser(user);
            if (optionalKakaoUser.isEmpty()) {
                log.warn("⚠️ 카카오 유저 정보가 없습니다.");
                return;
            }

            KakaoUser kakaoUser = optionalKakaoUser.get();

            if (kakaoUser.getAccessToken() != null && !kakaoUser.getAccessToken().isEmpty()) {
                kakaoService.unlink(kakaoUser.getAccessToken());
            }

            // 삭제 순서
            kakaoUserRepository.delete(kakaoUser);
            authRepository.deleteAllByUser(user);
            permissionRepository.deleteAllByUser(user);
            systemLogRepository.deleteAllByUser(user);

            List<Lists> userLists = listsRepository.findAllByUser(user);
            for (Lists list : userLists) {
                productRepository.deleteAllByLists(list);
                listsRepository.delete(list);
            }

            refreshTokenRepository.deleteById(user.getUserId());
            userRepository.delete(user);

            log.info("카카오 회원 탈퇴 성공");

        } catch (JsonProcessingException e) {
            log.error("JWT subject 디코딩 실패", e);
            throw new RuntimeException("토큰 파싱 실패");
        }
    }
    
    @Transactional
    public SignInResponse googleSignInWithAuthCode(String authCode) {
        TokenResponse tokenResp = googleOAuthService.exchangeAuthCode(authCode);
        String idToken = tokenResp.getIdToken();
        String refreshToken = tokenResp.getRefreshToken();
        
        if (idToken == null || refreshToken == null) {
            throw new RuntimeException("구글 토큰 교환 실패");
        }

        Map<String, Object> info = googleOAuthService.decodeIdToken(idToken);
        String email = (String) info.get("email");
        String name = (String) info.get("name");

        if (email == null || name == null) {
            throw new RuntimeException("구글 사용자 정보가 부족합니다.");
        }

        boolean isFirstGoogle = userRepository.findByUserEmail(email) == null;

        User user = userRepository.findByUserEmail(email);
        if (user == null) {
            // 기본 통화 조회 (예: 가장 먼저 등록된 것)
            Currency defaultCurrency = currencyRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("기본 통화 정보가 없습니다."));

            user = User.builder()
                    .userEmail(email)
                    .userName(name)
                    .userGender(true)
                    .userDateOfBirth(Date.valueOf(LocalDate.of(2000, 1, 1)))
                    .userPassword(UUID.randomUUID().toString())
                    .defaultCurrency(defaultCurrency) 
                    .userCreatedAt(new Date(System.currentTimeMillis()))
                    .userUpdatedAt(new Date(System.currentTimeMillis()))
                    .build();
            userRepository.save(user);
        }

        if (!googleUserRepository.findByUser(user).isPresent()) {
            GoogleUser gu = GoogleUser.builder()
                    .user(user)
                    .refreshToken(refreshToken)
                    .build();
            googleUserRepository.save(gu);
        }

        String jwtAccess = tokenProvider.createToken(user);
        String jwtRefresh = tokenProvider.createRefreshToken(user);
        refreshTokenRepository.save(new RefreshToken(user.getUserId(), user, jwtRefresh));

        return SignInResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .accessToken(jwtAccess)
                .refreshToken(jwtRefresh)
                .msg("구글 로그인 성공")
                .firstSocialLogin(isFirstGoogle)
                .socialProvider("google")
                .build();
    }

    @Transactional
    public void deleteGoogleUser(String accessJwt) {
        String email;
        try {
            email = tokenProvider.extractUserEmail(accessJwt);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("토큰에서 이메일 추출 실패", e);
        }

        // 사용자·GoogleUser 조회
        User user = userRepository.findByUserEmail(email);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        GoogleUser gu = googleUserRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("구글 사용자 정보가 없습니다."));

        // 리프레시 토큰 강제 만료(revoke) 호출
        googleOAuthService.revokeToken(gu.getRefreshToken());

        // DB에서 순차 삭제
        googleUserRepository.delete(gu);
        refreshTokenRepository.deleteById(user.getUserId());
        userRepository.delete(user);

        log.info("✅ 구글 회원 탈퇴 성공: {}", email);
    }
    
    public boolean isGoogleUserByEmail(String userEmail) {
        return googleUserRepository.existsByUserUserEmail(userEmail);
    }    
}