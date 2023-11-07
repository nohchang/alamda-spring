package aladma.alamdaspring.service;

import aladma.alamdaspring.domain.User;
import aladma.alamdaspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(User user) {

        validateDuplicateUser(user);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User.builder().password(encoder.encode(user.getPassword()));

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        Optional<User> findUsers = userRepository.findByEmail(user.getEmail());
        if (findUsers.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
