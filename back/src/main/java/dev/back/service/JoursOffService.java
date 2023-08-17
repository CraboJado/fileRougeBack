package dev.back.service;

import dev.back.entite.JoursOff;
import dev.back.repository.JoursOffRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JoursOffService {
    @Autowired

    JoursOffRepo joursOffRepo;
    public List<JoursOff> listJoursOff() {
        return joursOffRepo.findAll();
    }



    public JoursOff jourOffByDatee(LocalDate jour){
        return  joursOffRepo.findByJour(jour).orElseThrow();
    }

    @Transactional
    public void addJourOff(JoursOff joursOff) {
        joursOffRepo.save(joursOff);
    }

    public void deleteJourOff(JoursOff joursOff){joursOffRepo.delete(joursOff);}

    public JoursOff jourOffById(int id){return joursOffRepo.findById(id).orElseThrow();}

    public void deleteJourOff(int id){joursOffRepo.delete(joursOffRepo.findById(id).orElseThrow());}

}
