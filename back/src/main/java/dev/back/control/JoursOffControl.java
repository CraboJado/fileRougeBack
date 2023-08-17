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
    public void addJourOff(@RequestBody JourOffDTO jourOffDTO) {
        //TODO Verifier le role user
        JoursOff joursOff = new JoursOff(jourOffDTO.getJour(),jourOffDTO.getTypeJour());
        joursOffService.addJourOff(joursOff);
    }

    //TODO route PUT pour modifier JourOFF
    //TODO route DELETE pour supprimer JourOFF

    @PostMapping
    public void changeJourOffDate(@RequestBody int jourOffId,@RequestBody LocalDate newDate){
        JoursOff joursOff=joursOffService.JourOffById(jourOffId);
        joursOff.setJour(newDate);
    }

    @PostMapping
    public void changeJourOffType(@RequestBody int jourOffId,@RequestBody TypeJour newType){
        JoursOff joursOff=joursOffService.JourOffById(jourOffId);
        joursOff.setTypeJour(newType);
    }


    @PostMapping
    public void deleteJourOff(@RequestBody int jourOffId){
        joursOffService.deleteJourOff(jourOffId);
    }

}
