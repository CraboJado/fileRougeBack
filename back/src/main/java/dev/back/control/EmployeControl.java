package dev.back.control;

import dev.back.DTO.EmployeDTO;
import dev.back.entite.Employe;
import dev.back.service.EmployeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("employe")
public class EmployeControl {

    EmployeService employeService;
    private PasswordEncoder passwordEncoder;

    public EmployeControl(EmployeService employeService) {
        this.employeService = employeService;
    }

    @GetMapping
    public List<Employe> listAll(){
        return employeService.listEmployes();
    }


    @PostMapping
    public void addEmploye(@RequestBody EmployeDTO employeDTO){
        //TODO hasher MOT DE PASS
        String pswEncoded = passwordEncoder.encode(employeDTO.getPassWord());
        Employe employe=new Employe(employeDTO.getFirstName(), employeDTO.getLastName(), pswEncoded, employeDTO.getSoldeConge(), employeDTO.getSoldeRtt(), employeDTO.getEmail(), employeDTO.getRole(), employeDTO.getDepartement(), employeDTO.getManager());
        employeService.addEmploye(employe);
    }

}
