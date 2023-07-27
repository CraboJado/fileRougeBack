package dev.back.service;

import dev.back.entite.Absence;
import dev.back.entite.Departement;
import dev.back.entite.JoursOff;
import dev.back.repository.AbsenceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsenceService {
    @Autowired
    AbsenceRepo absenceRepo;
    public List<Absence> listAbsences() {
        return absenceRepo.findAll();
    }

    @Transactional
    public void addAbsence(Absence absence) {
        absenceRepo.save(absence);
    }


}
