package account.services;

import account.model.record.Action;
import account.model.security.SecurityEvent;
import account.repositories.SecurityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuditorService {
    private final SecurityEventRepository securityEventRepository;
    private final HttpServletRequest request;

    public void addEvent(Action action, String object) {
        String subject = getCurrentUser();
        SecurityEvent securityEvent = getSecurityEvent(action, subject, object);
        securityEventRepository.save(securityEvent);
    }

    public void addEvent(Action action, String subject, String object) {
        SecurityEvent securityEvent = getSecurityEvent(action, subject, object);
        securityEventRepository.save(securityEvent);
    }

    private SecurityEvent getSecurityEvent(Action action, String subject, String object) {
        return SecurityEvent.builder()
                .action(action)
                .subject(subject)
                .object(object)
                .path(request.getRequestURI())
                .build();
    }

    private String getCurrentUser() {
        Principal principal = request.getUserPrincipal();
        return principal == null ? "Anonymous" : principal.getName();
    }
}