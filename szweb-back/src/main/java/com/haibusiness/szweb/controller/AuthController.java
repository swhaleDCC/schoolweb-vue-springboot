package com.haibusiness.szweb.controller;

import com.haibusiness.szweb.entity.User;
import com.haibusiness.szweb.exception.BadRequestException;
import com.haibusiness.szweb.security.JWTUtil;
import com.haibusiness.szweb.security.PBKDF2Encoder;
import com.haibusiness.szweb.security.SecurityContextHolder;
import com.haibusiness.szweb.security.model.AuthRequest;
import com.haibusiness.szweb.security.model.AuthResponse;
import com.haibusiness.szweb.service.AuthorityService;
import com.haibusiness.szweb.service.UserService;
import com.haibusiness.szweb.vo.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/auth")
public class AuthController {
    private final PBKDF2Encoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private final JWTUtil jwtUtil;


    public AuthController(PBKDF2Encoder passwordEncoder, @Qualifier("userServiceImpl") UserDetailsService userDetailsService, AuthorityService authorityService, JWTUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody AuthRequest authRequest) {
        final UserDetails user = userDetailsService.loadUserByUsername(authRequest.getUsername());
        if (user == null) {
            throw new AccountExpiredException("用户不存在错误");
        }
        if (!passwordEncoder.matches(authRequest.getPassword(), (user.getPassword()))) {
            throw new AccountExpiredException("密码错误");
        }
        if (!user.isEnabled()) {
            throw new AccountExpiredException("账号没激活，请联系管理员");
        }
        // 生成令牌
        final String token = jwtUtil.generateToken(user);
        // 返回 token
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping(value = "/info")
    public ResponseEntity getUserInfo() {
        try {
            UserDetails userDetails = SecurityContextHolder.getUserDetails();
            User user = (User) userDetailsService.loadUserByUsername(userDetails.getUsername());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        UserService userService = (UserService) userDetailsService;
        if(userDetailsService.loadUserByUsername(user.getUsername())!=null) {
            throw new BadRequestException("用户名已存在");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false);
        userService.saveUser(user);

        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

}
