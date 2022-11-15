package account.controllers;

import account.mapper.UserMapper;
import account.model.dto.NewPasswordDTO;
import account.model.dto.NewUserDTO;
import account.model.dto.UserInfoDTO;
import account.model.user.User;
import account.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
@AllArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserInfoDTO> registerAccount(@RequestBody @Valid NewUserDTO newUserDto) {
        User user = userMapper.mapNewUserDTOToUser(newUserDto);
        return userService.registerAccount(user);
    }

    @PostMapping("/changepass")
    public ResponseEntity<Map<String, String>> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody @Valid NewPasswordDTO newPasswordDto) {
        return userService.changePassword(userDetails, newPasswordDto.getNewPassword());
    }
}