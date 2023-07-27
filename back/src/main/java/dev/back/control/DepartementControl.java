package dev.back.control;

import dev.back.DTO.DepartementDTO;
import dev.back.entite.Departement;
import dev.back.service.DepartementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("departement")
public class DepartementControl {

    DepartementService departementService;



    public DepartementControl(DepartementService departementService) {
        this.departementService = departementService;
    }

    @GetMapping
    public List<Departement> listAll(){
       return departementService.listDepartements();
    }

    @PostMapping
    public void addDepartement(@RequestBody DepartementDTO departementDTO){
        Departement departement= new Departement(departementDTO.getName());
        departementService.addDepartement(departement);
    }
}
