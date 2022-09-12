package com.bridgelabz.notesapp.user.model;

import com.bridgelabz.notesapp.notes.model.Notes;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "User")
public class User
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
    @JoinColumn(name = "fk_user_id" , referencedColumnName = "USER_ID")
    private List<Notes> notes;
}
