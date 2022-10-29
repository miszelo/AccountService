package account.services;

import account.exceptions.NotLoggedInException;
import account.exceptions.PasswordsMustBeDifferentException;
import account.exceptions.UserExistException;
import account.exceptions.UserNotFoundException;
import account.model.User;
import account.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<User> registerAccount(User user) {
        System.out.println("TEST");
        if (userRepository.existsUserByEmailIgnoreCase(user.getEmail())) {
            throw new UserExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<Map<String,String>> changePassword(UserDetails userDetails, String newPassword) {
        if (userDetails == null) {
            throw new NotLoggedInException();
        }
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordsMustBeDifferentException();
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("email", user.getEmail().toLowerCase(), "status", "The password has been updated successfully"));
    }
}
