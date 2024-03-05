package pt.virtualmarket.userservice.controller;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.services.UserService;
import pt.virtualmarket.userservice.utils.UserCreator;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userServiceMock;

  @Mock
  private ModelMapper mapper;

  @BeforeEach
  void setUp_userServiceMock_for() {
    ModelMapper mapper2 = new ModelMapper();
    UserResponse userResponse = mapper2.map(UserCreator.createValidUser(), UserResponse.class);

    BDDMockito.when(userServiceMock.getAllUsers()).thenReturn(List.of(userResponse));
    BDDMockito.doNothing().when(userServiceMock).deleteUserById(ArgumentMatchers.anyInt());
    BDDMockito.doNothing().when(userServiceMock).createUser(ArgumentMatchers.any());
    BDDMockito.when(
            userServiceMock.updateUserById(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any()))
        .thenReturn(userResponse);
    BDDMockito.when(userServiceMock.getUserById(ArgumentMatchers.anyInt()))
        .thenReturn(userResponse);

  }

  @Test
  @DisplayName("Test for getAllUsers returns list of users when successful")
  void testGetAllUsers_returnsListOfUsers_WhenSuccessful() {
    ResponseEntity<List<UserResponse>> responseEntity = userController.getAllUsers();

    Assertions.assertThat(responseEntity).isNotNull();
    Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    List<UserResponse> userResponses = responseEntity.getBody();
    Assertions.assertThat(userResponses).isNotNull().isNotEmpty();

    UserResponse user = userResponses.get(0);
    UserEntity mockUser = UserCreator.createValidUser();

    Assertions.assertThat(user.getName())
        .isEqualTo(mockUser.getName());
    Assertions.assertThat(user.getEmail())
        .isEqualTo(mockUser.getEmail());
    Assertions.assertThat(user.getId())
        .isEqualTo(mockUser.getId());
    Assertions.assertThat(user.getAge())
        .isEqualTo(mockUser.getAge());
  }

  @Test
  @DisplayName("Test for getUserById returns user when successful")
  void testGetUserById_returnsUser_WhenSuccessful() {
    UserEntity expectedUser = UserCreator.createValidUser();

    ResponseEntity<UserResponse> responseEntity = userController.getUserDetails(1);

    Assertions.assertThat(responseEntity).isNotNull();
    Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    UserResponse userResponse = responseEntity.getBody();

    Assertions.assertThat(userResponse).isNotNull();
    Assertions.assertThat(userResponse.getId()).isEqualTo(expectedUser.getId());
    Assertions.assertThat(userResponse.getName()).isEqualTo(expectedUser.getName());
    Assertions.assertThat(userResponse.getEmail()).isEqualTo(expectedUser.getEmail());
    Assertions.assertThat(userResponse.getAge()).isEqualTo(expectedUser.getAge());
  }

  @Test
  @DisplayName("Test for save user when successful")
  void testSaveUser_ReturnsOk_WhenSuccessful() {
    ResponseEntity<String> userResponseEntity = userController.saveUser(
        UserCreator.createValidUser());

    Assertions.assertThat(userResponseEntity).isNotNull();
    Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(userResponseEntity.getBody()).isEqualTo("User created");

    Mockito.verify(userServiceMock, Mockito.times(1)).createUser(ArgumentMatchers.any());
  }

  @Test
  @DisplayName("Test for update user when successful")
  void testUpdateUser_ReturnsOk_WhenSuccessful() {
    Assertions.assertThatCode(() -> {
      userController.updateUser(1, UserCreator.createValidUpdatedUser());
    }).doesNotThrowAnyException();

    ResponseEntity<String> entity = userController.updateUser(2,
        UserCreator.createValidUpdatedUser());

    Assertions.assertThat(entity).isNotNull();
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }


  @Test
  @DisplayName("delete removes user successful")
  void testDeleteUser_ReturnsOK_WhenSuccessful() {
    Assertions.assertThatCode(() -> userController.deleteUser(1))
        .doesNotThrowAnyException();

    ResponseEntity<Void> entity = userController.deleteUser(1);

    Assertions.assertThat(entity).isNotNull();
    Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}