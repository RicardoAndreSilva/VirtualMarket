package pt.virtualmarket.userservice.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.virtualmarket.userservice.services.UserService;

@Configuration
public class UserConfig {
    @Bean
    public UserService userBean(){
        return new UserService();
    }
    @Bean
    public ModelMapper  modelMapperBean(){
        return new ModelMapper();
    }
}
