package pt.virtualmarket.userservice.integration;


import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.repository.UserRepository;
import pt.virtualmarket.userservice.utils.UserCreator;


@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class UserControllerTestIT {

  @Autowired
  private TestRestTemplate testRestTemplate;
  @Autowired
  private UserRepository userRepository;
  @LocalServerPort
  private int port;
  private ModelMapper mapper;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("GetAllUsers returns empty list when no users exist")
  void getAllUsers_ReturnsEmptyList_WhenNoUsersExist() {
    ResponseEntity<List<UserEntity>> res = testRestTemplate.exchange(
        "http://localhost:" + port + "/users",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<List<UserEntity>>() {
        });

    Assertions.assertThat(res.getBody())
        .as("Check that the body is an empty list")
        .isEmpty();
  }

  @Test
  @DisplayName("GetAllUsers returns users when successful")
  void getAllUsers_ReturnsUsers_WhenSuccessful() {
    UserEntity savedUser = userRepository.save(UserCreator.createUserToBeSaved());

    List<UserEntity> users = testRestTemplate.exchange(
        "http://localhost:" + port + "/users",
        HttpMethod.GET, null, new ParameterizedTypeReference<List<UserEntity>>() {
        }).getBody();

    Assertions.assertThat(users)
        .isNotNull()
        .isNotEmpty();
  }

  @Test
  @DisplayName("Save returns user when successful")
  void save_ReturnsUser_WhenSuccessful() {
    ResponseEntity<String> userResponseEntity = testRestTemplate.postForEntity("/users",
        UserCreator.createUserToBeSaved(), String.class);

    Assertions.assertThat(userResponseEntity).isNotNull();
    Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(userResponseEntity.getBody()).isEqualTo("User created");

  }

  @Test
  @DisplayName("Delete returns user when successful")
  void delete_ReturnsUser_WhenSuccessful() {
    UserEntity user = userRepository.save(UserCreator.createUserToBeSaved());

    testRestTemplate.postForEntity("/users", UserCreator.createUserToBeSaved(), String.class);

    ResponseEntity<Void> resp = testRestTemplate.exchange(
        "http://localhost:" + port + "/users/" + user.getId(),
        HttpMethod.DELETE,
        HttpEntity.EMPTY, Void.class);

    Assertions.assertThat(resp).isNotNull();
    Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DisplayName("Test for delete throws exception when NOT FOUND")
  void testDeleteUser_ReturnsNOT_FOUND_WhenNotFound() {
    Assertions.assertThat(userRepository.findById(5)).isEmpty();

    ResponseEntity<Void> res = testRestTemplate.exchange(
        "http://localhost:" + port + "/users/5",
        HttpMethod.DELETE, null,
        Void.class);

    Assertions.assertThat(res.getStatusCode())
        .as("Check that the status code is NOT_FOUND").isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @DisplayName("GetByIdUser returns user when successful")
  void getByIdUser_ReturnsUser_WhenSuccessful() {
    UserEntity userEntity = userRepository.save(UserCreator.createUserToBeSaved());

    ResponseEntity<UserEntity> res = testRestTemplate.exchange(
        "http://localhost:" + port + "/users/" + userEntity.getId(),
        HttpMethod.GET, null, UserEntity.class);

    Assertions.assertThat(res.getBody()).isNotNull();
    Assertions.assertThat(res.getBody().getId()).isEqualTo(userEntity.getId());
    Assertions.assertThat(res.getBody().getAge()).isEqualTo(userEntity.getAge());
    Assertions.assertThat(res.getBody().getEmail()).isEqualTo(userEntity.getEmail());
    Assertions.assertThat(res.getBody().getName()).isEqualTo(userEntity.getName());
  }

  @Test
  @DisplayName("Test for getById throws exception when NOT FOUND")
  void testGetUserById_ReturnsNOT_FOUND_WhenNotFound() {
    ResponseEntity<UserEntity> res = testRestTemplate.getForEntity(
        "http://localhost:" + port + "/users/5",
        UserEntity.class);

    Assertions.assertThat(res.getStatusCode())
        .as("Check that the status code is NOT_FOUND").isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  @DisplayName("Update returns user when successful")
  void updateUser_ReturnsUser_WhenSuccessful() {
    UserEntity userEntityToSave = userRepository.save(UserCreator.createValidUpdatedUser());

    ResponseEntity<Void> res = testRestTemplate.exchange(
        "http://localhost:" + port + "/users/" + userEntityToSave.getId(),
        HttpMethod.PUT, new HttpEntity<>(userEntityToSave), Void.class);

    UserEntity updatedUser = userRepository.findById(userEntityToSave.getId()).orElse(null);

    Assertions.assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    Assertions.assertThat(updatedUser).isNotNull();
    Assertions.assertThat(userEntityToSave).isNotNull();
    Assertions.assertThat(userEntityToSave.getId()).isEqualTo(updatedUser.getId());
    Assertions.assertThat(userEntityToSave.getAge()).isEqualTo(updatedUser.getAge());
    Assertions.assertThat(userEntityToSave.getEmail()).isEqualTo(updatedUser.getEmail());
    Assertions.assertThat(userEntityToSave.getName()).isEqualTo(updatedUser.getName());

  }

  @Test
  @DisplayName("Test for updateUserById throws exception when NOT FOUND")
  void testUpdateUser_ReturnsNOT_FOUND_WhenNotFound() {
    UserEntity userEntityToSave = UserCreator.createValidUpdatedUser();

    ResponseEntity<Void> res = testRestTemplate.exchange(
        "http://localhost:" + port + "/users/5",
        HttpMethod.PUT, new HttpEntity<>(userEntityToSave),
        Void.class);

    Assertions.assertThat(res.getStatusCode())
        .as("Check that the status code is NOT_FOUND").isEqualTo(HttpStatus.NOT_FOUND);

  }
}


