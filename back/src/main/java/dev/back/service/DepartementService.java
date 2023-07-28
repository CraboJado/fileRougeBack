package dev.back.service;

import dev.back.entite.Departement;
import dev.back.entite.JoursOff;
import dev.back.repository.DepartementRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    DepartementRepo departementRepo;

    public List<Departement> listDepartements() {
        return departementRepo.findAll();
    }
    public Departement departementById(int id){ return  departementRepo.findById(id).orElseThrow();}

    @Transactional
    public void addDepartement(Departement departement) {
        departementRepo.save(departement);
    }

}
