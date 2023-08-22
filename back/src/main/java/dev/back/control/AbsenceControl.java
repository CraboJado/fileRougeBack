package dev.back.control;

import dev.back.DTO.AbsenceDTO;
import dev.back.entite.*;
import dev.back.service.AbsenceService;
import dev.back.service.EmailServiceImpl;
import dev.back.service.EmployeService;
import dev.back.service.JoursOffService;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    /**
     *
     * @return liste de toutes les absences de tous les employes de l'entreprise
     */
    //TODO
    @GetMapping
    public List<Absence> listAll(){
        return  absenceService.listAbsences();
    }

    /**
     * permet de d'ajouter une absence en base de donnée
     * seul l'employé connecté peut faire une demande d'absence pour lui-même
     *
     * @param absenceDTO
     * @return ResponseEntity :
     *      *                  Created - 201 si ça marche
     *      *                  Unauthorized - 401 sinon
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<?> addAbsence(@RequestBody AbsenceDTO absenceDTO) throws Exception {

        Employe employe = employeService.getEmployeById(absenceDTO.getEmployeId());

        Employe authEmploye = employeService.getActiveUser();

        if (absenceDTO.getEmployeId() == authEmploye.getId()) {


            if (TypeAbsence.CONGE_SANS_SOLDE.equals(absenceDTO.getTypeAbsence())) {
                if (Objects.equals(absenceDTO.getMotif(), "")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("le motif est obligatoire pour un congé sans solde ");
                }
            }
            Absence absence = new Absence(LocalDateTime.now(), absenceDTO.getDateDebut(), absenceDTO.getDateFin(), absenceDTO.getStatut(), absenceDTO.getTypeAbsence(), absenceDTO.getMotif(), employe);

            List<JoursOff> joursOffList = joursOffService.listJoursOff();

            for (JoursOff joursOff : joursOffList) {
                if (absence.getDateDebut().isEqual(joursOff.getJour()) || absence.getDateFin().isEqual(joursOff.getJour())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("la date de debut ou de fin ne peut pas être un jour férié ou un rtt employeur");
                }
            }

            List<Absence> absenceList = absenceService.listAbsenceByEmploye(absence.getEmploye().getId());
            boolean superpositionDeDate = false;
            List<LocalDate> datesDemandes = new ArrayList<>();
            try {
                datesDemandes = absenceDTO.getDateDebut().datesUntil(absenceDTO.getDateFin()).toList();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("la date de fin ne peut pas être avant celle de début ");
            }

            for (Absence absenceTempo : absenceList) {
                List<LocalDate> datesPrises = absenceTempo.getDateDebut().datesUntil(absenceTempo.getDateFin()).toList();
                for (LocalDate jour : datesPrises) {
                    for (LocalDate jourDemande : datesDemandes) {

                        if (jourDemande.equals(jour)) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("vous avez déjà une demande de congé sur cette période ");

                        }
                    }
                }
            }


            if (absence.getDateCreation().isAfter(absenceDTO.getDateDebut().atTime(LocalTime.now()))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("la date de debut ne peut pas être passée ");
            }
            absence.setStatut(Statut.INITIALE);
            absenceService.addAbsence(absence);
            return ResponseEntity.status(HttpStatus.CREATED).body("absence créée");


        }else{ return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("vous n'êtes pas autorisé à modifier la demande d'absence de quelqu'un d'autre ");}
    }

    /**
     *
     * @param employeId
     * @return liste de tous les employes
     */
    @RequestMapping("/employe")
    @GetMapping
    public List<Absence> listAllByEmploye(
            @RequestParam(name = "id", required = true) int employeId){
        return  absenceService.listAbsenceByEmploye(employeId);
    }

    /**
     *
     * @param departementid
     * @return liste de tous les employes pour un departement donné
     */
    @RequestMapping("/departement")
    @GetMapping
    public List<Absence> listAllByEmployeDepartement(
            @RequestParam(name = "id", required = true) int departementid){
        return  absenceService.listAbsenceByEmployeDepartement(departementid);
    }

    /**
     *
     * @param managerId
     * @return liste de tous les employes ayant le même manager d'id donné
     */
    @RequestMapping("/manager")
    @GetMapping
    public List<Absence> listAllByEmployeManager(
            @RequestParam(name = "id", required = true) int managerId){
        return  absenceService.getAbsenceByEmployeManagaerId(managerId);
    }


    /**
     * change le statut d'une demande d'absence,
     * seul le manager connecté de l'employe qui a fait la demande est autorisé
     *
     * un mail est envoyé à l'employé pour l'informer que sa demande a changé de statut
     *
     * @param absence
     * @param id
     * @return ResponseEntity :
     *                  OK - 200 si ça marche
     *                  Unauthorized - 401 sinon
     */

    @PutMapping("/statut/{id}")
    public ResponseEntity<?> ChangeAbsenceStatut(@RequestBody AbsenceDTO absence, @PathVariable("id") String id) {

        Employe authEmploye = employeService.getActiveUser();
        Employe employe = employeService.getEmployeById(absence.getEmployeId());

        //vérifie que la personne connectée est bien le manager de l'employé qui a fait la demande
        if (authEmploye.getManager().getId() == employe.getManager().getId()) {

            int absenceIdInt = Integer.parseInt(id);

            Absence absence1 = absenceService.getAbsenceById(absenceIdInt);

            int jourtotal = absenceService.nbJourOuvre(absence1);
            System.out.println(jourtotal);

            int nbRttNeeded = 0;
            int nbCongeNeeded = 0;




            absence1.setStatut(absence.getStatut());
            if (absence1.getStatut().equals(Statut.REJETEE)) {
                if (absence1.getTypeAbsence().equals(TypeAbsence.RTT)) {
                    nbRttNeeded = jourtotal;
                }
                if (absence1.getTypeAbsence().equals(TypeAbsence.CONGE_PAYE)) {
                    nbCongeNeeded = jourtotal;
                }
                System.out.println(nbRttNeeded);
                employe.setSoldeConge(employe.getSoldeConge() + nbCongeNeeded);
                employe.setSoldeRtt(employe.getSoldeRtt() + nbRttNeeded);
            }
            employeService.addEmploye(employe);
            absenceService.addAbsence(absence1);//addabsence uses .save() so it will update it if it already exists
            emailService.sendSimpleMail(employe.getEmail(), "le statut de votre demande de congé à été modifié, veuillez vous connnecter a votre compte pour vérifier"
                    + "\n " + "nouveau statut = " + absence.getStatut(), "le statut de votre absence à changé");
            return ResponseEntity.status(HttpStatus.OK).body("statut changé");
        }else {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("seul le manager de cet employé peut changer le statut de son absence");}
    }


    /**
     * change une demande d'absence Initiale ou une absence refusée
     * seul l'employé connecté peut modifier sa demande, il n'a pas le droit d'en modifier le statut.
     *
     *
     *
     * @param absenceDTO
     * @param id
     * @return ResponseEntity :
     *      *                  OK - 200 si ça marche
     *      *                  Unauthorized - 401 sinon
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> ChangeAbsence(@RequestBody AbsenceDTO absenceDTO, @PathVariable("id") String id) {

        Employe authEmploye = employeService.getActiveUser();
        Employe employe = employeService.getEmployeById(absenceDTO.getEmployeId());

        if (authEmploye.getId() == employe.getId()) {

            int absenceIdInt = Integer.parseInt(id);

            Absence absence1 = absenceService.getAbsenceById(absenceIdInt);

            absence1.setTypeAbsence(absenceDTO.getTypeAbsence());
            absence1.setMotif(absenceDTO.getMotif());
            absence1.setDateDebut(absenceDTO.getDateDebut());
            absence1.setDateFin(absenceDTO.getDateFin());
            absence1.setStatut(Statut.INITIALE);
            absence1.setDateCreation(LocalDateTime.now());

            absenceService.addAbsence(absence1);


            return ResponseEntity.status(HttpStatus.OK).body("Absence modifiée");
        }else {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("seul l'employé peut modifier ses demandes d'absence'");}
    }


    /**supprime une absence,
     * seul l'employé connecté peut supprimer ses absences
     *
     * @param absenceId
     * @return ResponseEntity :
     *      *                  OK - 200 si ça marche
     *      *                  Unauthorized - 401 sinon
     */
    @RequestMapping("/{id}")
    @DeleteMapping
    public ResponseEntity<?> deleteAbsence(@PathVariable("id") String absenceId) {
        Employe authEmploye = employeService.getActiveUser();
        Absence absence = absenceService.getAbsenceById(Integer.parseInt(absenceId));
        Employe employe = absence.getEmploye();
        if (authEmploye.getId() == employe.getId()) {

            int jourTotal =absenceService.nbJourOuvre(absenceService.getAbsenceById(Integer.parseInt(absenceId)));
            if(absence.getStatut().equals(Statut.VALIDEE)){
                if(absence.getTypeAbsence().equals(TypeAbsence.RTT)){
                    employe.setSoldeRtt(employe.getSoldeRtt()+jourTotal);
                }
                if(absence.getTypeAbsence().equals(TypeAbsence.CONGE_PAYE)){
                    employe.setSoldeConge(employe.getSoldeConge()+jourTotal);
                }

            }


            absenceService.deleteAbsence(Integer.parseInt(absenceId));
            return ResponseEntity.status(HttpStatus.OK).body("Absence supprimé");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("seul l'employé concerné peut supprimer une demande d'absence");

        }
    }







}
