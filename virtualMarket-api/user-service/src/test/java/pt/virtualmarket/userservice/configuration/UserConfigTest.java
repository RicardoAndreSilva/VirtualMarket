package pt.virtualmarket.userservice.configuration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.virtualmarket.userservice.services.UserService;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserConfigTest {

  @Autowired
  private UserConfig userConfig;

  @Test
  @DisplayName("Test userBean creation")
  void testUserBeanCreation_ReturnsOk_WhenSuccessful() {

    UserService userService = userConfig.userServiceBean();

    Assertions.assertThat(userService).isNotNull();
  }

  @Test
  @DisplayName("Test modelMapperBean creation")
  void testModelMapperBeanCreation_ReturnsOk_WhenSuccessful() {
    ModelMapper modelMapper = userConfig.modelMapperBean();

    Assertions.assertThat(modelMapper).isNotNull();
  }
}