package aladma.alamdaspring.controller;

import aladma.alamdaspring.domain.RefreshToken;
import aladma.alamdaspring.domain.User;
import aladma.alamdaspring.dto.AddUserRequest;
import aladma.alamdaspring.dto.AddUserResponse;
import aladma.alamdaspring.dto.LoginResponse;
import aladma.alamdaspring.service.RefreshTokenService;
import aladma.alamdaspring.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/users")
    public AddUserResponse saveUser(@RequestBody AddUserRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();

        Long id = userService.save(user);

        return new AddUserResponse(id);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<LoginResponse> successLoginUser(@PathVariable("id") Long id) {

        User user = userService.findById(id);

        RefreshToken refreshToken = refreshTokenService.findByUserId(id);

        return ResponseEntity.ok()
                .body(new LoginResponse(user.getId(), user.getEmail(), user.getNickname(), refreshToken.getRefreshToken()));
    }

    @GetMapping("/api/users")
    public Result findUsers() {
        List<User> findUsers = userService.findUsers();

        List<UserDto> collect = findUsers.stream()
                .map(m -> new UserDto(m.getEmail()))
                .collect(Collectors.toList());

        return new Result<>(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class UserDto {
        private String email;
    }
}