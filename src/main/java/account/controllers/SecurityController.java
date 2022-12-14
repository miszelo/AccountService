package account.controllers;

import account.model.security.SecurityEvent;
import account.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_AUDITOR')")
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityService securityService;

    @GetMapping("/events")
    public ResponseEntity<List<SecurityEvent>> getEvents() {
        return securityService.getEvents();
    }
}
