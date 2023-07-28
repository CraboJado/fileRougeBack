package dev.back.control;

import dev.back.DTO.EmployeDTO;
import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.service.DepartementService;
import dev.back.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("employe")
public class EmployeControl {

    EmployeService employeService;
    DepartementService departementService;
    private PasswordEncoder passwordEncoder;

    @Autowired
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
    public void addEmploye(@RequestBody EmployeDTO employeDTO){
        String pswEncoded = passwordEncoder.encode(employeDTO.getPassword());
        Employe manager= employeService.employeById(employeDTO.getManagerId());
        Departement departement=departementService.departementById(employeDTO.getDepartementId());
        Employe employe=new Employe(employeDTO.getFirstName(),
                employeDTO.getLastName(),
                pswEncoded,
                employeDTO.getSoldeConge(),
                employeDTO.getSoldeRtt(),
                employeDTO.getEmail(),
                employeDTO.getRole(),
                departement,
                manager);
        employeService.addEmploye(employe);
    }

}
