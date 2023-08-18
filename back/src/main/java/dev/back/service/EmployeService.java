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
        // else throw new Exception()
        return EmployeOp.orElse(null);

    }


    public void deleteEmploye(int id){employeRepo.delete(employeRepo.findById(id).orElseThrow());}



    public List<Employe> getEmployeByIdMnager(Integer id){



        // else throw new Exception()
        return employeRepo.findAllByManager_Id(id);

    }

    public Employe getEmployeByEmail(String email){
        return employeRepo.findByEmail(email).orElseThrow();
    }

    @Transactional
    public void addEmploye(Employe employe) {
        employeRepo.save(employe);

    }

}
