package dev.back.entite;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    LocalDate dateDebut;
    LocalDate dateFin;

    Statut statut;
    TypeAbsence typeAbsence;

    @ManyToOne
    Employe employe;

    public Absence(LocalDate dateDebut, LocalDate dateFin, Statut statut, TypeAbsence typeAbsence, Employe employe) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.typeAbsence = typeAbsence;
        this.employe = employe;
    }
}
