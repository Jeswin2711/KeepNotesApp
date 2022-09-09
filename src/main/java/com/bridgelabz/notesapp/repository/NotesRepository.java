package com.bridgelabz.notesapp.repository;

import com.bridgelabz.notesapp.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes,Integer>
{
    @Query("SELECT u FROM Notes u where u.deleted = false ")
    List<Notes> findUnArchived();
}
