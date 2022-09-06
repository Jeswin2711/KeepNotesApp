package com.bridgelabz.notesapp.repository;

import com.bridgelabz.notesapp.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<ConfirmationToken,Integer> {

    Optional<ConfirmationToken> findByToken (String token);
}
