package account.model.dto;

import account.validation.Password;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserDTO {

    @NotEmpty(message = "name required")
    private String name;

    @NotEmpty(message = "lastname required")
    private String lastname;

    @Email(regexp = "\\w+(@acme.com)$", message = "Mail does not include @acme.com")
    @NotEmpty(message = "email required")
    private String email;

    @Password
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "password required")
    private String password;
}
