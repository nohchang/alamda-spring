package aladma.alamdaspring.config.oauth;

import aladma.alamdaspring.config.jwt.TokenProvider;
import aladma.alamdaspring.domain.RefreshToken;
import aladma.alamdaspring.domain.User;
import aladma.alamdaspring.dto.LoginResponse;
import aladma.alamdaspring.repository.RefreshTokenRepository;
import aladma.alamdaspring.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    private static final String REDIRECT_PATH = "/signup";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userService.findByEmail((String) oAuth2User.getAttributes().get("email"));

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);

//        String fullAccessToken = "Bearer " + accessToken;
//        response.setHeader("Authorization", fullAccessToken);

        String jsonResponse = new ObjectMapper().writeValueAsString(new LoginResponse(user.getId(), user.getEmail(), user.getNickname(), refreshToken));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);

//        String targetUrl = getTargetUrl(accessToken);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//        getRedirectStrategy().sendRedirect(request, response, "/api/users/" + user.getId());
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}
