package dev.back.service;

import dev.back.entite.Absence;
import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.entite.JoursOff;
import dev.back.repository.AbsenceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


    public List<Absence> listAbsenceyEmploye(int id){
        return absenceRepo.getAbsenceByEmploye_Id(id);
    }
    public List<Absence> listAbsenceyEmployeDepartement(int id){
        return absenceRepo.getAbsenceByEmploye_Departement_Id(id);
    }

    public Absence getAbsenceById(Integer id){
        Optional<Absence> absenceOp = absenceRepo.findById(id);
        // else throw new Exception()
        return absenceOp.orElse(null);

    }


}
