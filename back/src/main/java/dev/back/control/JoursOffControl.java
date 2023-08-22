package dev.back.control;

import dev.back.DTO.JourOffDTO;
import dev.back.entite.JoursOff;
import dev.back.entite.TypeJour;
import dev.back.service.JoursOffService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("jouroff")
public class JoursOffControl {

    JoursOffService joursOffService;

    public JoursOffControl(JoursOffService joursOffService) {
        this.joursOffService = joursOffService;
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
        return   ResponseEntity.status(HttpStatus.CREATED).body("jour officiel créé");
    }


    /**
     * permet à un admin de changer un jourOff
     *
     * @param joursOff
     * @return ResponseEntity
     *                  ok - 200
     */
    @PutMapping
    public ResponseEntity<?> changeJourOffDate(@RequestBody JoursOff joursOff){
        JoursOff joursOff1=joursOffService.jourOffById(joursOff.getId());
        joursOff1.setJour(joursOff.getJour());
        joursOff1.setTypeJour(joursOff.getTypeJour());
        joursOff1.setDescription(joursOff.getDescription());
        joursOffService.addJourOff(joursOff1);
        return   ResponseEntity.status(HttpStatus.OK).body("date changée");
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
