package dev.back.control;


import dev.back.DTO.DepartementDTO;
import dev.back.entite.Absence;
import dev.back.entite.Departement;
import dev.back.service.DepartementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addDepartement(@RequestBody DepartementDTO departementDTO){
        Departement departement= new Departement(departementDTO.getName());
        departementService.addDepartement(departement);
        return   ResponseEntity.status(HttpStatus.CREATED).body("departement créé");
    }


    @PutMapping
    public ResponseEntity<?>  ChangeDepartement(@RequestBody Departement departement) {
        Departement departement1= departementService.getDepartementById(departement.getId());
        departement1.setName(departement.getName());
        departementService.addDepartement(departement);
        return   ResponseEntity.status(HttpStatus.CREATED).body("département modifié");

  }




    @RequestMapping("/delete")
    @PostMapping
    public ResponseEntity<?> deleteDepartement(@RequestBody int departementid){
       departementService.deleteDepartement(departementid);
        return   ResponseEntity.status(HttpStatus.CREATED).body("départemet supprimé");
    }

}


