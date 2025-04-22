package model;

public class Commento {
    private Documento documento;
    private Giudice giudice;
    private String testo;

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
}
