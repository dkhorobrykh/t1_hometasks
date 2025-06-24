package ru.t1.school.second_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1.school.second_service.service.CustomUserDetailsService;
import ru.t1.school.second_service.util.JwtUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("token")
    @ResponseStatus(HttpStatus.OK)
    public String generateToken(@RequestParam("userId") Long userId) {
        var userDetails = customUserDetailsService.loadUserByUsername(userId.toString());
        return jwtUtils.generateJwtToken(userDetails);
    }
}
