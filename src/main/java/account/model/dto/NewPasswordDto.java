package account.model.dto;

import account.validation.Password;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordDto {

    @NotEmpty
    @Password
    @JsonProperty("new_password")
    private String newPassword;
}
