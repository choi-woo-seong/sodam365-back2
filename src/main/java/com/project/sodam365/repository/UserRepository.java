package com.project.sodam365.repository;

import com.project.sodam365.dto.UserDto;
import com.project.sodam365.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserid(String userid);
}
