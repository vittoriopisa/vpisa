package model;

public class Aggiornamento {
    private Team team;
    private Documento documento;
    private String contenuto;

    // Costruttore
    public Aggiornamento(Team team, Documento documento, String contenuto) {
        if (team != null) {
            this.team = team;
            team.aggiungiNuovoAggiornamento(this);
        }

        if (documento != null) {
            this.documento = documento;
            documento.aggiungiNuovoAggiornamento(this);
        }

        this.contenuto = contenuto;
    }

    // Setter per Team
    public void setTeam(Team team) {
        if (team != null) {
            this.team = team;
            team.aggiungiNuovoAggiornamento(this); // Assumi che Team abbia questo metodo
        }
    }

    // Setter per Documento
    public void setDocumento(Documento documento) {
        if (documento != null) {
            this.documento = documento;
            documento.aggiungiNuovoAggiornamento(this); // Assumi che Documento abbia questo metodo
        }
    }

    // Setter per Contenuto
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    // Getter per Team
    public Team getTeam() {
        return team;
    }

    // Getter per Documento
    public Documento getDocumento() {
        return documento;
    }

    // Getter per Contenuto
    public String getContenuto() {
        return contenuto;
    }

    @Override
    public String toString() {
        return "Aggiornamento{" +
                "team=" + (team != null ? team.toString() : "null") +
                ", documento=" + (documento != null ? documento.toString() : "null") +
                ", contenuto='" + contenuto +
                '}';
    }
}

