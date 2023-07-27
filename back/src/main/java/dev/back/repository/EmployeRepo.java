package dev.back.repository;

import dev.back.entite.Employe;
import dev.back.entite.JoursOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepo extends JpaRepository<Employe,Integer> {
}
