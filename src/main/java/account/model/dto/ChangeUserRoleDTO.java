package account.model.dto;

import account.model.user.Operation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangeUserRoleDTO {
    private String user;
    private String role;
    private Operation operation;
}