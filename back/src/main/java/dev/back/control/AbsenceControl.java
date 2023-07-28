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

//    public AbsenceControl(AbsenceService absenceService) {
//        this.absenceService = absenceService;
//    }

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
        Employe employe = employeService.getEmployeById(absenceDTO.getEmployeId());
        System.out.println("==============++++++++++++" + employe.getEmail());

        Absence absence=new Absence(absenceDTO.getDateDebut(),absenceDTO.getDateFin(),absenceDTO.getStatut(),absenceDTO.getTypeAbsence(),employe);
        absenceService.addAbsence(absence);
    }
}
