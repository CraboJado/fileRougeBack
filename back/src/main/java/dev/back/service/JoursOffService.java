package dev.back.service;

import dev.back.entite.JoursOff;
import dev.back.repository.JoursOffRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
