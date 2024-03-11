package pt.virtualmarket.userservice.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.exceptions.HttpException;
import pt.virtualmarket.userservice.repository.UserRepository;
import pt.virtualmarket.userservice.utils.UserCreator;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepositoryMock;

  @Mock
  private ModelMapper mapper;


  @Test
  @DisplayName("Test for getAllUsers returns list of users when successful")
  void testGetAllUsers_returnsListOfUsers_WhenSuccessful() {
    when(userRepositoryMock.findAll())
        .thenReturn(List.of(UserCreator.createValidUser()));
    when(mapper.map(any(UserEntity.class), any()))
        .thenReturn(UserCreator.createUserToUserResponse());
    List<UserResponse> users = userService.getAllUsers();

    Assertions.assertThat(users).isNotNull().isNotEmpty();

    UserResponse userResponse = users.get(0);
    Assertions.assertThat(userResponse).isNotNull();

    Assertions.assertThat(userResponse.getName())
        .isEqualTo(UserCreator.createValidUser().getName());
    Assertions.assertThat(userResponse.getEmail())
        .isEqualTo(UserCreator.createValidUser().getEmail());
    Assertions.assertThat(userResponse.getId()).isEqualTo(UserCreator.createValidUser().getId());
    Assertions.assertThat(userResponse.getAge()).isEqualTo(UserCreator.createValidUser().getAge());
  }

  @Test
  @DisplayName("Test for getAllUsers throws exception when an error occurs")
  void testGetAllUsers_Internal_Server_Error() {
    when(userRepositoryMock.findAll()).thenThrow(RuntimeException.class);

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.getAllUsers();
    });

    Assertions.assertThat(exception.getStatusCode()).isEqualTo(500);
    Assertions.assertThat(exception.getMessage()).isEqualTo("Failed to get users");
  }

  @Test
  @DisplayName("Test for getUserById returns user when successful")
  void testGetUserById_returnsUser_WhenSuccessful() {
    UserEntity userEntity = UserCreator.createValidUser();
    UserResponse expectedUserResponse = UserCreator.createUserToUserResponse();

    when(userRepositoryMock.findById(Mockito.anyInt())).thenReturn(Optional.of(userEntity));
    when(mapper.map(Mockito.any(), Mockito.eq(UserResponse.class)))
        .thenReturn(expectedUserResponse);

    UserResponse userResponse = userService.getUserById(1);

    Assertions.assertThat(userResponse).isNotNull();
    Assertions.assertThat(userResponse.getId()).isEqualTo(userEntity.getId());
    Assertions.assertThat(userResponse.getName()).isEqualTo(userEntity.getName());
    Assertions.assertThat(userResponse.getAge()).isEqualTo(userEntity.getAge());
    Assertions.assertThat(userResponse.getEmail()).isEqualTo(userEntity.getEmail());
  }

  @Test
  @DisplayName("Test for getUserById throws exception when user NOT FOUND")
  void testGetUserById_ReturnsNOT_FOUND_WhenNotFound() {
    when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.getUserById(1);
    });

    Assertions.assertThat(exception.getStatusCode()).isEqualTo(404);

    Assertions.assertThat(exception.getMessage()).isEqualTo("User not found");
  }

  @Test
  @DisplayName("Test for getUserById throws exception when user not found")
  void testGetUserById_ThrowsException_WhenUserNotFound() {
    when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(HttpException.class, () -> userService.getUserById(1));
  }

  @Test
  @DisplayName("Test for save user when INTERNAL_SERVER_ERROR")
  void testUpdateUserById_Internal_Server_Error_WhenSuccessful() {
    when(userRepositoryMock.existsById(anyInt())).thenReturn(false);

    UserEntity userToSave = UserCreator.createValidUser();

    when(userRepositoryMock.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.createUser(userToSave);
    });

    Assertions.assertThat(exception.getMessage()).isEqualTo("Failed to create user");
    Assertions.assertThat(exception.getStatusCode()).isEqualTo(500);
  }

  @Test
  @DisplayName("Test for save user when successful")
  void testSaveUser_ReturnsOkWhenSuccessful() {
    when(userRepositoryMock.save(any(UserEntity.class)))
        .thenReturn(UserCreator.createValidUser());

    UserEntity userToSave = UserCreator.createValidUser();

    userService.createUser(userToSave);
    Mockito.verify(userRepositoryMock, Mockito.times(1)).save(userToSave);
  }

  @Test
  @DisplayName("Test for save user when CONFLICT")
  void testSaveUser_Conflict_WhenConflict() {
    when(userRepositoryMock.existsById(anyInt())).thenReturn(true);

    UserEntity userToSave = UserCreator.createValidUser();

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.createUser(userToSave);
    });

    Assertions.assertThat(exception.getMessage()).isEqualTo("User already exists");
    Assertions.assertThat(exception.getStatusCode()).isEqualTo(409);
  }

  @Test
  @DisplayName("Test for save user when successful")
  void testSaveUser_Internal_Server_Error_WhenSuccessful() {
    when(userRepositoryMock.existsById(anyInt())).thenReturn(false);

    UserEntity userToSave = UserCreator.createValidUser();

    when(userRepositoryMock.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.createUser(userToSave);
    });

    Assertions.assertThat(exception.getMessage()).isEqualTo("Failed to create user");
    Assertions.assertThat(exception.getStatusCode()).isEqualTo(500);
  }

  @Test
  @DisplayName("Test updates user when successful")
  void testUpdatesUser_ReturnsOk_WhenSuccessful() {
    when(userRepositoryMock.save(any(UserEntity.class)))
        .thenReturn(UserCreator.createUserToBeSaved());

    when(userRepositoryMock.findById(anyInt()))
        .thenReturn(Optional.of(UserCreator.createUserToBeSaved()));

    Assertions.assertThatCode(() -> userService.updateUserById(
            UserCreator.createValidUpdatedUser().getId(), UserCreator.createUserToBeSaved()))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Test for updateUserById throws exception when Failed to update User")
  void testUpdateUser_ReturnsFailed_WhenFailed() {
    UserEntity userEntity = UserCreator.createUserToBeSaved();

    when(userRepositoryMock.findById(anyInt())).thenReturn(
        Optional.ofNullable(userEntity));

    when(userRepositoryMock.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.updateUserById(userEntity.getId(), UserCreator.createUserToBeSaved());
    });

    Assertions.assertThat(exception.getStatusCode()).isEqualTo(500);

    Assertions.assertThat(exception.getMessage()).isEqualTo("Failed to update User");
  }

  @Test
  @DisplayName("Test for updateUserById throws exception when NOT FOUND")
  void testUpdateUser_Not_Found_WhenNotFound() {
    UserEntity userEntity = UserCreator.createUserToBeSaved();

    when(userRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.updateUserById(userEntity.getId(), UserCreator.createUserToBeSaved());
    });

    Assertions.assertThat(exception.getStatusCode()).isEqualTo(404);
    Assertions.assertThat(exception.getMessage()).isEqualTo("User not found");
  }

  @Test
  @DisplayName("delete removes user successful")
  void testDeleteUser_ReturnsOK_WhenSuccessful() {
    when(userRepositoryMock.existsById(1)).thenReturn(true);
    Mockito.doNothing().when(userRepositoryMock).delete(any(UserEntity.class));
    Assertions.assertThatCode(() -> userService.deleteUserById(1))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("Test for deleteUserById throws exception when NOT FOUND")
  void testDeleteUser_ReturnsNOT_FOUND_WhenNotFound() {

    when(userRepositoryMock.existsById(anyInt())).thenReturn(false);

    HttpException exception = assertThrows(HttpException.class, () -> {
      userService.deleteUserById(1);
    });
    Assertions.assertThat(exception.getStatusCode()).isEqualTo(404);

    Assertions.assertThat(exception.getMessage()).isEqualTo("User not found");
  }
}


