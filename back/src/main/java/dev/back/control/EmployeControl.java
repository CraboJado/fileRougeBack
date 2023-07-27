package dev.back.control;

import dev.back.DTO.EmployeDTO;
import dev.back.entite.Employe;
import dev.back.service.EmployeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("employe")
public class EmployeControl {

    EmployeService employeService;

    public EmployeControl(EmployeService employeService) {
        this.employeService = employeService;
    }

    @GetMapping
    public List<Employe> listAll(){
        return employeService.listEmployes();
    }


    @PostMapping
    public void addEmploye(EmployeDTO employeDTO){
        Employe employe=new Employe(employeDTO.getFirstName(), employeDTO.getLastName(), employeDTO.getPassWord(), employeDTO.getSoldeConge(), employeDTO.getSoldeRtt(), employeDTO.getEmail(), employeDTO.getRole(), employeDTO.getDepartement(), employeDTO.getManager());
        employeService.addEmploye(employe);
    }

}
