package account.security.login;

import account.model.record.Action;
import account.services.AuditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuditorService auditService;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        auditService.addEvent(Action.ACCESS_DENIED, request.getUserPrincipal().getName(), request.getRequestURI());
        response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied!");
    }
}