package com.bridgelabz.notesapp.notes.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class NotesDto
{
    @Column(name = "Note")
    private String note;
}
