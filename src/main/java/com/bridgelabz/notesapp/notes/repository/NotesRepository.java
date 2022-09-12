package com.bridgelabz.notesapp.notes.repository;

import com.bridgelabz.notesapp.notes.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes,Integer>
{
    @Query("SELECT u FROM Notes u where u.isArchived = false and u.isDeleted = false")
    List<Notes> findUnArchived();
}
