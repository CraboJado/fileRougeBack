package dev.back.DTO;

import dev.back.entite.Departement;
import dev.back.entite.Employe;
//import dev.back.entite.Role;
import lombok.Data;

import java.util.List;
@Data
public class EmployeDTO {
    String firstName;
    String lastName;
    String password;

    int soldeConge;
    int soldeRtt;

    String email;

    List<String> roles;

    Integer departementId;

    Integer managerId;

}
