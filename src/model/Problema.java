package model;

public class Problema {
    private Team team;
    private Giudice giudice;
    private String descrizione;

    // Costruttore
    public Problema(Team team, Giudice giudice, String descrizione) {


        if (team.getProblema() == null) { // Controlla se il team ha già un problema
            this.team = team;
            team.assegnaProblema(this); // Assegna il problema al team
        } else {
            System.out.println("Errore: Il team ha già un problema assegnato!");
        }

        if (giudice != null) {
            this.giudice = giudice;
            giudice.aggiungiNuovoProblema(this);
        }

        this.descrizione = descrizione;
    }

    //Setter per Team
    public void setTeam(Team team) {
        if (team != null && team.getProblema() == null) { // Controlla se il team non ha già un problema
            this.team = team;
            team.assegnaProblema(this); // Assegna il problema al nuovo team
        } else {
            System.out.println("Errore: Il team ha già un problema assegnato!");
        }
    }

    // Setter per Giudice
    public void setGiudice(Giudice giudice) {
        if (giudice != null) {
            this.giudice = giudice;
            giudice.aggiungiNuovoProblema(this);
        }
    }

    // Setter per Descrizione
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


    // Getter per Team
    public Team getTeam() {
        return team;
    }

    // Getter per Giudice
    public Giudice getGiudice() {
        return giudice;
    }

    // Getter per Descrizione
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public String toString() {
        return "Problema{" +
                "team=" + (team != null ? team.toString() : "null") +
                ", giudice=" + (giudice != null ? giudice.toString() : "null") +
                ", descrizione='" + descrizione +
                '}';
    }


}
