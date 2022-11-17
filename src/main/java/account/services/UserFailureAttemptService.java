package account.services;

import account.model.record.Action;
import account.model.user.Role;
import account.model.user.User;
import account.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFailureAttemptService {
    public static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final AuditorService auditorService;
    private final HttpServletRequest request;

    public void increaseFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        user.setFailedAttempt(newFailAttempts);
        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        auditorService.addEvent(Action.LOCK_USER, user.getEmail(), "Lock user " + user.getEmail());
        userRepository.save(user);
    }

    public void registerFailedLoginAttempt(String email) {
        Optional<User> foundUser = userRepository.findByEmailIgnoreCase(email);
        if (foundUser.isEmpty()) {
            return;
        }
        User user = foundUser.get();
        increaseFailedAttempts(user);
        if (user.getFailedAttempt() >= MAX_FAILED_ATTEMPTS) {
            auditorService.addEvent(Action.BRUTE_FORCE, user.getEmail(), request.getRequestURI());
            if (user.getRoles().stream()
                    .anyMatch(role -> !Objects.equals(Role.ROLE_ADMINISTRATOR, role))){
                lock(user);
            }
        }
        userRepository.save(user);
    }

}
