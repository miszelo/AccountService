package account.security.login;

import account.model.record.Action;
import account.security.UserDetailsImpl;
import account.services.AuditorService;
import account.services.UserFailureAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthenticationEvents {

    private final UserFailureAttemptService userFailureAttemptService;
    private final AuditorService auditService;
    private final HttpServletRequest request;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        UserDetailsImpl user = (UserDetailsImpl) event.getAuthentication().getPrincipal();
        userFailureAttemptService.resetFailedAttempts(user.getUsername());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();
        String path = request.getRequestURI();
        auditService.addEvent(Action.LOGIN_FAILED, email, path);
        userFailureAttemptService.registerFailedLoginAttempt(email);
    }
}