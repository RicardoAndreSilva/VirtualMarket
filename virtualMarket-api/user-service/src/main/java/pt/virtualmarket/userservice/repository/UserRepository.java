package pt.virtualmarket.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.virtualmarket.userservice.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
}
