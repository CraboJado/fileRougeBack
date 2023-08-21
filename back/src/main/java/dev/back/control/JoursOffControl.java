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

    @PostMapping()
    public ResponseEntity<?> addJourOff(@RequestBody JourOffDTO jourOffDTO) {
        //TODO Verifier le role user
        JoursOff joursOff = new JoursOff(jourOffDTO.getJour(),jourOffDTO.getTypeJour(),jourOffDTO.getDescription());
        joursOffService.addJourOff(joursOff);
        return   ResponseEntity.status(HttpStatus.CREATED).body("jour officiel créé");
    }


    @PutMapping
    public ResponseEntity<?> changeJourOffDate(@RequestBody JoursOff joursOff){
        JoursOff joursOff1=joursOffService.jourOffById(joursOff.getId());
        joursOff1.setJour(joursOff.getJour());
        joursOff1.setTypeJour(joursOff.getTypeJour());
        joursOff1.setDescription(joursOff.getDescription());
        joursOffService.addJourOff(joursOff1);
        return   ResponseEntity.status(HttpStatus.CREATED).body("date changée");
    }



    @RequestMapping("/delete")
    @PostMapping
    public ResponseEntity<?> deleteJourOff(@RequestBody int jourOffId){
        joursOffService.deleteJourOff(jourOffId);
        return   ResponseEntity.status(HttpStatus.CREATED).body("jour officiel supprimé");
    }

    @PostMapping
    @RequestMapping("/jourferie")
    public ResponseEntity<?> addJourFerie(){
        joursOffService.fetchAndSaveJoursFeries(2023);
        return   ResponseEntity.status(HttpStatus.CREATED).body("les jours fériés");

    }


}
