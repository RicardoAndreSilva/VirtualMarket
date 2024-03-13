package pt.virtualmarket.userservice.utils;

import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;

public class UserCreator {

  public static UserEntity createUserToBeSaved() {
    return UserEntity.builder()
        .age("20")
        .email("junit5@hotmail.com")
        .name("john")
        .build();

  }

  public static UserResponse createUserToUserResponse() {
    return UserResponse.builder()
        .age("20")
        .email("teste5@hotmail.com")
        .name("john")
        .build();

  }

  public static UserEntity createValidUser() {
    return UserEntity.builder()
        .age("20")
        .email("teste5@hotmail.com")
        .name("john")
        .build();

  }

  public static UserEntity createValidUpdatedUser() {
    return UserEntity.builder()
        .id(1)
        .age("20")
        .email("teste@hotmail.com")
        .name("userResponse")
        .build();
  }
}
