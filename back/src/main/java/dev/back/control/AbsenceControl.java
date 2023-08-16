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
        //TODO return un list absence d'un employé
        return  absenceService.listAbsences();
    }

    //TODO Method get list congés futures (
    //TODO Method get list congés consommés  ( une absence
    //TODO pour un MANAGER, list de ses collaborateur present et absentes

    @PostMapping
    public void addAbsence(@RequestBody AbsenceDTO absenceDTO){
        //TODO regarder règle métier
        Employe employe = employeService.getEmployeById(absenceDTO.getEmployeId());

        Absence absence=new Absence(
                absenceDTO.getDateDebut(),
                absenceDTO.getDateFin(),
                absenceDTO.getStatut(),
                absenceDTO.getTypeAbsence(),
                employe);

        absenceService.addAbsence(absence);
    }

    //TODO validerAbsence par Manager :  MODIFIER STATUS DE L'ABSENCE de ses collaborateurs
    //TODO @PutMapping
    //TODO ATTENTION : 1. status est EN_ATTENTE ( traitement de nuit) , 2 L'ABSENCE de ses collaborateurs


    // TODO pour EMPLOYE : modifier une demande d'absence au statut INITIALE ou REJETEE
    //  @PutMapping

    // TODO pour EMPLOYE : supprimer une demande d'absence quel que soit le statut
}
