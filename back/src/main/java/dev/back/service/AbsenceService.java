package dev.back.service;

import dev.back.entite.*;
import dev.back.repository.AbsenceRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AbsenceService {
    @Autowired
    AbsenceRepo absenceRepo;
    @Autowired
    JoursOffService joursOffService;
    public List<Absence> listAbsences() {
        return absenceRepo.findAll();
    }

    @Transactional
    public void addAbsence(Absence absence) {
        absenceRepo.save(absence);
    }


    public List<Absence> listAbsenceByEmploye(int id){
        return absenceRepo.getAbsenceByEmploye_Id(id);
    }
    public List<Absence> listAbsenceByEmployeDepartement(int id){
        return absenceRepo.getAbsenceByEmploye_Departement_Id(id);
    }

    public Absence getAbsenceById(Integer id){
        Optional<Absence> absenceOp = absenceRepo.findById(id);
        return  absenceOp.orElseThrow();
    }


    public void deleteAbsence(int id){absenceRepo.delete(absenceRepo.findById(id).orElseThrow());}

    public List<Absence> getAbsenceByEmployeManagaerId(int id){
        return absenceRepo.getAbsenceByEmploye_Manager_Id(id);
    }

    public int nbJourOuvre(Absence absence){
        int jourTotal =0;
        long nombreDeJour = DAYS.between(absence.getDateDebut(), absence.getDateFin());
        List<LocalDate> listeJourAbsence = absence.getDateDebut().datesUntil(absence.getDateFin().plusDays(1)).toList();
        for(LocalDate jour : listeJourAbsence) {

            if (!jour.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !jour.getDayOfWeek().equals(DayOfWeek.SATURDAY)){

                List<JoursOff> jourOffs= joursOffService.listJoursOff();
                List<LocalDate> listeDateFerie= new ArrayList<>();

                for(JoursOff joursOff:jourOffs){
                    listeDateFerie.add(joursOff.getJour());
                }
                if(!listeDateFerie.contains(jour)){

                    if(absence.getTypeAbsence().equals(TypeAbsence.RTT)){
                        jourTotal++;
                    }
                    if(absence.getTypeAbsence().equals(TypeAbsence.CONGE_PAYE)){
                        jourTotal++;
                    }
                }
            }
        }

        return  jourTotal;
    }
}
