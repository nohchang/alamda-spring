package aladma.alamdaspring.controller;

import aladma.alamdaspring.domain.User;
import aladma.alamdaspring.dto.AddUserRequest;
import aladma.alamdaspring.dto.AddUserResponse;
import aladma.alamdaspring.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

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

    @GetMapping("/api/users")
    public Result findUsers() {
        List<User> findUsers = userService.findUsers();

        List<UserDto> collect = findUsers.stream()
                .map(m -> new UserDto(m.getEmail()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
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