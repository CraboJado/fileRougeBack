package dev.back.control;

import dev.back.DTO.AbsenceDTO;
import dev.back.entite.Absence;
import dev.back.entite.Employe;
import dev.back.entite.Statut;
import dev.back.service.AbsenceService;
import dev.back.service.EmployeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("absence")
public class AbsenceControl {
    AbsenceService absenceService;
    EmployeService employeService;

    public AbsenceControl(AbsenceService absenceService, EmployeService employeService) {
        this.absenceService = absenceService;
        this.employeService = employeService;
    }

    @GetMapping
    public List<Absence> listAll(){
        return  absenceService.listAbsences();
    }



    @PostMapping
    public void addAbsence(@RequestBody AbsenceDTO absenceDTO){
        //TODO regarder règle métier
        Employe employe = employeService.getEmployeById(absenceDTO.getEmployeId());
        Absence absence=new Absence(absenceDTO.getDateDebut(),absenceDTO.getDateFin(),absenceDTO.getStatut(),absenceDTO.getTypeAbsence(),employe);
        absenceService.addAbsence(absence);
    }


    @GetMapping
    public List<Absence> listAllByEmploye(
            @RequestParam(name = "id", required = false) int id){
        return  absenceService.listAbsenceyEmploye(id);
    }

    @GetMapping
    public List<Absence> listAllByEmployeDepartement(
            @RequestParam(name = "departementId", required = false) int id){
        return  absenceService.listAbsenceyEmployeDepartement(id);
    }


     @PostMapping
    public void ChangeAbsence(@RequestBody int absenceId, @RequestBody Statut newStatut) {

        Absence absence = absenceService.getAbsenceById(absenceId);
        absence.setStatut(newStatut);
        absenceService.addAbsence(absence);//addabsence uses .save() so it will update it if it already exists

     }

}
