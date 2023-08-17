package dev.back.control;

import dev.back.DTO.*;
import dev.back.entite.Absence;
import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.service.DepartementService;
import dev.back.service.EmployeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("employe")
public class EmployeControl {

    EmployeService employeService;


    DepartementService departementService;
    PasswordEncoder passwordEncoder;


    public EmployeControl(EmployeService employeService, DepartementService departementService, PasswordEncoder passwordEncoder) {
        this.employeService = employeService;
        this.departementService = departementService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Employe> listAll(){
        return employeService.listEmployes();
    }

    @PostMapping
    public ResponseEntity<?> addEmploye(@RequestBody EmployeDTO employeDTO){

        String pswEncoded = passwordEncoder.encode(employeDTO.getPassword());
        Employe manager = employeService.getEmployeById(employeDTO.getManagerId());
        Departement departement = departementService.getDepartementById(employeDTO.getDepartementId());

        Employe employe = new Employe(
                employeDTO.getFirstName(),
                employeDTO.getLastName(),
                pswEncoded,
                employeDTO.getSoldeConge(),
                employeDTO.getSoldeRtt(),
                employeDTO.getEmail(),
                employeDTO.getRoles(),
                departement,
                manager);

        employeService.addEmploye(employe);

        return ResponseEntity.status(HttpStatus.CREATED).body("employe cree");
    }

    @RequestMapping("/newpassword")
    @PostMapping
    public ResponseEntity<?> changeEmployePassword(@RequestBody Employe employe){
        String pswEncoded = passwordEncoder.encode(employe.getPassword());
      Employe employe1= employeService.getEmployeById(employe.getId());
        employe.setPassword(pswEncoded);
        employeService.addEmploye(employe);
        return   ResponseEntity.status(HttpStatus.CREATED).body("mot de passe changé");
    }


    @RequestMapping("/modifier")
    @PutMapping
    public ResponseEntity<?> testPut(@RequestBody Employe employe){

        Employe employe1 = employeService.getEmployeById(employe.getId());
        employe1.setFirstName(employe.getFirstName());
        employe1.setLastName(employe.getLastName());
        employe1.setManager(employe.getManager());
        employe1.setDepartement(employe.getDepartement());
        employe1.setRoles(employe.getRoles());
        employe1.setSoldeConge(employe.getSoldeConge());
        employe1.setSoldeRtt(employe.getSoldeRtt());
        employeService.addEmploye(employe1);

        return ResponseEntity.status(HttpStatus.OK).body("l'employé a été modfié");
    }

}

