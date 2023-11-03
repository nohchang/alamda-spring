package aladma.alamdaspring.controller;

import aladma.alamdaspring.domain.User;
import aladma.alamdaspring.dto.AddUserRequest;
import aladma.alamdaspring.dto.AddUserResponse;
import aladma.alamdaspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
