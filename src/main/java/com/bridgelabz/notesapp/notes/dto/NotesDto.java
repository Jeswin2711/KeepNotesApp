package com.bridgelabz.notesapp.notes.dto;

import lombok.Data;

import javax.xml.stream.events.StartElement;


@Data
public class NotesDto
{

    private String title;

    private String description;

    private String color;
}
