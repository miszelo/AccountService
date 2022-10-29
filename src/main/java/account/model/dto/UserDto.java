package account.model.dto;

import account.validation.Password;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotEmpty(message = "name required")
    private String name;

    @NotEmpty(message = "lastname required")
    private String lastname;

    @Email(regexp = "\\w+(@acme.com)$", message = "Mail does not include @acme.com")
    @NotEmpty(message = "email required")
    private String email;

    @Password
    @NotEmpty(message = "password required")
    private String password;
}
