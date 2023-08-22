package dev.back.onStartUp;


import dev.back.entite.*;
import dev.back.service.AbsenceService;
import dev.back.service.DepartementService;
import dev.back.service.EmployeService;
import dev.back.service.JoursOffService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class StartUp{

    EmployeService employeService;
    DepartementService departementService;
    AbsenceService absenceService;

    JoursOffService joursOffService;

    PasswordEncoder passwordEncoder;

    public StartUp(EmployeService employeService, DepartementService departementService, AbsenceService absenceService, JoursOffService joursOffService, PasswordEncoder passwordEncoder) {
        this.employeService = employeService;
        this.departementService = departementService;
        this.absenceService = absenceService;
        this.joursOffService = joursOffService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {



        departementService.addDepartement(new Departement("ressources humaines"));
        departementService.addDepartement(new Departement("informatique"));
        departementService.addDepartement(new Departement("commercial"));
        departementService.addDepartement(new Departement("magasinier"));


        List<String> roles = new ArrayList<>();
        roles.add("MANAGER");
        roles.add("ADMIN");
        employeService.addEmploye(new Employe("passwordtout","lastname1",passwordEncoder.encode("passwordtout"),10,10,"exampletout@gmail.com",roles,departementService.getDepartementById(1),null));


        roles.remove(1);
        employeService.addEmploye(new Employe("passwordmanager","lastname2",passwordEncoder.encode("passwordmanager"),10,10,"examplemanager@gmail.com",roles,departementService.getDepartementById(1),employeService.getEmployeById(1)));

        roles.remove(0);
        employeService.addEmploye(new Employe("passwordemploye","lastnam3",passwordEncoder.encode("passwordemploye"),10,10,"exampleemploye@gmail.com",null,departementService.getDepartementById(1),employeService.getEmployeById(2)));


        roles.add("ADMIN");
        employeService.addEmploye(new Employe("passwordadmin","lastnam4",passwordEncoder.encode("passwordadmin"),10,10,"exampleadmin@gmail.com",roles,departementService.getDepartementById(2),employeService.getEmployeById(1)));

        employeService.addEmploye(new Employe("passwordemploye1","lastname5",passwordEncoder.encode("passwordemploye1"),10,10,"exampleemploye1@gmail.com",null,departementService.getDepartementById(1),employeService.getEmployeById(2)));

        employeService.addEmploye(new Employe("passwordemploye2","lastname6",passwordEncoder.encode("passwordemploye2"),10,10,"exampleemploye2@gmail.com",null,departementService.getDepartementById(1),employeService.getEmployeById(1)));


        joursOffService.fetchAndSaveJoursFeries(2023);



        absenceService.addAbsence(new Absence(LocalDateTime.parse("2023-10-10T01:02:04"),LocalDate.parse("2023-11-02"),LocalDate.parse("2023-11-10"),Statut.INITIALE,TypeAbsence.RTT,"",employeService.getEmployeById(3)));

        absenceService.addAbsence(new Absence(LocalDateTime.parse("2023-10-10T01:02:04"),LocalDate.parse("2022-11-02"),LocalDate.parse("2022-11-10"),Statut.VALIDEE,TypeAbsence.CONGE_PAYE,"",employeService.getEmployeById(1)));
        absenceService.addAbsence(new Absence(LocalDateTime.parse("2023-12-10T01:02:04"),LocalDate.parse("2025-01-02"),LocalDate.parse("2025-01-10"),Statut.VALIDEE,TypeAbsence.CONGE_PAYE,"",employeService.getEmployeById(1)));

        absenceService.addAbsence(new Absence(LocalDateTime.parse("2023-10-10T01:02:04"),LocalDate.parse("2023-12-02"),LocalDate.parse("2024-11-10"),Statut.INITIALE,TypeAbsence.CONGE_SANS_SOLDE,"testmotif",employeService.getEmployeById(2)));




        }



}
