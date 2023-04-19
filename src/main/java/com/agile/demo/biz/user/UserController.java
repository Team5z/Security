package com.agile.demo.biz.user;

import com.agile.demo.jwt.JwtAuthenticationToken;
import com.agile.demo.jwt.JwtUtilToken;
import com.agile.demo.repository.service.RefreshTokenService;
import com.agile.demo.repository.service.UserService;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user")
public class UserController {

    private final JwtToken jwtToken;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public UserController(JwtAuthenticationToken jwtAuthenticationToken, UserService userService, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder) {
        this.jwtToken = jwtToken;
        this.UserService = userService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
    }

//    @PostMapping("/signup")
//    public ResponseEntity signup(@RequestBody @Validated UserSignupDto memberSignupDto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return new ResponseEntity(HttpStatus.BAD_REQUEST);
//        }
//        User user = new User();
////        user.setName(memberSignupDto.getName());
//        user.setEmail(userSignupDto.getEmail());
//        user.setPassword(passwordEncoder.encode(memberSignupDto.getPassword()));
//
//        User saveMember = userService.addUser(user);
//
//        UserSignupResponseDto memberSignupResponse = new UserSignupResponseDto();
//        userSignupResponse.setMemberId(saveUser.getUsername());
//        userSignupResponse.setName(saveMember.getName());
//        userSignupResponse.setRegdate(saveMember.getRegdate());
//        userSignupResponse.setEmail(saveMember.getEmail());

        // 회원가입
//        return new ResponseEntity(userSignupResponse, HttpStatus.CREATED);
//    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated UserLoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // email이 없을 경우 Exception이 발생한다. Global Exception에 대한 처리가 필요하다.
        User user = userService.findByUsername(loginDto.getEmail());
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        // List<Role> ===> List<String>
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        // JWT토큰을 생성
        String accessToken = jwtToken.createAccessToken(user.userId(), user.getEmail(), roles);
        String refreshToken = jwtToken.createRefreshToken(user.getuserId(), user.getEmail(), roles);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setUserId(user.getuserId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        UserLoginResponseDto loginResponse = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .build();
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }

    /*
    1. 전달받은 유저의 아이디로 유저가 존재하는지 확인한다.
    2. RefreshToken이 유효한지 체크한다.
    3. AccessToken을 발급하여 기존 RefreshToken과 함께 응답한다.
     */
    @PostMapping("/refreshToken")
    public ResponseEntity requestRefresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenDto.getRefreshToken()).orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        Claims claims = jwtToken.parseRefreshToken(refreshToken.getValue());

        Long userId = Long.valueOf((Integer) claims.get("userId"));

        User user = UserService.getMember(userId).orElseThrow(() -> new IllegalArgumentException("Member not found"));


        List roles = (List) claims.get("roles");
        String email = claims.getSubject();

        String accessToken = jwtToken.createAccessToken(userId, email, roles);

        UserLoginResponseDto loginResponse = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenDto.getRefreshToken())
                .memberId(user.getUserrId())
                .build();
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }
}
