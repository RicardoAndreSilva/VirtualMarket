package pt.virtualmarket.userservice.entities;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pt.virtualmarket.userservice.utils.UserCreator;

class UserResponseTest {

  @Test
  @DisplayName("Test for equals() and hashCode()")
  void testEqualsAndHashCode_returnsOk_WhenSuccessful() {
    UserResponse user1 = new UserResponse(1, "John", "teste1@example.com", "30");
    UserResponse user2 = new UserResponse(1, "John", "teste1@example.com", "30");
    UserResponse user3 = new UserResponse(2, "Jane", "test3@example.com", "25");

    boolean equals1to2 = user1.equals(user2);
    boolean equals2to1 = user2.equals(user1);
    boolean equals1to3 = user1.equals(user3);
    boolean equals3to1 = user3.equals(user1);

    Assertions.assertThat(equals1to2 && equals2to1).isTrue();
    Assertions.assertThat(equals1to3 || equals3to1).isFalse();
    Assertions.assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    Assertions.assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
  }

  @Test
  @DisplayName("Test for toString()")
  void testToString_returnsOk_WhenSuccessful() {
    UserResponse user = UserCreator.createUserToUserResponse();

    String userExpected = user.toString();

    Assertions.assertThat(userExpected)
        .contains("john")
        .contains("teste5@hotmail.com")
        .contains("20");
  }

  @Test
  @DisplayName("Test object creation using Lombok @Builder")
  void testObjectCreationUsingBuilder_ReturnsOk_WhenSuccessful() {
    UserResponse user = UserResponse.builder()
        .id(1)
        .name("spring")
        .email("testeWithJunit5@test.com")
        .age("30")
        .build();

    Assertions.assertThat(user).isNotNull();
  }
}