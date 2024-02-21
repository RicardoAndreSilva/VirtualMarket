package pt.virtualmarket.userservice.repository;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pt.virtualmarket.userservice.entities.UserEntity;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Test for User repository")
class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("Test for save user when sucessful")
  void save_persistUserTest(){
    UserEntity userToBeSaved =createUser();
    UserEntity userSaved=this.userRepository.save(userToBeSaved);

    assertThat(userSaved).isNotNull();
    assertThat(userSaved.getId()).isNotNull();
    assertThat(userSaved.getName()).isEqualTo(userToBeSaved.getName());
    assertThat(userSaved.getEmail()).isEqualTo(userToBeSaved.getEmail());
    assertThat(userSaved.getAge()).isEqualTo(userToBeSaved.getAge());
  }

  @Test
  @DisplayName("Test for update user when sucessfull")
  void update_persistUserTest(){
    UserEntity userToBeSaved= createUser();
    UserEntity userSaved=this.userRepository.save(userToBeSaved);

    userSaved.setName("Miguel oliveira");
    userSaved.setEmail("teste@gmail.com");
    userSaved.setAge("25");

    UserEntity userUpdated=this.userRepository.save(userSaved);

    assertThat(userUpdated).isNotNull();
    assertThat(userUpdated.getId()).isEqualTo(userSaved.getName());
    assertThat(userUpdated.getAge()).isEqualTo(userSaved.getAge());
    assertThat(userUpdated.getEmail()).isEqualTo(userSaved.getEmail());
  }

  @Test
  @DisplayName("Test for delete when sucessful")
  void delete_persistUserTest(){
    UserEntity userToBySaved=createUser();
    UserEntity userSaved=this.userRepository.save(userToBySaved);

    this.userRepository.delete(userSaved);

    Optional<UserEntity> userOptional= this.userRepository.findById(userSaved.getId());

    assertThat(userOptional).isEmpty();
  }

  @Test
  @DisplayName("Test for get user by id when successful")
  void findById_persistUserTest(){
    UserEntity userToBeSaved = createUser();
    UserEntity userSaved = this.userRepository.save(userToBeSaved);

    Optional<UserEntity> userOptional = this.userRepository.findById(userSaved.getId());

    assertThat(userOptional).isPresent();
    assertThat(userOptional.get()).isEqualTo(userSaved);
  }
}