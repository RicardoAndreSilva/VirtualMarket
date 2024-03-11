package pt.virtualmarket.userservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import pt.virtualmarket.userservice.exceptions.HttpException;
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

    when(userServiceMock.getAllUsers()).thenReturn(List.of(userResponse));
    BDDMockito.doNothing().when(userServiceMock).deleteUserById(anyInt());
    BDDMockito.doNothing().when(userServiceMock).createUser(any());
    when(
        userServiceMock.updateUserById(any(Integer.class), any()))
        .thenReturn(userResponse);
    when(userServiceMock.getUserById(anyInt()))
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
  @DisplayName("Test controller returns correct status code when HttpException is thrown during getAllUsers ")
  void testGetAllUsersUsers_ReturnsCorrectStatusCode_WhenHttpExceptionThrown() {
    UserEntity userEntity = UserCreator.createValidUpdatedUser();

    doThrow(new HttpException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .when(userServiceMock)
        .getAllUsers();

    ResponseEntity<List<UserResponse>> response = userController.getAllUsers();

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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
  @DisplayName("Test controller returns correct status code when HttpException is thrown during user getById")
  void testGetUserById_ReturnsCorrectStatusCode_WhenHttpExceptionThrown() {

    UserEntity userEntity = UserCreator.createUserToBeSaved();

    when(userServiceMock.getUserById(anyInt())).thenThrow(
        new HttpException("User not found", HttpStatus.NOT_FOUND.value()));

    ResponseEntity<UserResponse> response = userController.getUserDetails(userEntity.getId());

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @DisplayName("Test for save user when successful")
  void testSaveUser_ReturnsOk_WhenSuccessful() {
    ResponseEntity<String> userResponseEntity = userController.saveUser(
        UserCreator.createValidUser());

    Assertions.assertThat(userResponseEntity).isNotNull();
    Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(userResponseEntity.getBody()).isEqualTo("User created");

    Mockito.verify(userServiceMock, Mockito.times(1)).createUser(any());
  }

  @Test
  @DisplayName("Test controller returns correct status code when HttpException is thrown during user save")
  void testSaveUser_ReturnsCorrectStatusCode_WhenHttpExceptionThrown() {
    UserEntity userEntity = UserCreator.createValidUser();

    doThrow(new HttpException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .when(userServiceMock)
        .createUser(any());

    ResponseEntity<String> response = userController.saveUser(userEntity);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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
  @DisplayName("Test controller returns correct status code when HttpException is thrown during user update")
  void testUpdateUser_ReturnsCorrectStatusCode_WhenHttpExceptionThrown() {
    UserEntity userEntity = UserCreator.createValidUpdatedUser();

    doThrow(new HttpException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .when(userServiceMock)
        .updateUserById(userEntity.getId(), userEntity);

    ResponseEntity<String> response = userController.updateUser(userEntity.getId(), userEntity);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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

  @Test
  @DisplayName("Test controller returns correct status code when HttpException is thrown during user delete")
  void testDeleteUser_ReturnsCorrectStatusCode_WhenHttpExceptionThrown() {
    UserEntity userEntity = UserCreator.createValidUpdatedUser();

    doThrow(new HttpException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .when(userServiceMock)
        .deleteUserById(userEntity.getId());

    ResponseEntity<Void> response = userController.deleteUser(userEntity.getId());

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}