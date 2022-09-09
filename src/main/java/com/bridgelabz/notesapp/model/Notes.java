package com.bridgelabz.notesapp.model;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@Table(name = "Notes")
public class Notes
{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Note")
    private String note;
}
