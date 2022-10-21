package account.services;

import account.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResponseEntity<User> registerAccount(User user) {
        return ResponseEntity.ok(user);
    }
}