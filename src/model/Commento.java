package model;

public class Commento {
    private Documento documento;
    private Giudice giudice;
    private String testo;

    // Costruttore
    public Commento(Documento documento, Giudice giudice, String testo) {
        if (documento != null) {
            this.documento = documento;
            documento.aggiungiNuovoCommento(this);
        }

        if (giudice != null) {
            this.giudice = giudice;
            documento.aggiungiNuovoCommento(this);
        }

        this.testo = testo;
    }

    // Setter per Documento
    public void setDocumento(Documento documento) {
        if (documento != null) {
            this.documento = documento;
            documento.aggiungiNuovoCommento(this);
        }
    }

    // Setter per Giudice
    public void setGiudice(Giudice giudice) {
        if (giudice != null) {
            this.giudice = giudice;
            giudice.aggiungiNuovoCommento(this);
        }
    }

    // Setter per Testo
    public void setTesto(String testo) {
        this.testo = testo;
    }


    // Getter per Documento
    public Documento getDocumento() {
        return documento;
    }

    // Getter per Giudice
    public Giudice getGiudice() {
        return giudice;
    }

    // Getter per Testo
    public String getTesto() {
        return testo;
    }

    @Override
    public String toString() {
        return "Commento{" +
                "documento=" + (documento != null ? documento.toString() : "null") +
                ", giudice=" + (giudice != null ? giudice.toString() : "null") +
                ", testo='" + testo +
                '}';
    }
}
