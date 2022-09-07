package com.bridgelabz.notesapp.repository;


import com.bridgelabz.notesapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>
{
    Optional<Users> findByUserName(String userName);

    Optional<Users> findByEmail(String passWord);
}
