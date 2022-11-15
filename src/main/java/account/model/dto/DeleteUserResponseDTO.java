package account.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DeleteUserResponseDTO {

    private final String user;
    private final String status = "Deleted successfully!";
}