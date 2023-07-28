package dev.back.entite;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String firstName;
    String lastName;
    String password;

    int soldeConge;
    int soldeRtt;

    @Email
    String email;

    Role role;

    @ManyToOne
    Departement departement;

    @ManyToOne
    Employe manager;

    public Employe(String firstName, String lastName, String password, int soldeConge, int soldeRtt, String email, Role role, Departement departement, Employe manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.soldeConge = soldeConge;
        this.soldeRtt = soldeRtt;
        this.email = email;
        this.role = role;
        this.departement = departement;
        this.manager = manager;
    }
}
