package account.controllers;

import account.controllers.mapper.UserMapper;
import account.model.User;
import account.model.dto.NewPasswordDto;
import account.model.dto.UserDto;
import account.services.UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> registerAccount(@RequestBody @Valid UserDto userDto) {
        return userService.registerAccount(userMapper.mapUserDtoToUser(userDto));
    }

    @PostMapping("/changepass")
    public ResponseEntity<Map<String, String>> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestBody @Valid NewPasswordDto newPasswordDto) {
        return userService.changePassword(userDetails, newPasswordDto.getNewPassword());
    }
}
