package account.controllers;

import account.entity.User;
import account.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<User> registerAccount(@Valid @RequestBody User user) {
        return userService.registerAccount(user);
    }
}
