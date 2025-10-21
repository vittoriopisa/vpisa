package model;

import java.time.LocalDate;

public class Concorrente extends Utente {
    private LocalDate dataIscrizione;
    private boolean status;
    private Team team;

    // Costruttore di Concorrente
    public Concorrente(String nome, String cognome, String email, LocalDate dataRegistrazione, LocalDate dataIscrizione, boolean status, Team team) {
        super(nome, cognome, email, dataRegistrazione); // Richiama il costruttore parametrico di Utente
        this.dataIscrizione = dataIscrizione;
        this.status = status;
        this.team = team;
    }

    // Getter e Setter per DataIscrizione
    public LocalDate getDataIscrizione() {
        return dataIscrizione;
    }

    public void setDataIscrizione(LocalDate dataIscrizione) {
        if(dataIscrizione !=null && dataIscrizione.isAfter(this.getDataRegistrazione())) {
            this.dataIscrizione = dataIscrizione;
        }
    }

    // Getter e Setter per Status
    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Getter e Setter per Team
    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if (team != null) {
            this.team = team;
            team.aggiungiNuovoConcorrente(this); // Associa il concorrente al team
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                ", dataIscrizione='" + dataIscrizione + ", status='" + status + ", team='" + (team!=null ? team.toString() : null) +'}';
    }
}
