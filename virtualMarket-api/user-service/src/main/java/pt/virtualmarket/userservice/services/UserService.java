package pt.virtualmarket.userservice.services;

import static pt.virtualmarket.userservice.utils.Constants.INTERNAL_SERVER_ERROR;
import static pt.virtualmarket.userservice.utils.Constants.NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.exceptions.HttpException;
import pt.virtualmarket.userservice.repository.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ModelMapper mapper;


  public UserResponse getUserById(int id) {
    Optional<UserEntity> user = userRepository.findById(id);
    if (user.isPresent()) {
      return mapper.map(user, UserResponse.class);
    } else {
      throw new HttpException("User not found", NOT_FOUND);
    }
  }

  public void createUser(UserEntity user) {
    try {
      if (userRepository.existsById(user.getId())) {
        throw new HttpException("User already exists", HttpStatus.CONFLICT.value());
      }
      userRepository.save(user);
    } catch (Exception e) {
      throw new HttpException("Failed to create user", INTERNAL_SERVER_ERROR);
    }
  }

  public UserResponse updateUserById(int userId, UserEntity userToUpdate) {
    Optional<UserEntity> userEntity = userRepository.findById(userId);
    if (userEntity.isPresent()) {
      UserEntity user = userEntity.get();
      user.setName(userToUpdate.getName());
      user.setAge(userToUpdate.getAge());
      user.setEmail(userToUpdate.getEmail());
      try {
        userRepository.save(user);
        return mapper.map(user, UserResponse.class);
      } catch (Exception e) {
        throw new HttpException("Failed to update User", INTERNAL_SERVER_ERROR);
      }
    } else {
      throw new HttpException("User not found", NOT_FOUND);
    }
  }

  public List<UserResponse> getAllUsers() {
    try {
      List<UserEntity> usersList = userRepository.findAll();
      return usersList.stream()
          .map(user -> mapper.map(user, UserResponse.class))
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new HttpException("Failed to get users", INTERNAL_SERVER_ERROR);
    }
  }

  public void deleteUserById(int userId) {
    if (!userRepository.existsById(userId)) {
      throw new HttpException("User not found", NOT_FOUND);
    }
    userRepository.deleteById(userId);
  }
}


