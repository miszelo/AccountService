package account.services;

import account.exceptions.UserNotFoundException;
import account.mapper.UserMapper;
import account.model.dto.ChangeAccessForUserDTO;
import account.model.dto.ChangeUserRoleDTO;
import account.model.dto.DeleteUserResponseDTO;
import account.model.dto.UserInfoDTO;
import account.model.record.Action;
import account.model.record.ResponseStatus;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminService {

    private final AuditorService auditorService;
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
        auditorService.addEvent(Action.DELETE_USER, user.getEmail());
        return ResponseEntity.ok(new DeleteUserResponseDTO(userEmail));
    }

    public ResponseEntity<UserInfoDTO> changeUserRole(ChangeUserRoleDTO changeUserRoleDTO) {
        User user = userRepository.findByEmailIgnoreCase(changeUserRoleDTO.getUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        changeUserRoleDTO.setRole("ROLE_" + changeUserRoleDTO.getRole());

        if (Arrays.stream(Role.values())
                .noneMatch(role ->
                        role.toString().equals(changeUserRoleDTO.getRole()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!");
        }

        switch (changeUserRoleDTO.getOperation()) {
            case GRANT -> grantRole(user, changeUserRoleDTO.getRole());
            case REMOVE -> removeRole(user, changeUserRoleDTO.getRole());
            default -> throw new IllegalStateException("Unexpected value: " + changeUserRoleDTO.getOperation());
        }

        userRepository.save(user);
        return ResponseEntity.ok(userMapper.mapUserToUserInfoDTO(user));
    }

    public ResponseEntity<ResponseStatus> changeUserAccess(ChangeAccessForUserDTO changeAccessForUserDTO) {
        User user = userRepository.findByEmailIgnoreCase(changeAccessForUserDTO.getUser())
                .orElseThrow(UserNotFoundException::new);
        switch (changeAccessForUserDTO.getOperation()) {
            case LOCK -> lockUserAccount(user);
            case UNLOCK -> unlockUserAccount(user);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operation");
        }
        return ResponseEntity.ok(
                new ResponseStatus(
                        "User " + user.getEmail() + " " + changeAccessForUserDTO.getOperation().withSuffix() + "!"));
    }

    private void grantRole(User user, String role) {
        if (isUserBusiness(user) && role.equals(Role.ROLE_ADMINISTRATOR.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }
        if (isUserAdmin(user) && (role.equals(Role.ROLE_USER.name()) || role.equals(Role.ROLE_ACCOUNTANT.name()) || role.equals(Role.ROLE_AUDITOR.name()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user cannot combine administrative and business roles!");
        }
        user.grantAuthority(Role.valueOf(role));
        auditorService.addEvent(Action.GRANT_ROLE, userRepository.findById(1L).get().getEmail(), "Grant role " + Role.valueOf(role).withoutROLE_() + " to " + user.getEmail());
    }
    private void removeRole(User user, String role) {
        if (!user.getRoles().contains(Role.valueOf(role))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
        }
        if (role.equals(Role.ROLE_ADMINISTRATOR.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }
        if (user.getRoles().size() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
        }
        user.removeAuthority(Role.valueOf(role));
        auditorService.addEvent(Action.REMOVE_ROLE, userRepository.findById(1L).get().getEmail(), "Remove role " + Role.valueOf(role).withoutROLE_() + " from " + user.getEmail());
    }


    private void unlockUserAccount(User user) {
        if (user.isAccountNonLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User account is unlock already!");
        }
        user.setFailedAttempt(0);
        user.setAccountNonLocked(true);
        auditorService.addEvent(Action.UNLOCK_USER, userRepository.findById(1L).get().getEmail(), "Unlock user " + user.getEmail());
        userRepository.save(user);
    }

    private void lockUserAccount(User user) {
        if (isUserAdmin(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Can't lock the ADMINISTRATOR!");
        }
        if (!user.isAccountNonLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User account is locked already!");
        }
        user.setAccountNonLocked(false);
        auditorService.addEvent(Action.LOCK_USER, user.getEmail(), "Lock user " + user.getEmail());
        userRepository.save(user);
    }

    private boolean isUserBusiness(User user) {
        return user.getRoles().stream()
                .anyMatch(r -> List.of(Role.ROLE_USER, Role.ROLE_ACCOUNTANT, Role.ROLE_AUDITOR).contains(r));
    }

    private boolean isUserAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(r -> r == Role.ROLE_ADMINISTRATOR);
    }
}