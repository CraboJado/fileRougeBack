package dev.back.DTO;

import dev.back.entite.Employe;
import dev.back.entite.Statut;
import dev.back.entite.TypeAbsence;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class AbsenceDTO {
    LocalDate dateDebut;
    LocalDate dateFin;

    Statut statut;
    TypeAbsence typeAbsence;

    int employeId;

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public TypeAbsence getTypeAbsence() {
        return typeAbsence;
    }

    public void setTypeAbsence(TypeAbsence typeAbsence) {
        this.typeAbsence = typeAbsence;
    }

    public Integer getEmployeId() {
        return employeId;
    }

    public void setEmploye(Integer employeId) {
        this.employeId = employeId;
    }
}
