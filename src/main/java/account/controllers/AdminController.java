package account.controllers;

import account.model.dto.ChangeAccessForUserDTO;
import account.model.dto.ChangeUserRoleDTO;
import account.model.dto.DeleteUserResponseDTO;
import account.model.dto.UserInfoDTO;
import account.model.record.ResponseStatus;
import account.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
@RequestMapping("/api/admin/user")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<UserInfoDTO>> informationAboutEmployees() {
        return adminService.informationAboutEmployee();
    }

    @DeleteMapping("/{userEmail}")
    public ResponseEntity<DeleteUserResponseDTO> deleteUser(@PathVariable @Email String userEmail) {
        return adminService.deleteUser(userEmail);
    }

    @PutMapping("/role")
    public ResponseEntity<UserInfoDTO> changeUserRole(@RequestBody @Valid ChangeUserRoleDTO changeUserRoleDTO) {
        return adminService.changeUserRole(changeUserRoleDTO);
    }

    @PutMapping("/access")
    public ResponseEntity<ResponseStatus> changeUserAccess(@RequestBody ChangeAccessForUserDTO changeAccessForUserDTO) {
        return adminService.changeUserAccess(changeAccessForUserDTO);
    }
}