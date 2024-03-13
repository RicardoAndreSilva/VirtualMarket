package pt.virtualmarket.userservice.entities;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.virtualmarket.userservice.repository.UserRepository;
import pt.virtualmarket.userservice.utils.UserCreator;

@ExtendWith(SpringExtension.class)
class UserEntityTest {

  @InjectMocks
  private UserEntity userEntity;

  @Mock
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    userEntity = new UserEntity(userEntity.getId(), "Test user", "testUserEntity@gmail.com", "25");
  }

  @Test
  @DisplayName("Test for save user when successful")
  void testSaveUser_returnsOk_WhenSuccessful() {
    when(userRepository.save(userEntity)).thenReturn(userEntity);

    UserEntity savedUser = userRepository.save(userEntity);

    Assertions.assertThat(userEntity.getName()).isEqualTo(savedUser.getName());
    Assertions.assertThat(userEntity.getEmail()).isEqualTo(savedUser.getEmail());
    Assertions.assertThat(userEntity.getAge()).isEqualTo(savedUser.getAge());
    Assertions.assertThat(userEntity.getId()).isEqualTo(savedUser.getId());

  }

  @Test
  @DisplayName("Test for update user when successful")
  void testUpdateUser_returnsOk_WhenSuccessful() {
    userEntity.setId(1);
    userEntity.setName("Updated User");
    userEntity.setEmail("updatedUser@gmail.com");
    userEntity.setAge("30");

    when(userRepository.save(userEntity)).thenReturn(userEntity);

    UserEntity updatedUser = userRepository.save(userEntity);

    Assertions.assertThat(userEntity.getName()).isEqualTo(updatedUser.getName());
    Assertions.assertThat(userEntity.getEmail()).isEqualTo(updatedUser.getEmail());
    Assertions.assertThat(userEntity.getAge()).isEqualTo(updatedUser.getAge());
    Assertions.assertThat(userEntity.getId()).isEqualTo(updatedUser.getId());

  }

  @Test
  @DisplayName("Test for get user by id when successful")
  void testFindByIdUser_returnsOk_WhenSuccessful() {

    when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

    Optional<UserEntity> foundUser = userRepository.findById(userEntity.getId());

    Assertions.assertThat(userEntity.getId()).isEqualTo(foundUser.get().getId());

  }

  @Test
  @DisplayName("Test for get user by id returns empty optional when user not found")
  void testFindByIdUser_returnsEmptyOptional_WhenUserNotFound() {
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

    Optional<UserEntity> foundUser = userRepository.findById(5);

    Assertions.assertThat(foundUser).isEmpty();
  }

  @Test
  @DisplayName("Test for delete user when successful")
  void testDeleteUser_returnsOk_WhenSuccessful() {

    userRepository.delete(userEntity);

    Optional<UserEntity> deletedUser = userRepository.findById(userEntity.getId());

    Assertions.assertThat(deletedUser).isEqualTo(Optional.empty());

  }

  @Test
  @DisplayName("Test for get all users when successful")
  void testGetAllUsers_returnsOk_WhenSuccessful() {
    UserEntity userEntity2 = UserCreator.createUserToBeSaved();

    when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity, userEntity2));

    List<UserEntity> users = userRepository.findAll();

    Assertions.assertThat(users).hasSize(2);
    Assertions.assertThat(userEntity.getName()).isEqualTo(users.get(0).getName());
    Assertions.assertThat(userEntity2.getName()).isEqualTo(users.get(1).getName());

  }

  @Test
  @DisplayName("Test for equals() and hashCode()")
  void testEqualsAndHashCode_returnsOk_WhenSuccessful() {
    UserEntity user1 = new UserEntity(1, "John", "teste1@example.com", "30");
    UserEntity user2 = new UserEntity(1, "John", "teste1@example.com", "30");
    UserEntity user3 = new UserEntity(2, "Jane", "test3@example.com", "25");

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
    UserEntity user = new UserEntity(1, "spring", "junit5@example.com", "30");

    String actualToString = user.toString();

    Assertions.assertThat(actualToString)
        .contains("UserEntity")
        .contains("id=1")
        .contains("name=spring")
        .contains("email=junit5@example.com")
        .contains("age=30");
  }


  @Test
  @DisplayName("Test object creation using Lombok @Builder")
  void testObjectCreationUsingBuilder_ReturnsOk_WhenSuccessful() {
    UserEntity user = UserEntity.builder()
        .id(1)
        .name("spring")
        .email("testeWithJunit5@test.com")
        .age("30")
        .build();

    Assertions.assertThat(user).isNotNull();
  }
}
