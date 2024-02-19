package pt.virtualmarket.userservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.virtualmarket.userservice.entities.UserEntity;
import pt.virtualmarket.userservice.entities.UserResponse;
import pt.virtualmarket.userservice.exceptions.UserNotFoundException;
import pt.virtualmarket.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;


    public UserResponse getUserById(int id){
        Optional<UserEntity> user= userRepository.findById(id);
        return  mapper.map(user,UserResponse.class);
    }
    public void createUser(UserEntity userEntity){
        userRepository.save(userEntity);
    }

    public UserResponse updateUserById(int id, UserEntity userToUpdate){
        Optional<UserEntity> userEntity=userRepository.findById(id);
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            user.setName(userToUpdate.getName());
            user.setAge(userToUpdate.getAge());
            user.setEmail(userToUpdate.getEmail());
            try {
                userRepository.save(user);
                return mapper.map(user, UserResponse.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update user", e);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public List<UserResponse> getAllUsers() {
        List<UserEntity> usersList = userRepository.findAll();
        return usersList.stream()
                .map(user -> mapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteUserById(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }
}


