package com.bridgelabz.notesapp.notes.controller;

import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.notes.dto.NotesDto;
import com.bridgelabz.notesapp.notes.service.INotesServiceImpl;
import com.bridgelabz.notesapp.utility.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class NotesController
{

    @Autowired
    private INotesServiceImpl notesService;


    @PostMapping("/addnote/{id}")
    public ResponseEntity<Response> addNoteById(@PathVariable int id , @RequestBody NotesDto notes) throws CustomException
    {
        return new ResponseEntity<>(notesService.addNoteById( id, notes ), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnote/{note_id}")
    public ResponseEntity<Response> getNoteById(@PathVariable int user_id , @PathVariable int note_id) throws CustomException
    {
        return new ResponseEntity<>(notesService.getNoteById(user_id , note_id ) , HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnotes")
    public ResponseEntity<Response> getNotesById(@PathVariable int user_id) throws CustomException
    {
        return new ResponseEntity<>(notesService.getNotesById(user_id) , HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/deletenote/{note_id}")
    public ResponseEntity<Response> deleteNoteById(@PathVariable int user_id , @PathVariable int note_id) throws CustomException
    {
        return new ResponseEntity<>(notesService.deleteNoteById(user_id,note_id),HttpStatus.OK);
    }


    @PostMapping("/archieve/{username}/{note_id}")
    public ResponseEntity<Response> archieveNote(@PathVariable String username , @PathVariable int note_id)
    {
        return new ResponseEntity<>(notesService.archieveNote(username , note_id),HttpStatus.OK);
    }

    @PostMapping("/unarchieve/{username}/{note_id}")
    public ResponseEntity<Response> unArchieve(@PathVariable String username , @PathVariable int note_id)
    {
        return new ResponseEntity<>(notesService.unArchieveNote(username , note_id),HttpStatus.OK);
    }
}
