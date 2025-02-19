package com.samia.gestion.clients.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients", indexes = {
        @Index(name = "idx_last_name_first_name", columnList = "last_name, first_name"),
        @Index(name = "idx_zip_code", columnList = "zip_code")
})

public class Client {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "ce champ est obligatoire")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "ce champ est obligatoire")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "comments", columnDefinition="LONGTEXT")
    private String comments;

    @Column(name="email", nullable = true)
    private String email;

    @Column(name="mobile_phone")
    private String mobilePhone;

    @Column(name="home_phone")
    private String homePhone;

    @Column(name="birthday")
    private LocalDate birthday;

    @Column(name="city")
    private String city;

    @Column(name="street_name")
    private String streetName;

    @Column(name="zip_code")
    private String zipCode;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "modified")
    private LocalDate modified;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private List<Program> programs = new ArrayList<>();

    // Add nullable columns for the new fields
    @Column(name = "stress", nullable = true)
    private String stress;

    @Column(name = "stress_psychoaffectif", nullable = true)
    private String stressPsychoaffectif;

    @Column(name = "fumez_vous_combien", nullable = true)
    private String fumezVousCombien;

    @Column(name = "dereglement_hormonal", nullable = true)
    private String dereglementHormonal;

    @Column(name = "crises_epilepsie", nullable = true)
    private String crisesEpilepsie;

    @Column(name = "interventions_chirurgicales", nullable = true)
    private String interventionsChirurgicales;

    @Column(name = "pacemaker", nullable = true)
    private String pacemaker;

    @Column(name = "anticoagulants", nullable = true)
    private String anticoagulants;

    @Column(name = "allergies_ou_intolerances", nullable = true)
    private String allergiesOuIntolerances;

    @Column(name = "problemes_thyroidiens", nullable = true)
    private String problemesThyroidiens;

    @Column(name = "chimiotherapie", nullable = true)
    private String chimiotherapie;

    @Column(name = "fatigue_generale", nullable = true)
    private String fatigueGenerale;

    @Column(name = "regime_alimentaire", nullable = true)
    private String regimeAlimentaire;

    @Column(name = "carences", nullable = true)
    private String carences;

    @Column(name = "gastroplastie", nullable = true)
    private String gastroplastie;

    @Column(name = "menopause", nullable = true)
    private String menopause;

    @Column(name = "moyen_contraception", nullable = true)
    private String moyenContraception;

    @Column(name = "regles_abondantes", nullable = true)
    private String reglesAbondantes;

    @Column(name = "enceinte", nullable = true)
    private String enceinte;

    @Column(name = "enfant_deux_derniere_annee", nullable = true)
    private String enfant2DerniereAnnee;

    @Column(name = "chute_apres_accouchement", nullable = true)
    private String chuteApresAccouchement;

    @Column(name = "interruption_allaitement", nullable = true)
    private String interruptionAllaitement;

    @Column(name = "fausse_couche", nullable = true)
    private String fausseCouche;

    @Column(name = "heredite_chute_cheveux", nullable = true)
    private String herediteChuteCheveux;

    @Column(name = "calvitie", nullable = true)
    private String calvitie;

    @Column(name = "cheveux_peu_abondants", nullable = true)
    private String cheveuxPeuAbondants;

    @Column(name = "pelade", nullable = true)
    private String pelade;

    @Column(name = "cheveux_gras", nullable = true)
    private String cheveuxGras;

    @Column(name = "cheveux_secs", nullable = true)
    private String cheveuxSecs;

    @Column(name = "chute_cheveux_depuis_quand", nullable = true)
    private String chuteCheveuxDepuisQuand;

    @Column(name = "type_chute", nullable = true)
    private String typeChute;

    @Column(name = "trichotillomanie", nullable = true)
    private String trichotillomanie;

    @Column(name = "nombre_shampoings_par_semaine", nullable = true)
    private String nombreShampoingsParSemaine;

    @Column(name = "apres_shampoing_masque", nullable = true)
    private String apresShampoingMasque;

    @Column(name = "methodes_agressives_coiffage", nullable = true)
    private String methodesAgressivesCoiffage;

    @Column(name = "alimentation", nullable = true)
    private String alimentation;

    public Client() {
    }

    public Client(String firstName, String lastName, String comments, String email, String mobilePhone, String homePhone, LocalDate birthday, String city, String streetName, String zipCode, LocalDate created, LocalDate modified, Sex sex, Long userId, List<Program> programs, String stress, String stressPsychoaffectif, String fumezVousCombien, String dereglementHormonal, String crisesEpilepsie, String interventionsChirurgicales, String pacemaker, String anticoagulants, String allergiesOuIntolerances, String problemesThyroidiens, String chimiotherapie, String fatigueGenerale, String regimeAlimentaire, String carences, String gastroplastie, String menopause, String moyenContraception, String reglesAbondantes, String enceinte, String enfant2DerniereAnnee, String chuteApresAccouchement, String interruptionAllaitement, String fausseCouche, String herediteChuteCheveux, String calvitie, String cheveuxPeuAbondants, String pelade, String cheveuxGras, String cheveuxSecs, String chuteCheveuxDepuisQuand, String typeChute, String trichotillomanie, String nombreShampoingsParSemaine, String apresShampoingMasque, String methodesAgressivesCoiffage, String alimentation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.comments = comments;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.birthday = birthday;
        this.city = city;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.created = created;
        this.modified = modified;
        this.sex = sex;
        this.userId = userId;
        this.programs = programs;
        this.stress = stress;
        this.stressPsychoaffectif = stressPsychoaffectif;
        this.fumezVousCombien = fumezVousCombien;
        this.dereglementHormonal = dereglementHormonal;
        this.crisesEpilepsie = crisesEpilepsie;
        this.interventionsChirurgicales = interventionsChirurgicales;
        this.pacemaker = pacemaker;
        this.anticoagulants = anticoagulants;
        this.allergiesOuIntolerances = allergiesOuIntolerances;
        this.problemesThyroidiens = problemesThyroidiens;
        this.chimiotherapie = chimiotherapie;
        this.fatigueGenerale = fatigueGenerale;
        this.regimeAlimentaire = regimeAlimentaire;
        this.carences = carences;
        this.gastroplastie = gastroplastie;
        this.menopause = menopause;
        this.moyenContraception = moyenContraception;
        this.reglesAbondantes = reglesAbondantes;
        this.enceinte = enceinte;
        this.enfant2DerniereAnnee = enfant2DerniereAnnee;
        this.chuteApresAccouchement = chuteApresAccouchement;
        this.interruptionAllaitement = interruptionAllaitement;
        this.fausseCouche = fausseCouche;
        this.herediteChuteCheveux = herediteChuteCheveux;
        this.calvitie = calvitie;
        this.cheveuxPeuAbondants = cheveuxPeuAbondants;
        this.pelade = pelade;
        this.cheveuxGras = cheveuxGras;
        this.cheveuxSecs = cheveuxSecs;
        this.chuteCheveuxDepuisQuand = chuteCheveuxDepuisQuand;
        this.typeChute = typeChute;
        this.trichotillomanie = trichotillomanie;
        this.nombreShampoingsParSemaine = nombreShampoingsParSemaine;
        this.apresShampoingMasque = apresShampoingMasque;
        this.methodesAgressivesCoiffage = methodesAgressivesCoiffage;
        this.alimentation = alimentation;
    }

    public Client(Long id, String firstName, String lastName, String comments, String email, String mobilePhone, String homePhone, LocalDate birthday, String city, String streetName, String zipCode, LocalDate created, LocalDate modified, Sex sex, Long userId, List<Program> programs, String stress, String stressPsychoaffectif, String fumezVousCombien, String dereglementHormonal, String crisesEpilepsie, String interventionsChirurgicales, String pacemaker, String anticoagulants, String allergiesOuIntolerances, String problemesThyroidiens, String chimiotherapie, String fatigueGenerale, String regimeAlimentaire, String carences, String gastroplastie, String menopause, String moyenContraception, String reglesAbondantes, String enceinte, String enfant2DerniereAnnee, String chuteApresAccouchement, String interruptionAllaitement, String fausseCouche, String herediteChuteCheveux, String calvitie, String cheveuxPeuAbondants, String pelade, String cheveuxGras, String cheveuxSecs, String chuteCheveuxDepuisQuand, String typeChute, String trichotillomanie, String nombreShampoingsParSemaine, String apresShampoingMasque, String methodesAgressivesCoiffage, String alimentation) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comments = comments;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.birthday = birthday;
        this.city = city;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.created = created;
        this.modified = modified;
        this.sex = sex;
        this.userId = userId;
        this.programs = programs;
        this.stress = stress;
        this.stressPsychoaffectif = stressPsychoaffectif;
        this.fumezVousCombien = fumezVousCombien;
        this.dereglementHormonal = dereglementHormonal;
        this.crisesEpilepsie = crisesEpilepsie;
        this.interventionsChirurgicales = interventionsChirurgicales;
        this.pacemaker = pacemaker;
        this.anticoagulants = anticoagulants;
        this.allergiesOuIntolerances = allergiesOuIntolerances;
        this.problemesThyroidiens = problemesThyroidiens;
        this.chimiotherapie = chimiotherapie;
        this.fatigueGenerale = fatigueGenerale;
        this.regimeAlimentaire = regimeAlimentaire;
        this.carences = carences;
        this.gastroplastie = gastroplastie;
        this.menopause = menopause;
        this.moyenContraception = moyenContraception;
        this.reglesAbondantes = reglesAbondantes;
        this.enceinte = enceinte;
        this.enfant2DerniereAnnee = enfant2DerniereAnnee;
        this.chuteApresAccouchement = chuteApresAccouchement;
        this.interruptionAllaitement = interruptionAllaitement;
        this.fausseCouche = fausseCouche;
        this.herediteChuteCheveux = herediteChuteCheveux;
        this.calvitie = calvitie;
        this.cheveuxPeuAbondants = cheveuxPeuAbondants;
        this.pelade = pelade;
        this.cheveuxGras = cheveuxGras;
        this.cheveuxSecs = cheveuxSecs;
        this.chuteCheveuxDepuisQuand = chuteCheveuxDepuisQuand;
        this.typeChute = typeChute;
        this.trichotillomanie = trichotillomanie;
        this.nombreShampoingsParSemaine = nombreShampoingsParSemaine;
        this.apresShampoingMasque = apresShampoingMasque;
        this.methodesAgressivesCoiffage = methodesAgressivesCoiffage;
        this.alimentation = alimentation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "ce champ est obligatoire") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "ce champ est obligatoire") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "ce champ est obligatoire") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "ce champ est obligatoire") String lastName) {
        this.lastName = lastName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public String getStress() {
        return stress;
    }

    public void setStress(String stress) {
        this.stress = stress;
    }

    public String getStressPsychoaffectif() {
        return stressPsychoaffectif;
    }

    public void setStressPsychoaffectif(String stressPsychoaffectif) {
        this.stressPsychoaffectif = stressPsychoaffectif;
    }

    public String getFumezVousCombien() {
        return fumezVousCombien;
    }

    public void setFumezVousCombien(String fumezVousCombien) {
        this.fumezVousCombien = fumezVousCombien;
    }

    public String getDereglementHormonal() {
        return dereglementHormonal;
    }

    public void setDereglementHormonal(String dereglementHormonal) {
        this.dereglementHormonal = dereglementHormonal;
    }

    public String getCrisesEpilepsie() {
        return crisesEpilepsie;
    }

    public void setCrisesEpilepsie(String crisesEpilepsie) {
        this.crisesEpilepsie = crisesEpilepsie;
    }

    public String getInterventionsChirurgicales() {
        return interventionsChirurgicales;
    }

    public void setInterventionsChirurgicales(String interventionsChirurgicales) {
        this.interventionsChirurgicales = interventionsChirurgicales;
    }

    public String getPacemaker() {
        return pacemaker;
    }

    public void setPacemaker(String pacemaker) {
        this.pacemaker = pacemaker;
    }

    public String getAnticoagulants() {
        return anticoagulants;
    }

    public void setAnticoagulants(String anticoagulants) {
        this.anticoagulants = anticoagulants;
    }

    public String getAllergiesOuIntolerances() {
        return allergiesOuIntolerances;
    }

    public void setAllergiesOuIntolerances(String allergiesOuIntolerances) {
        this.allergiesOuIntolerances = allergiesOuIntolerances;
    }

    public String getProblemesThyroidiens() {
        return problemesThyroidiens;
    }

    public void setProblemesThyroidiens(String problemesThyroidiens) {
        this.problemesThyroidiens = problemesThyroidiens;
    }

    public String getChimiotherapie() {
        return chimiotherapie;
    }

    public void setChimiotherapie(String chimiotherapie) {
        this.chimiotherapie = chimiotherapie;
    }

    public String getFatigueGenerale() {
        return fatigueGenerale;
    }

    public void setFatigueGenerale(String fatigueGenerale) {
        this.fatigueGenerale = fatigueGenerale;
    }

    public String getRegimeAlimentaire() {
        return regimeAlimentaire;
    }

    public void setRegimeAlimentaire(String regimeAlimentaire) {
        this.regimeAlimentaire = regimeAlimentaire;
    }

    public String getCarences() {
        return carences;
    }

    public void setCarences(String carences) {
        this.carences = carences;
    }

    public String getGastroplastie() {
        return gastroplastie;
    }

    public void setGastroplastie(String gastroplastie) {
        this.gastroplastie = gastroplastie;
    }

    public String getMenopause() {
        return menopause;
    }

    public void setMenopause(String menopause) {
        this.menopause = menopause;
    }

    public String getMoyenContraception() {
        return moyenContraception;
    }

    public void setMoyenContraception(String moyenContraception) {
        this.moyenContraception = moyenContraception;
    }

    public String getReglesAbondantes() {
        return reglesAbondantes;
    }

    public void setReglesAbondantes(String reglesAbondantes) {
        this.reglesAbondantes = reglesAbondantes;
    }

    public String getEnceinte() {
        return enceinte;
    }

    public void setEnceinte(String enceinte) {
        this.enceinte = enceinte;
    }

    public String getEnfant2DerniereAnnee() {
        return enfant2DerniereAnnee;
    }

    public void setEnfant2DerniereAnnee(String enfant2DerniereAnnee) {
        this.enfant2DerniereAnnee = enfant2DerniereAnnee;
    }

    public String getChuteApresAccouchement() {
        return chuteApresAccouchement;
    }

    public void setChuteApresAccouchement(String chuteApresAccouchement) {
        this.chuteApresAccouchement = chuteApresAccouchement;
    }

    public String getInterruptionAllaitement() {
        return interruptionAllaitement;
    }

    public void setInterruptionAllaitement(String interruptionAllaitement) {
        this.interruptionAllaitement = interruptionAllaitement;
    }

    public String getFausseCouche() {
        return fausseCouche;
    }

    public void setFausseCouche(String fausseCouche) {
        this.fausseCouche = fausseCouche;
    }

    public String getHerediteChuteCheveux() {
        return herediteChuteCheveux;
    }

    public void setHerediteChuteCheveux(String herediteChuteCheveux) {
        this.herediteChuteCheveux = herediteChuteCheveux;
    }

    public String getCalvitie() {
        return calvitie;
    }

    public void setCalvitie(String calvitie) {
        this.calvitie = calvitie;
    }

    public String getCheveuxPeuAbondants() {
        return cheveuxPeuAbondants;
    }

    public void setCheveuxPeuAbondants(String cheveuxPeuAbondants) {
        this.cheveuxPeuAbondants = cheveuxPeuAbondants;
    }

    public String getPelade() {
        return pelade;
    }

    public void setPelade(String pelade) {
        this.pelade = pelade;
    }

    public String getCheveuxGras() {
        return cheveuxGras;
    }

    public void setCheveuxGras(String cheveuxGras) {
        this.cheveuxGras = cheveuxGras;
    }

    public String getCheveuxSecs() {
        return cheveuxSecs;
    }

    public void setCheveuxSecs(String cheveuxSecs) {
        this.cheveuxSecs = cheveuxSecs;
    }

    public String getChuteCheveuxDepuisQuand() {
        return chuteCheveuxDepuisQuand;
    }

    public void setChuteCheveuxDepuisQuand(String chuteCheveuxDepuisQuand) {
        this.chuteCheveuxDepuisQuand = chuteCheveuxDepuisQuand;
    }

    public String getTypeChute() {
        return typeChute;
    }

    public void setTypeChute(String typeChute) {
        this.typeChute = typeChute;
    }

    public String getTrichotillomanie() {
        return trichotillomanie;
    }

    public void setTrichotillomanie(String trichotillomanie) {
        this.trichotillomanie = trichotillomanie;
    }

    public String getNombreShampoingsParSemaine() {
        return nombreShampoingsParSemaine;
    }

    public void setNombreShampoingsParSemaine(String nombreShampoingsParSemaine) {
        this.nombreShampoingsParSemaine = nombreShampoingsParSemaine;
    }

    public String getApresShampoingMasque() {
        return apresShampoingMasque;
    }

    public void setApresShampoingMasque(String apresShampoingMasque) {
        this.apresShampoingMasque = apresShampoingMasque;
    }

    public String getMethodesAgressivesCoiffage() {
        return methodesAgressivesCoiffage;
    }

    public void setMethodesAgressivesCoiffage(String methodesAgressivesCoiffage) {
        this.methodesAgressivesCoiffage = methodesAgressivesCoiffage;
    }

    public String getAlimentation() {
        return alimentation;
    }

    public void setAlimentation(String alimentation) {
        this.alimentation = alimentation;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", comments='" + comments + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", birthday=" + birthday +
                ", city='" + city + '\'' +
                ", streetName='" + streetName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", sex=" + sex +
                ", userId=" + userId +
                ", programs=" + programs +
                ", stress='" + stress + '\'' +
                ", stressPsychoaffectif='" + stressPsychoaffectif + '\'' +
                ", fumezVousCombien='" + fumezVousCombien + '\'' +
                ", dereglementHormonal='" + dereglementHormonal + '\'' +
                ", crisesEpilepsie='" + crisesEpilepsie + '\'' +
                ", interventionsChirurgicales='" + interventionsChirurgicales + '\'' +
                ", pacemaker='" + pacemaker + '\'' +
                ", anticoagulants='" + anticoagulants + '\'' +
                ", allergiesOuIntolerances='" + allergiesOuIntolerances + '\'' +
                ", problemesThyroidiens='" + problemesThyroidiens + '\'' +
                ", chimiotherapie='" + chimiotherapie + '\'' +
                ", fatigueGenerale='" + fatigueGenerale + '\'' +
                ", regimeAlimentaire='" + regimeAlimentaire + '\'' +
                ", carences='" + carences + '\'' +
                ", gastroplastie='" + gastroplastie + '\'' +
                ", menopause='" + menopause + '\'' +
                ", moyenContraception='" + moyenContraception + '\'' +
                ", reglesAbondantes='" + reglesAbondantes + '\'' +
                ", enceinte='" + enceinte + '\'' +
                ", enfant2DerniereAnnee='" + enfant2DerniereAnnee + '\'' +
                ", chuteApresAccouchement='" + chuteApresAccouchement + '\'' +
                ", interruptionAllaitement='" + interruptionAllaitement + '\'' +
                ", fausseCouche='" + fausseCouche + '\'' +
                ", herediteChuteCheveux='" + herediteChuteCheveux + '\'' +
                ", calvitie='" + calvitie + '\'' +
                ", cheveuxPeuAbondants='" + cheveuxPeuAbondants + '\'' +
                ", pelade='" + pelade + '\'' +
                ", cheveuxGras='" + cheveuxGras + '\'' +
                ", cheveuxSecs='" + cheveuxSecs + '\'' +
                ", chuteCheveuxDepuisQuand='" + chuteCheveuxDepuisQuand + '\'' +
                ", typeChute='" + typeChute + '\'' +
                ", trichotillomanie='" + trichotillomanie + '\'' +
                ", nombreShampoingsParSemaine='" + nombreShampoingsParSemaine + '\'' +
                ", apresShampoingMasque='" + apresShampoingMasque + '\'' +
                ", methodesAgressivesCoiffage='" + methodesAgressivesCoiffage + '\'' +
                ", alimentation='" + alimentation + '\'' +
                '}';
    }
}
