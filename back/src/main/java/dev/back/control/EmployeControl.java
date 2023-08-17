package dev.back.control;

import dev.back.DTO.EmployeDTO;
import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.service.DepartementService;
import dev.back.service.EmployeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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



    @PostMapping
    public ResponseEntity<?> changeEmployePassword(@RequestBody int employeId,@RequestBody String newPass){
      Employe employe= employeService.getEmployeById(employeId);
        employe.setPassword(newPass);
        employeService.addEmploye(employe);
        return   ResponseEntity.status(HttpStatus.CREATED).body("mot de passe changé");
    }

    @PostMapping
    public ResponseEntity<?> changeEmployeFirstName(@RequestBody int employeId,@RequestBody String newFirstName){
        Employe employe= employeService.getEmployeById(employeId);
        employe.setFirstName(newFirstName);
        employeService.addEmploye(employe);
        return   ResponseEntity.status(HttpStatus.CREATED).body("firstName changé");
    }

    @PostMapping
    public ResponseEntity<?> changeEmployeLastName(@RequestBody int employeId,@RequestBody String newLastName){
        Employe employe= employeService.getEmployeById(employeId);
        employe.setPassword(newLastName);
        employeService.addEmploye(employe);
        return   ResponseEntity.status(HttpStatus.CREATED).body("lastName changé");
    }
    @PostMapping
    public ResponseEntity<?> changeEmployeMail(@RequestBody int employeId,@RequestBody String newMail){
        Employe employe= employeService.getEmployeById(employeId);
        employe.setEmail(newMail);
        employeService.addEmploye(employe);
        return   ResponseEntity.status(HttpStatus.CREATED).body("email changé");
    }




}
