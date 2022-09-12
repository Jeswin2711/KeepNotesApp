package com.bridgelabz.notesapp.notes.model;

import lombok.Data;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "Notes")
public class Notes
{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "is_archived")
    private boolean isArchived = Boolean.FALSE;

    @Column(name = "isDeleted")
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "Color")
    private String color;
}
