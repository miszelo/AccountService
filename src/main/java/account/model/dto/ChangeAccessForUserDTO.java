package account.model.dto;


import account.model.user.Operation;
import lombok.Data;

@Data
public class ChangeAccessForUserDTO {
    private String user;
    private Operation operation;
}
