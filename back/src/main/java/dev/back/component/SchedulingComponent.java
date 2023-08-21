package dev.back.component;

import dev.back.entite.*;
import dev.back.service.AbsenceService;
import dev.back.service.EmailServiceImpl;
import dev.back.service.EmployeService;
import dev.back.service.JoursOffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

EmailServiceImpl emailServiceImpl;

    public SchedulingComponent(AbsenceService absenceService, EmployeService employeService, JoursOffService joursOffService, EmailServiceImpl emailService) {
        this.absenceService = absenceService;
        this.employeService = employeService;
        this.joursOffService = joursOffService;
        this.emailServiceImpl = emailService;
    }

    @Scheduled(cron="0 0 0 1 1 ?")
    public void UpdateJourFerie(){

        int anneeActuelle=LocalDate.now().getYear();
        for (int i = anneeActuelle-5; i < anneeActuelle +5 ; i++) {
            joursOffService.fetchAndSaveJoursFeries(i);
        }


    }

    @Scheduled(cron="@midnight")
    public void TraitementDeNuit(){

        System.out.println("traitement de nuit");

         List<Absence> absences= absenceService.listAbsences().stream().filter(absence -> absence.getStatut().equals(Statut.INITIALE)).toList();


        for(Absence absence:absences){
            int jourOuvre=absenceService.nbJourOuvre(absence);
            int nbRTTNeeded=0;
            int nbCongeNeeded=0;
            Employe employe = absence.getEmploye();


       if(absence.getEmploye().getSoldeConge()<nbCongeNeeded || absence.getEmploye().getSoldeRtt()<nbRTTNeeded){
           absence.setStatut(Statut.REJETEE);
           emailServiceImpl.sendSimpleMail(employe.getEmail(),"Bonjour "+ employe.getFirstName()+" "+ employe.getLastName() +"\n votre demande a été refusée par le traitement de nuit","Absence Refusée automatiquement");

           absenceService.addAbsence(absence);

       }else{
           absence.setStatut(Statut.EN_ATTENTE);


           if (absence.getStatut().equals(Statut.REJETEE)) {
               if (absence.getTypeAbsence().equals(TypeAbsence.RTT)) {
                   nbRTTNeeded = jourOuvre;
               }
               if (absence.getTypeAbsence().equals(TypeAbsence.CONGE_PAYE)) {
                   nbCongeNeeded = jourOuvre;
               }
               employe.setSoldeConge(employe.getSoldeConge() + nbCongeNeeded);
               employe.setSoldeRtt(employe.getSoldeRtt() + nbRTTNeeded);
           }

           emailServiceImpl.sendSimpleMail(employe.getManager().getEmail(),"la demande d'absence de "+ employe.getLastName() +" a été validée par le traitement de nuit, en attente de votre validation","Absence en attente de validation");
           absenceService.addAbsence(absence);
           employe.setSoldeRtt(employe.getSoldeRtt()-nbRTTNeeded);
           employe.setSoldeConge(employe.getSoldeConge()-nbCongeNeeded);
           employeService.addEmploye(employe);
            }
        }
    }
}
