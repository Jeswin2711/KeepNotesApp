package com.bridgelabz.notesapp.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "Users")
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private int id;

    @Column(name = "Username")
    private String userName;

    @Column(name = "Password")
    private String passWord;

    @Column(name = "Email")
    private String email;

    @Column(name = "Phonenumber")
    private long phoneNumber;

    @Column(name = "Verified")
    private boolean isVerified = false;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_note" , referencedColumnName = "USER_ID")
    private List<Notes> notes;
}
