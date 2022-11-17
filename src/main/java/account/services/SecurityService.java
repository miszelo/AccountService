package account.services;

import account.model.security.SecurityEvent;
import account.repositories.SecurityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuditorService auditorService;

    private final SecurityEventRepository securityEventRepository;

    public ResponseEntity<List<SecurityEvent>> getEvents() {
        return ResponseEntity.ok(securityEventRepository.findByOrderByIdAsc());
    }
}
