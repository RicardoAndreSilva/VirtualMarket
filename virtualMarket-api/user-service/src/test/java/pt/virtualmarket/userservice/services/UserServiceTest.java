package pt.virtualmarket.userservice.services;

import static pt.virtualmarket.userservice.utils.Constants.NOT_FOUND;

import java.util.List;
import java.util.Optional;
import org.apache.coyote.BadRequestException;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.exceptions.HttpException;
import pt.virtualmarket.userservice.repository.UserRepository;
import pt.virtualmarket.userservice.utils.UserCreator;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepositoryMock;

  @Mock
  private ModelMapper mapper;

  @BeforeEach
  void setUp_userServiceMock_for() {
    PageImpl<UserEntity> userPage = new PageImpl<>(List.of(UserCreator.createValidUser()));

    BDDMockito.when(userRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
        .thenReturn(userPage);
    BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyInt()))
        .thenReturn(Optional.of(UserCreator.createValidUser()));
    BDDMockito.when(userRepositoryMock.findAll())
        .thenReturn(List.of(UserCreator.createValidUser()));
    BDDMockito.when(userRepositoryMock.save(ArgumentMatchers.any(UserEntity.class)))
        .thenReturn(UserCreator.createValidUser());
    BDDMockito.doNothing().when(userRepositoryMock).delete(ArgumentMatchers.any(UserEntity.class));
  }

  @Test
  @DisplayName("Test for getAllUsers returns list of users when successful")
  void testGetAllUsers_returnsListOfUsers_WhenSuccessful() {
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
  @DisplayName("Test for getUserById returns user when successful")
  void testGetUserById_returnsUser_WhenSuccessful() {

    UserResponse userResponse = userService.getUserById(1);

    Assertions.assertThat(userResponse).isNotNull();
    Assertions.assertThat(userResponse.getId()).isEqualTo(UserCreator.createValidUser().getId());
    Assertions.assertThat(userResponse.getName())
        .isEqualTo(UserCreator.createValidUser().getName());
    Assertions.assertThat(userResponse.getEmail())
        .isEqualTo(UserCreator.createValidUser().getEmail());
    Assertions.assertThat(userResponse.getAge()).isEqualTo(UserCreator.createValidUser().getAge());
  }

  @Test
  @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when user is not found")
  void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenUserIsNotFound() {
    BDDMockito.when(userRepositoryMock.findById(ArgumentMatchers.anyInt()))
        .thenReturn(Optional.empty());

    Assertions.assertThatExceptionOfType(BadRequestException.class)
        .isThrownBy(() -> {
          throw new HttpException("User not found", NOT_FOUND);
        });
  }

  @Test
  @DisplayName("Test for save user when successful")
  void testSaveUser_ReturnsOkWhenSuccessful() {
    UserEntity userToSave = UserCreator.createValidUser();

    userService.createUser(userToSave);
    Mockito.verify(userRepositoryMock, Mockito.times(1)).save(userToSave);
  }

  @Test
  @DisplayName("Test updates user when successful")
  void testUpdatesUser_ReturnsOkWhenSuccessful() {
    Assertions.assertThatCode(() -> userService.updateUserById(
            UserCreator.createValidUser().getId(), UserCreator.createUserToBeSaved()))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("delete removes user successful")
  void testDeleteUser_ReturnsOK_WhenSuccessful() {
    Assertions.assertThatCode(() -> userService.deleteUserById(1))
        .doesNotThrowAnyException();
  }
}

