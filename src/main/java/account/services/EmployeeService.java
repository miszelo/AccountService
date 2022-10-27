package account.services;

import account.entity.User;
import account.repositories.UserRepository;
import account.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final UserRepository userRepository;

    public EmployeeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseEntity<User> getUserInfo(UserDetailsImpl userDetails) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        //System.out.println(user);
        return ResponseEntity.of(user);
    }
}
