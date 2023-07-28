package dev.back.service;

import dev.back.entite.Departement;
import dev.back.entite.Employe;
import dev.back.entite.JoursOff;
import dev.back.repository.EmployeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeService {
    @Autowired
    EmployeRepo employeRepo;
    public List<Employe> listEmployes() {
        return employeRepo.findAll();
    }

    public Employe getEmployeById(Integer id){
        Optional<Employe> EmployeOp = employeRepo.findById(id);
        if(EmployeOp.isPresent()) return EmployeOp.get();
        // else throw new Exception()
        else return null;

    }
    @Transactional
    public void addEmploye(Employe employe) {
        employeRepo.save(employe);
    }

}
