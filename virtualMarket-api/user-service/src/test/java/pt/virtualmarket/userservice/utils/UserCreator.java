package pt.virtualmarket.userservice.utils;

import pt.virtualmarket.userservice.entities.UserEntity;

public class UserCreator {

 public static UserEntity createUserToBeSaved(){
    return UserEntity.builder()
        .age("20")
        .email("Rs.silva.1985@hotmail.com")
        .name("john")
        .build();

  }
  public static UserEntity createValidUser(){
    return UserEntity.builder()
        .id(1)
        .age("20")
        .email("Rs.silva.1985@hotmail.com")
        .name("john")
        .build();

  }
  public static UserEntity createValidUpdatedUser(){
    return UserEntity.builder()
        .age("20")
        .email("Rs.silva.1985@hotmail.com")
        .name("john")
        .build();
  }
}
