package dev.back.control;

import dev.back.DTO.AbsenceDTO;
import dev.back.entite.*;
import dev.back.service.AbsenceService;
import dev.back.service.EmailServiceImpl;
import dev.back.service.EmployeService;
import dev.back.service.JoursOffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@CrossOrigin
@RestController
@RequestMapping("absence")
public class AbsenceControl {
    AbsenceService absenceService;
    EmployeService employeService;

    JoursOffService joursOffService;

    EmailServiceImpl emailService;

    public AbsenceControl(AbsenceService absenceService, EmployeService employeService, JoursOffService joursOffService, EmailServiceImpl emailService) {
        this.absenceService = absenceService;
        this.employeService = employeService;
        this.joursOffService = joursOffService;
        this.emailService = emailService;
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


        if(absence.getDateCreation().isAfter(absenceDTO.getDateDebut().atTime(LocalTime.now()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("la date de debut ne peut pas être passée ");
        }

        if(absenceDTO.getDateFin().isBefore(absenceDTO.getDateDebut())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("la date de fin ne peut pas être avant celle de début ");
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









    @PutMapping("/{id}")
    public ResponseEntity<?> ChangeAbsenceStatut(@RequestBody AbsenceDTO absence, @PathVariable("id") String id) {

        System.out.println(id);

        int absenceIdInt= Integer.parseInt(id);

        Absence absence1 = absenceService.getAbsenceById(absenceIdInt);

        long nombreDeJour = DAYS.between(absence.getDateDebut(), absence.getDateFin());

        List<LocalDate> listeJourAbsence = absence.getDateDebut().datesUntil(absence.getDateFin().plusDays(1)).toList();
        for (LocalDate date:listeJourAbsence){
            System.out.println(date);
        }

        System.out.println(listeJourAbsence.size());
        Employe employe = employeService.getEmployeById(absence.getEmployeId());

        int nbRttNeeded=0;
        int nbCongeNeeded=0;

        for(LocalDate jour : listeJourAbsence) {

            if (!jour.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !jour.getDayOfWeek().equals(DayOfWeek.SATURDAY)){

                List<JoursOff> jourOffs= joursOffService.listJoursOff();

                for(JoursOff joursOff:jourOffs){

                    if(!jour.equals(joursOff.getJour())){

                        if(absence.getTypeAbsence().equals(TypeAbsence.RTT)){

                            employe.setSoldeRtt(employe.getSoldeRtt()-1);
                            nbRttNeeded++;

                        }


                        if(absence.getTypeAbsence().equals(TypeAbsence.CONGE_PAYE)){

                            employe.setSoldeRtt(employe.getSoldeConge()-1);
                            nbCongeNeeded++;

                        }



                    }
                }


            }


        }





        System.out.println(employe.getSoldeRtt());

            absence1.setStatut(absence.getStatut());
            if(absence1.getStatut().equals(Statut.REJETEE)){
                employe.setSoldeConge(employe.getSoldeConge()+nbCongeNeeded);
                employe.setSoldeRtt(employe.getSoldeRtt()+nbRttNeeded);
            }
        employeService.addEmploye(employe);

            absenceService.addAbsence(absence1);//addabsence uses .save() so it will update it if it already exists


        emailService.sendSimpleMail("antoine.ligerot@outlook.fr","le statut de votre demande de congé à été modifié, veuillez vous connnecter a votre compte pour vérifier"
        + "\n " + "nouveau statut = " + absence.getStatut() );


        return ResponseEntity.status(HttpStatus.CREATED).body("statut changé");


        }

    @RequestMapping("/delete")
    @PostMapping
    public ResponseEntity<?> deleteAbsence(@RequestBody int absenceId){
        absenceService.deleteAbsence(absenceId);
        return   ResponseEntity.status(HttpStatus.CREATED).body("jour officiel supprimé");
    }

}
