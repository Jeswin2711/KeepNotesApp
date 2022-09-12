package com.bridgelabz.notesapp.notes.service.interfaces;

import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.notes.dto.NotesDto;
import com.bridgelabz.notesapp.utility.Response;

public interface INotesService
{
     Response addNoteById(int id , NotesDto notes) throws CustomException;
    Response getNoteById(int user_id ,int note_id) throws CustomException;
    Response deleteNoteById(int user_id , int note_id ) throws CustomException;
    Response archivedNote(String username , int note_id);
    Response restoreNote(int user_id , int note_id);
    Response updateNote(String username, int note_id, NotesDto notesDto);


}
