package model;

import java.util.ArrayList;
import java.util.List;

public class Hackathon {
    private String nome; // Nome dell'Hackathon
    private String descrizione; // Descrizione dell'Hackathon
    private String luogo; // Luogo dell'Hackathon
    private String dataInizio; // Data di inizio
    private String dataFine; // Data di fine
    private List<Team> teams; // Lista dei team partecipanti
    private List<Organizzatore> organizzatori; // Lista degli organizzatori

    // Costruttore con tutti i campi
    public Hackathon(String nome, String descrizione, String luogo, String dataInizio, String dataFine) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.luogo = luogo;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.teams = new ArrayList<>();
        this.organizzatori = new ArrayList<>();
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

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
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

    public List<Organizzatore> getOrganizzatori() {
        return organizzatori;
    }

    public void aggiungiNuovoOrganizzatore(Organizzatore nuovoOrganizzatore) {
        if (nuovoOrganizzatore != null && !this.organizzatori.contains(nuovoOrganizzatore)) {
            this.organizzatori.add(nuovoOrganizzatore);
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
                ", organizzatori=" + organizzatori +
                '}';
    }
}
