package com.bridgelabz.notesapp.user.repository;

import com.bridgelabz.notesapp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    Optional<User> findByPassWord(String passWord);

    Optional<User> findByEmail(String email);
}
