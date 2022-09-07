package com.bridgelabz.notesapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ConfirmationToken")
@NoArgsConstructor
public class ConfirmationToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private int id;

    @Column(name = "Token")
    private String token;

    @OneToOne(targetEntity = Users.class , cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_id")
    private Users users;

    public ConfirmationToken(String token , Users users)
    {
        this.token = token;
        this.users = users;
    }
}
