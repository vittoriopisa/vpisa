package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hackathon {
    private String nome; // Nome dell'Hackathon
    private String descrizione; // Descrizione dell'Hackathon
    private String luogo; // Luogo dell'Hackathon
    private LocalDate dataInizio; // Data di inizio
    private LocalDate dataFine; // Data di fine
    private List<Team> teams; // Lista dei team partecipanti
    private Organizzatore organizzatore;
    private boolean statoRegistrazioni;

    // Costruttore con tutti i campi
    public Hackathon(String nome, String descrizione, String luogo, LocalDate dataInizio, LocalDate dataFine) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.luogo = luogo;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.teams = new ArrayList<>();
        this.organizzatore = organizzatore;
        this.statoRegistrazioni = false;

    }



    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void aggiungiNuovoTeam(Team nuovoTeam) {
        if (nuovoTeam != null && !this.teams.contains(nuovoTeam)) {
            this.teams.add(nuovoTeam);
            nuovoTeam.setHackathon(this);
        }
    }

    public Organizzatore getOrganizzatore() {
        return organizzatore;
    }


    public void setOrganizzarore(Organizzatore nuovoOrganizzatore) {
        if(nuovoOrganizzatore != null) {
            this.organizzatore = organizzatore;
        }
    }

    public void setStatoRegistrazioni(boolean stato) {
        this.statoRegistrazioni = stato;
    }

    public boolean isStatoRegistrazioni() {
        return this.statoRegistrazioni;
    }

    public void pubblicaClassifica() {
        LocalDate oggi = LocalDate.now();

        if (oggi.isBefore(dataFine)) {
            System.out.println("L'Hackathon non è ancora terminato! Classifica non disponibile.");
            return;
        }

        System.out.println("\n Classifica Finale dell'Hackathon: " + nome);

        for (Team team : teams) {
            double punteggioMedio = team.getValutazioni().stream()
                    .mapToInt(Valutazione::getPunteggio)
                    .average().orElse(0.0);

            System.out.println(team.getNome() + " - Punteggio Medio: " + punteggioMedio);
        }
    }

    @Override
    public String toString() {
        return "Hackathon{" +
                "nome='" + nome + '\'' +
                ", descrizione='" + descrizione +
                ", luogo='" + luogo +
                ", dataInizio='" + dataInizio +
                ", dataFine='" + dataFine +
                ", teams=" + teams +
                ", organizzatore=" + (organizzatore != null ? organizzatore.toString() : "Nessun organizzatore") +
                '}';
    }
}
