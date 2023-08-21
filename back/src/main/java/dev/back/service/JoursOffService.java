package dev.back.service;

import dev.back.entite.JoursOff;
import dev.back.entite.TypeJour;
import dev.back.repository.JoursOffRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class JoursOffService {
    @Autowired

    JoursOffRepo joursOffRepo;
    public List<JoursOff> listJoursOff() {
        return joursOffRepo.findAll();
    }



    public JoursOff jourOffByDatee(LocalDate jour){
        return  joursOffRepo.findByJour(jour).orElseThrow();
    }

    @Transactional
    public void addJourOff(JoursOff joursOff) {
        joursOffRepo.save(joursOff);
    }

    public void deleteJourOff(JoursOff joursOff){joursOffRepo.delete(joursOff);}

    public JoursOff jourOffById(int id){return joursOffRepo.findById(id).orElseThrow();}

    public void deleteJourOff(int id){joursOffRepo.delete(joursOffRepo.findById(id).orElseThrow());}

// Assume you have a repository for JourFerie

    public void fetchAndSaveJoursFeries(int year) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://calendrier.api.gouv.fr/jours-feries/metropole/{annee}.json";
        String apiUrlWithYear = apiUrl.replace("{annee}", String.valueOf(year));
        Map<String, String> joursFeriesData = restTemplate.getForObject(apiUrlWithYear, Map.class);

        for (Map.Entry<String, String> entry : joursFeriesData.entrySet()) {
            JoursOff jourFerie = new JoursOff();
            jourFerie.setTypeJour(TypeJour.JOUR_FERIE);
            jourFerie.setJour(LocalDate.parse(entry.getKey()));
            jourFerie.setDescription(entry.getValue());
            joursOffRepo.save(jourFerie);
        }
    }
}
