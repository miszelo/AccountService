package account.services;

import account.model.User;
import account.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final UserRepository userRepository;

    public EmployeeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseEntity<User> getUserInfo(UserDetails userDetails) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        return ResponseEntity.of(user);
    }
}
