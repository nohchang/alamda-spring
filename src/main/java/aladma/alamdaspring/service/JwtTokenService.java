package aladma.alamdaspring.service;

import aladma.alamdaspring.domain.JwtToken;
import aladma.alamdaspring.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;

    public JwtToken findByRefreshToken(String refreshToken) {
        return jwtTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
    }

    public JwtToken findByUserId(Long userId) {
        return jwtTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));
    }
}
