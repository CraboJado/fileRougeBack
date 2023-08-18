package dev.back.component;

import dev.back.entite.*;
import dev.back.service.AbsenceService;
import dev.back.service.EmployeService;
import dev.back.service.JoursOffService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class SchedulingComponent {


    AbsenceService absenceService;

    EmployeService employeService;
JoursOffService joursOffService;

    public SchedulingComponent(AbsenceService absenceService, EmployeService employeService, JoursOffService joursOffService) {
        this.absenceService = absenceService;
        this.employeService = employeService;
        this.joursOffService = joursOffService;
    }

    @Scheduled(cron="@midnight")
    public void TraitementDeNuit(){

        System.out.println("traitement de nuit");

         List<Absence> absences= absenceService.listAbsences().stream().filter(absence -> absence.getStatut().equals(Statut.INITIALE)).toList();

        for(Absence absence:absences){
            int nbRTTNeeded=0;
            int nbCongeNeeded=0;

            List<LocalDate> listeJourAbsence = absence.getDateDebut().datesUntil(absence.getDateFin().plusDays(1)).toList();




            for(LocalDate jour : listeJourAbsence) {

                if (!jour.getDayOfWeek().equals(DayOfWeek.SUNDAY) && !jour.getDayOfWeek().equals(DayOfWeek.SATURDAY)){

                    List<JoursOff> jourOffs= joursOffService.listJoursOff();

                    for(JoursOff joursOff:jourOffs){

                        if(!jour.equals(joursOff.getJour())){

                            if(absence.getTypeAbsence().equals(TypeAbsence.RTT)){

                                nbRTTNeeded++;

                            }


                            if(absence.getTypeAbsence().equals(TypeAbsence.CONGE_PAYE)){

                                nbCongeNeeded++;

                            }
                        }
                    }
                }
            }

       if(absence.getEmploye().getSoldeConge()<nbCongeNeeded || absence.getEmploye().getSoldeRtt()<nbRTTNeeded){
           absence.setStatut(Statut.REJETEE);
           //TODO envoyer un mail a l'employe.
           absenceService.addAbsence(absence);

       }else{
           absence.setStatut(Statut.EN_ATTENTE);
           Employe employe = absence.getEmploye();
           //TODO envoyer un mail a l'employe.
           absenceService.addAbsence(absence);
           employe.setSoldeRtt(employe.getSoldeRtt()-nbRTTNeeded);
           employe.setSoldeConge(employe.getSoldeConge()-nbCongeNeeded);
           employeService.addEmploye(employe);
            }
        }
    }
}
