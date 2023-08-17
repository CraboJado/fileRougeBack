package dev.back.control;

import dev.back.DTO.AbsenceDTO;
import dev.back.entite.Absence;
import dev.back.entite.Employe;
import dev.back.entite.Statut;
import dev.back.entite.TypeAbsence;
import dev.back.service.AbsenceService;
import dev.back.service.EmployeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<?> addAbsence(@RequestBody AbsenceDTO absenceDTO) throws Exception {
        //TODO regarder règle métier
        Employe employe = employeService.getEmployeById(absenceDTO.getEmployeId());
        if(TypeAbsence.CONGE_SANS_SOLDE.equals(absenceDTO.getTypeAbsence())) {
            if(Objects.equals(absenceDTO.getMotif(), "")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("le motif est obligatoire pour un congé sans solde ");
            }
        }
        Absence absence=new Absence(LocalDateTime.now(),absenceDTO.getDateDebut(),absenceDTO.getDateFin(),absenceDTO.getStatut(),absenceDTO.getTypeAbsence(),absenceDTO.getMotif(),employe);


        if(absence.getDateCreation().isBefore(absenceDTO.getDateDebut())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("la date de debut ne peut pas être passée ");

        }
        absence.setStatut(Statut.INITIALE);
        absenceService.addAbsence(absence);
        return ResponseEntity.status(HttpStatus.CREATED).body("absence créée");
    }


    @RequestMapping("/employe")
    @GetMapping
    public List<Absence> listAllByEmploye(
            @RequestParam(name = "id", required = true) int id){
        return  absenceService.listAbsenceByEmploye(id);
    }

    @RequestMapping("/departement")
    @GetMapping
    public List<Absence> listAllByEmployeDepartement(
            @RequestParam(name = "id", required = true) int id){
        return  absenceService.listAbsenceByEmployeDepartement(id);
    }


    @RequestMapping("/manager")
    @GetMapping
    public List<Absence> listAllByEmployeManager(
            @RequestParam(name = "id", required = true) int id){
        return  absenceService.getAbsenceByEmployeManagaerId(id);
    }




    @RequestMapping("/modifier")
    @PostMapping
    public ResponseEntity<?> ChangeAbsenceStatut(@RequestBody Absence absence) {
        Absence absence1 = absenceService.getAbsenceById(absence.getId());

        absence1.setStatut(absence.getStatut());
        absenceService.addAbsence(absence1);//addabsence uses .save() so it will update it if it already exists
        return   ResponseEntity.status(HttpStatus.CREATED).body("statut changé");
    }
}