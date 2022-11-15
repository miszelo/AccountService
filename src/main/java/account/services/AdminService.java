package account.services;

import account.exceptions.UserNotFoundException;
import account.mapper.UserMapper;
import account.model.dto.ChangeUserRoleDTO;
import account.model.dto.DeleteUserResponseDTO;
import account.model.dto.UserInfoDTO;
import account.model.user.Role;
import account.model.user.User;
import account.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public ResponseEntity<List<UserInfoDTO>> informationAboutEmployee() {
        return ResponseEntity.ok(userRepository.findAll()
                .stream()
                .map(userMapper::mapUserToUserInfoDTO)
                .collect(Collectors.toList()));
    }

    public ResponseEntity<DeleteUserResponseDTO> deleteUser(String userEmail) {
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        if (user.getRoles().contains(Role.ROLE_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        return ResponseEntity.ok(new DeleteUserResponseDTO(userEmail));
    }

    public ResponseEntity<UserInfoDTO> changeUserRole(ChangeUserRoleDTO changeUserRoleDTO) {
        User user = userRepository.findByEmailIgnoreCase(changeUserRoleDTO.getUser())
                .orElseThrow(UserNotFoundException::new);

        switch (changeUserRoleDTO.getOperation()) {
            case GRANT -> grantRole(user, changeUserRoleDTO.getRole());
            case REMOVE -> removeRole(user, changeUserRoleDTO.getRole());
            default -> throw new IllegalStateException("Unexpected value: " + changeUserRoleDTO.getOperation());
        }

        return ResponseEntity.ok(userMapper.mapUserToUserInfoDTO(user));
    }

    private void grantRole(User user, Role role) {
        if (user.getRoles().stream()
                .anyMatch(r -> Objects.equals(Role.ROLE_ADMINISTRATOR, r))
                && user.getRoles().stream()
                .anyMatch(r -> List.of(Role.ROLE_ACCOUNTANT, Role.ROLE_USER).contains(r))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
        }
        List<Role> newRoles = user.getRoles();
        newRoles.add(role);
        user.setRoles(newRoles);
        userRepository.save(user);
    }

    private void removeRole(User user, Role role) {
        if (role == Role.ROLE_ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        if (!Arrays.asList(Role.values()).contains(role)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found!");
        }
        if (user.getRoles().size() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }
        userRepository.save(user);
    }

}
