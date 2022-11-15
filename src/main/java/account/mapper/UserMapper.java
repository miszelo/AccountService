package account.mapper;

import account.model.dto.NewUserDTO;
import account.model.dto.UserInfoDTO;
import account.model.user.Role;
import account.model.user.User;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User mapNewUserDTOToUser(NewUserDTO newUserDTO) {
        return User.builder()
                .name(newUserDTO.getName())
                .lastname(newUserDTO.getLastname())
                .email(newUserDTO.getEmail())
                .password(newUserDTO.getPassword())
                .build();
    }

    public UserInfoDTO mapUserToUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .filter(Objects::nonNull)
                        .map(Role::name)
                        .sorted()
                        .collect(Collectors.toList()))
                .build();
    }

}