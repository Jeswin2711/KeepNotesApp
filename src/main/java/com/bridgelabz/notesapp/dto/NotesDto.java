package com.bridgelabz.notesapp.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class NotesDto
{
    @Column(name = "Note")
    private String note;
}
