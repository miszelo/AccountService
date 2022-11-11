package account.controllers.mapper;

import account.model.user.User;
import account.model.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapUserDTOToUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .lastname(userDTO.getLastname())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();
    }

}
