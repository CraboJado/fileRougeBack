package dev.back.DTO;

import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.entite.Role;
import jakarta.persistence.ManyToOne;

public class EmployeDTO {
    String firstName;
    String lastName;
    String passWord;

    int soldeConge;
    int soldeRtt;
    String email;

    Role role;

    Departement departement;

    Employe manager;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getSoldeConge() {
        return soldeConge;
    }

    public void setSoldeConge(int soldeConge) {
        this.soldeConge = soldeConge;
    }

    public int getSoldeRtt() {
        return soldeRtt;
    }

    public void setSoldeRtt(int soldeRtt) {
        this.soldeRtt = soldeRtt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Employe getManager() {
        return manager;
    }

    public void setManager(Employe manager) {
        this.manager = manager;
    }
}
