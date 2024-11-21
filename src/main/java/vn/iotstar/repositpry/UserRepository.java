package vn.iostart.repositpry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.iostart.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}
