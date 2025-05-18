package model;

public class Valutazione {
    private Team team;
    private Giudice giudice;
    private int punteggio;
    private String feedback;

    //Costruttore
    public Valutazione(Team team, Giudice giudice, int punteggio, String feedback) throws IllegalArgumentException {
        if(team !=null) {
            this.team = team;
            team.aggiungiNuovaValutazione(this);
        }
        if(giudice !=null) {
            this.giudice = giudice;
            giudice.aggiungiNuovaValutazione(this);
        }
        if (punteggio < 0 || punteggio > 10) {
            throw new IllegalArgumentException("Punteggio non valido");
        }
        this.punteggio = punteggio;
        this.feedback = feedback;
    }

    // Setter per Team
    public void setTeam(Team team) {
        if (team != null) {
            this.team = team;
            team.aggiungiNuovaValutazione(this); // Assumi che Team abbia questo metodo
        }
    }

    // Setter per Giudice
    public void setGiudice(Giudice giudice) {
        if (giudice != null) {
            this.giudice = giudice;
            giudice.aggiungiNuovaValutazione(this); // Assumi che Giudice abbia questo metodo
        }
    }

    // Setter per Punteggio
    public void setPunteggio(int punteggio) throws IllegalArgumentException{
        if (punteggio < 0 || punteggio > 10) {
            throw new IllegalArgumentException("Punteggio non valido");
        }
            this.punteggio=punteggio;

    }

    // Setter per Feedback
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // Getter per Team
    public Team getTeam() {
        return team;
    }

    // Getter per Giudice
    public Giudice getGiudice() {
        return giudice;
    }

    // Getter per Punteggio
    public int getPunteggio() {
        return punteggio;
    }

    // Getter per Feedback
    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return "Valutazione{" +
                "team=" + (team != null ? team.getNome() : "null") +
                ", giudice=" + (giudice != null ? giudice.getNome() + " " + giudice.getCognome() : "null") +
                ", punteggio=" + punteggio +
                ", feedback='" + feedback +
                '}';
    }
}
