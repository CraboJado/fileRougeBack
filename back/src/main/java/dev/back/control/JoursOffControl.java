package dev.back.control;

import dev.back.DTO.JourOffDTO;
import dev.back.entite.*;
import dev.back.service.AbsenceService;
import dev.back.service.EmployeService;
import dev.back.service.JoursOffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("jouroff")
public class JoursOffControl {

    JoursOffService joursOffService;

    AbsenceService absenceService;

    EmployeService employeService;

    public JoursOffControl(JoursOffService joursOffService, AbsenceService absenceService, EmployeService employeService) {
        this.joursOffService = joursOffService;
        this.absenceService = absenceService;
        this.employeService = employeService;
    }

    @GetMapping
    public List<JoursOff> listAll(){
        return joursOffService.listJoursOff();
    }


    /**
     * permet à un admin de créer des jourFerié ou des RTT_employeur
     *
     * @param jourOffDTO
     * @return ResponseEntity
     *                     created - 201
     */


    @PostMapping()
    public ResponseEntity<?> addJourOff(@RequestBody JourOffDTO jourOffDTO) {
        //TODO Verifier le role user
        JoursOff joursOff = new JoursOff(jourOffDTO.getJour(),jourOffDTO.getTypeJour(),jourOffDTO.getDescription());
        joursOffService.addJourOff(joursOff);
        if(joursOff.getTypeJour().equals(TypeJour.RTT_EMPLOYEUR)){

            for(Employe employe:employeService.listEmployes()) {
                absenceService.addAbsence(new Absence(
                        LocalDateTime.now(),
                        jourOffDTO.getJour(),
                        jourOffDTO.getJour(),
                        Statut.INITIALE,
                        TypeAbsence.RTT_EMPLOYEUR,
                        jourOffDTO.getDescription(),
                        employe
                ));
                }
        }
        return   ResponseEntity.status(HttpStatus.CREATED).body("jour officiel créé");
    }


    /**
     * permet à un admin de changer un jourOff
     *
     * @param joursOff
     * @return ResponseEntity
     *                  ok - 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> changeJourOffDate(@RequestBody JourOffDTO jourOffDTO, @PathVariable("id") int id){
        JoursOff joursOff1=joursOffService.getJourOffById(id);
        joursOff1.setJour(jourOffDTO.getJour());
        joursOff1.setTypeJour(jourOffDTO.getTypeJour());
        joursOff1.setDescription(jourOffDTO.getDescription());
        joursOffService.addJourOff(joursOff1);
        return   ResponseEntity.status(HttpStatus.OK).body("Jour officiel modifié");
    }


    /**
     * permet à un admin de supprimer un jourOff
     *
     * @param jourOffId
     * @return ResponseEntity  ok - 200
     */
    @RequestMapping("/{id}")
    @DeleteMapping
    public ResponseEntity<?> deleteJourOff(@PathVariable("id") String jourOffId){
        JoursOff joursOff=joursOffService.getJourOffById(Integer.parseInt(jourOffId));

        //si un RTT_employeur est supprimé et que les absences etaient validée,
        //il faut rendre le RTT decompté au salarié

        if(joursOff.getTypeJour().equals(TypeJour.RTT_EMPLOYEUR)){
         for(Absence absence:  absenceService.getAbsenceByDate(joursOff.getJour())){
             if(absence.getStatut().equals(Statut.VALIDEE) && absence.getTypeAbsence().equals(TypeAbsence.RTT_EMPLOYEUR)){
                 Employe employe=absence.getEmploye();
                 employe.setSoldeRtt(employe.getSoldeRtt()+1);
             }
            }
        }

        joursOffService.deleteJourOff(Integer.parseInt(jourOffId));
        return   ResponseEntity.status(HttpStatus.OK).body("jour officiel supprimé");
    }


    /**
     * fais appel à l'api du gouvernement pour récuperer les joursferiés officiels.
     * récupère les 5 années précédentes et les 5 années suivantes
     * si il existe déjà un jourOFF à cette date, rien n'est fait.
     * voir schedulingcomponent
     *
     * @return ResponseEntity  created - 201
     */
    @PostMapping
    @RequestMapping("/jourferie")
    public ResponseEntity<?> addJourFerie(){
        int anneeActuelle=LocalDate.now().getYear();
        for (int i = anneeActuelle-5; i < anneeActuelle +5 ; i++) {
            joursOffService.fetchAndSaveJoursFeries(i);
        }


        return   ResponseEntity.status(HttpStatus.CREATED).body("les jours fériés");

    }


}