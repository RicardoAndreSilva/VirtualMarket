package pt.virtualmarket.userservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.virtualmarket.userservice.services.UserService;


@Configuration
public class UserConfig {

  @Bean
  public ModelMapper modelMapperBean() {
    return new ModelMapper();
  }

  @Bean
  public UserService userServiceBean() {
    return new UserService();
  }
}
