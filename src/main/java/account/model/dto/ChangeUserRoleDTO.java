package account.model.dto;

import account.model.user.Operation;
import account.model.user.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ChangeUserRoleDTO {
    private String user;
    @Setter
    @JsonProperty(value = "ROLE_")
    private Role role;
    private Operation operation;
}
