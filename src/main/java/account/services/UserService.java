package account.services;

import account.mapper.UserMapper;
import account.exceptions.NotLoggedInException;
import account.exceptions.PasswordsMustBeDifferentException;
import account.exceptions.UserExistException;
import account.exceptions.UserNotFoundException;
import account.model.dto.UserInfoDTO;
import account.model.user.Role;
import account.model.user.User;
import account.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<UserInfoDTO> registerAccount(User user) {
        if (userRepository.existsUserByEmailIgnoreCase(user.getEmail())) {
            throw new UserExistException();
        }

        user.getRoles().add(userRepository.findAll().isEmpty() ? Role.ROLE_ADMINISTRATOR : Role.ROLE_USER);
        user.setEmail(user.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        UserInfoDTO userInfoDTO = userMapper.mapUserToUserInfoDTO(user);
        return ResponseEntity.ok(userInfoDTO);
    }

    public ResponseEntity<Map<String, String>> changePassword(UserDetails userDetails, String newPassword) {
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
