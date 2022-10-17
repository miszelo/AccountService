package account.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class User {

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastname;

    @Email(regexp = "\\w+(@acme.com)$", message = "Mail does not include @acme.com")
    @NotEmpty
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    private String password;

}
