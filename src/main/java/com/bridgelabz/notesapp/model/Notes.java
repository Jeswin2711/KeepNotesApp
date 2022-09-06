package com.bridgelabz.notesapp.model;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.List;


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
