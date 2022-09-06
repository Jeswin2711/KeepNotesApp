package com.bridgelabz.notesapp.repository;

import com.bridgelabz.notesapp.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes,Integer>{}
