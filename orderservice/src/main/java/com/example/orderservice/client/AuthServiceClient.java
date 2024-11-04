package com.example.orderservice.client;

import com.example.orderservice.model.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "http://localhost:8081")
public interface AuthServiceClient {
    @GetMapping("/api/auth/me")
    UserResponse getUserDetails(@RequestParam("username") String username, @RequestHeader("Authorization") String token);
}
