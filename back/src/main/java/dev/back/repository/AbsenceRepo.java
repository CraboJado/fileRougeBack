package dev.back.repository;

import dev.back.entite.Absence;
import dev.back.entite.JoursOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepo extends JpaRepository<Absence,Integer> {
}
