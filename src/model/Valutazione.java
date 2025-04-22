package model;

public class Valutazione {
    private Team team;
    private Giudice giudice;
    private int punteggio;
    private String feedback;

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
    public void setPunteggio(int punteggio) {
        if (punteggio >= 0 && punteggio <= 10) {
            this.punteggio = punteggio;
        } else {
            System.out.println("Punteggio non valido");
        }
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
}
