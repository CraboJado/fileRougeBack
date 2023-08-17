package dev.back.service;

import dev.back.entite.JoursOff;
import dev.back.repository.JoursOffRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JoursOffService {
    @Autowired

    JoursOffRepo joursOffRepo;
    public List<JoursOff> listJoursOff() {
        return joursOffRepo.findAll();
    }


    @Transactional
    public void addJourOff(JoursOff joursOff) {
        joursOffRepo.save(joursOff);
    }

    public void deleteJourOff(JoursOff joursOff){joursOffRepo.delete(joursOff);}

    public JoursOff JourOffById(int id){return joursOffRepo.findById(id).orElseThrow();}

    public void deleteJourOff(int id){joursOffRepo.delete(joursOffRepo.findById(id).orElseThrow());}

}
