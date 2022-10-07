package com.bridgelabz.notesapp.notes.controller;

import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.notes.dto.NotesDto;
import com.bridgelabz.notesapp.notes.model.Notes;
import com.bridgelabz.notesapp.notes.service.INotesServiceImpl;
import com.bridgelabz.notesapp.utility.Response;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpsRedirectSpec;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class NotesController {

    @Autowired
    private INotesServiceImpl notesService;

    @PostMapping("/addnote/{id}")
    public ResponseEntity<Response> addNoteById(@PathVariable int id, @RequestBody NotesDto notes)
            throws CustomException {
        return new ResponseEntity<>(notesService.addNoteById(id, notes), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnote/{note_id}")
    public ResponseEntity<Response> getNoteById(@PathVariable int user_id, @PathVariable int note_id)
            throws CustomException {
        return new ResponseEntity<>(notesService.getNoteById(user_id, note_id), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getnotes")
    public ResponseEntity<Response> getNotesById(@PathVariable int user_id) throws CustomException {
        return new ResponseEntity<>(notesService.getNotesById(user_id), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/delete/{note_id}")
    public ResponseEntity<Response> deleteNoteById(@PathVariable int user_id, @PathVariable int note_id)
            throws CustomException {
        return new ResponseEntity<>(notesService.deleteNoteById(user_id, note_id), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/remove/{note_id}")
    public ResponseEntity<Response> deleteNote(@PathVariable int user_id, @PathVariable int note_id)
            throws CustomException {
        return new ResponseEntity<>(notesService.deleteNote(user_id, note_id), HttpStatus.OK);
    }

    @PostMapping("/{user_id}/restore/{note_id}")
    public ResponseEntity<Response> undoDeletedNoteById(@PathVariable int user_id, @PathVariable int note_id)
            throws CustomException {
        return new ResponseEntity<>(notesService.restoreNote(user_id, note_id), HttpStatus.OK);
    }

    @PostMapping("/archieve/{username}/{note_id}")
    public ResponseEntity<Response> archieveNote(@PathVariable String username, @PathVariable int note_id) {
        return new ResponseEntity<>(notesService.archivedNote(username, note_id), HttpStatus.OK);
    }

    @PutMapping("/{username}/update/{note_id}")
    public ResponseEntity<Response> updateNote(@PathVariable String username,
            @PathVariable int note_id,
            @RequestBody NotesDto notesDto) {
        return new ResponseEntity<>(notesService.updateNote(username, note_id, notesDto), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getarchieved")
    public ResponseEntity<Response> archievedNotes(@PathVariable int user_id) {
        return new ResponseEntity<>(notesService.getArchievedNotes(user_id), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getdeleted")
    public ResponseEntity<Response> trashNotes(@PathVariable int user_id) {
        return new ResponseEntity<>(notesService.getTrashNotes(user_id), HttpStatus.OK);
    }

    @PostMapping("/pin/{id}")
    public ResponseEntity<Response> pinNote(@PathVariable int id) {
        return new ResponseEntity<>(notesService.pinNote(id), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/getpinned")
    public ResponseEntity<Response> getPinned(@PathVariable int user_id) {
        return new ResponseEntity<>(notesService.getPinned(user_id), HttpStatus.OK);
    }

    @PostMapping("/{note_id}/setbackground/{color}")
    public ResponseEntity<Response> setColor(@PathVariable int note_id, @PathVariable String color) {
        return new ResponseEntity<>(notesService.setColor(note_id, color), HttpStatus.OK);
    }

}
