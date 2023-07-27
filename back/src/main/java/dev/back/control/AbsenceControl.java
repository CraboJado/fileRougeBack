package dev.back.control;

import dev.back.DTO.AbsenceDTO;
import dev.back.entite.Absence;
import dev.back.service.AbsenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("absence")
public class AbsenceControl {
    AbsenceService absenceService;

    public AbsenceControl(AbsenceService absenceService) {
        this.absenceService = absenceService;
    }


    @GetMapping
    public List<Absence> listAll(){
      return  absenceService.listAbsences();
    }

    @PostMapping
    public void addAbsence(AbsenceDTO absenceDTO){
        Absence absence=new Absence(absenceDTO.getDateDebut(),absenceDTO.getDateFin(),absenceDTO.getStatut(),absenceDTO.getTypeAbsence(),absenceDTO.getEmploye());
        absenceService.addAbsence(absence);
    }
}
