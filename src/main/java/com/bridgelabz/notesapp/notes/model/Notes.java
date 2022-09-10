package com.bridgelabz.notesapp.notes.model;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "Notes")
@SQLDelete(sql = "UPDATE notes SET deleted = true WHERE id=?")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
public class Notes
{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Note")
    private String note;

    @Column(name = "isArchieved")
    private boolean isArchieved = Boolean.FALSE;
}
