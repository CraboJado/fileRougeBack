package dev.back.DTO;

import dev.back.entite.TypeJour;

import java.time.LocalDate;

public class JourOffDTO {
    LocalDate jour;
    TypeJour typeJour;

    public LocalDate getJour() {
        return jour;
    }

    public void setJour(LocalDate jour) {
        this.jour = jour;
    }

    public TypeJour getTypeJour() {
        return typeJour;
    }

    public void setTypeJour(TypeJour typeJour) {
        this.typeJour = typeJour;
    }
}
