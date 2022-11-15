package account.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {

    private Long id;

    private String name;

    private String lastname;

    private String email;

    private List<String> roles;
}
