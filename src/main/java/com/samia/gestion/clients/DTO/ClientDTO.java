package com.samia.gestion.clients.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.samia.gestion.clients.entity.Sex;

import java.time.LocalDate;
import java.util.List;

public record ClientDTO(
        Long id,
        String firstName,
        String lastName,
        String comments,
        String email,
        String mobilePhone,
        String homePhone,
        @JsonProperty("birthday")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthday,
        String city,
        String streetName,
        String zipCode,
        LocalDate created,
        LocalDate modified,
        Sex sex,
        Long userId,
        List<ProgramDTO> programDTOS,
        // Newly added fields
        String stress,                  // Stress
        String stressPsychoaffectif,    // Stress Psychoaffectif
        String fumezVousCombien,        // Fumez-vous ? Combien ?
        String dereglementHormonal,     // Dereglement Hormonal
        String crisesEpilepsie,         // Crises d’Épilepsie
        String interventionsChirurgicales, // Interventions Chirurgicales
        String pacemaker,               // Pacemaker
        String anticoagulants,          // Anticoagulants
        String allergiesOuIntolerances,   // Allergies ou Intolérances
        String problemesThyroidiens,    // Problèmes Thyroïdiens
        String chimiotherapie,          // Chimiothérapie
        String fatigueGenerale,         // Fatigue Générale
        String regimeAlimentaire,       // Régime Alimentaire
        String carences,                // Carences
        String gastroplastie,           // Gastroplastie
        String menopause,               // Ménopause
        String moyenContraception,      // Moyen de Contraception
        String reglesAbondantes,        // Règles Abondantes
        String enceinte,                // Enceinte
        String enfant2DerniereAnnee,  // Enfant 2 Dernières Années
        String chuteApresAccouchement,  // Chute Après Accouchement
        String interruptionAllaitement, // Interruption Allaitement
        String fausseCouche,            // Fausse Coucher
        String herediteChuteCheveux,  // Hérédité Chute De Cheveux
        String calvitie,                // Calvitie
        String cheveuxPeuAbondants,     // Cheveux Peu Abondants
        String pelade,                  // Pelade
        String cheveuxGras,             // Cheveux Gras
        String cheveuxSecs,             // Cheveux Secs
        String chuteCheveuxDepuisQuand, // Chute De Cheveux ? Depuis Quand ?
        String typeChute,             // Type De Chute
        String trichotillomanie,        // Trichotillomanie
        String nombreShampoingsParSemaine,      // Nombre de Shampoings Par Semaine
        String apresShampoingMasque,  // Après Shampoing Ou Masque
        String methodesAgressivesCoiffage, // Méthodes Agressives De Coiffage
        String alimentation             // Alimentation
) {}
