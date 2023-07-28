package dev.back.service;

import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.entite.JoursOff;
import dev.back.repository.EmployeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeService {
    @Autowired
    EmployeRepo employeRepo;
    public List<Employe> listEmployes() {
        return employeRepo.findAll();
    }
    public Employe employeById(int id){return employeRepo.findById(id).orElseThrow();}
    @Transactional
    public void addEmploye(Employe employe) {
        employeRepo.save(employe);
    }

}
