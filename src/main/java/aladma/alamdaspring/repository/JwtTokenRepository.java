package aladma.alamdaspring.repository;

import aladma.alamdaspring.domain.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByUserId(Long userId);
    Optional<JwtToken> findByRefreshToken(String refreshToken);
}
