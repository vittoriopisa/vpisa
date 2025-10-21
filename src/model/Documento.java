package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Documento {
    private String descrizione;
    private String titolo;
    private LocalDate dataCreazione;
    private String formato;
    private double dimensione;
    private String tipo;
    private List<Commento> commenti; // Nuovo campo per gestire i commenti
    private List<Aggiornamento> aggiornamenti; // Nuovo campo per gli aggiornamenti

    // Costruttore
    public Documento(String descrizione, String titolo, LocalDate dataCreazione, String formato, double dimensione, String tipo)  {
        this.descrizione = descrizione;
        this.titolo=titolo;
        this.dataCreazione=dataCreazione;
        this.formato=formato;
        this.dimensione=dimensione;
        this.tipo=tipo;
        this.commenti = new ArrayList<>();
        this.aggiornamenti = new ArrayList<>(); // Inizializza la lista degli aggiornamenti// Inizializza la lista dei commenti
    }

    // Metodo per aggiungere un nuovo aggiornamento
    public void aggiungiNuovoAggiornamento(Aggiornamento nuovoAggiornamento) {
        if (nuovoAggiornamento != null && !this.aggiornamenti.contains(nuovoAggiornamento)) {
            this.aggiornamenti.add(nuovoAggiornamento);
        }
    }

    // Getter per la lista degli aggiornamenti
    public List<Aggiornamento> getAggiornamenti() {
        return aggiornamenti;
    }

    // Metodo per aggiungere un nuovo commento
    public void aggiungiNuovoCommento(Commento commento) {
        if (commento != null && !this.commenti.contains(commento)) {
            this.commenti.add(commento);
        }
    }

    // Getter per la lista dei commenti
    public List<Commento> getCommenti() {
        return commenti;
    }

    // Altri Getter e Setter (descrizione e titolo)
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    // Getter e Setter per DataCreazione
    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataIscrizione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    //Getter e Setter Formato
    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato =formato;
    }



    //Getter e Setter Dimensione
    public double getDimensione() {
        return dimensione;
    }

    public void setDimensione(double dimensione) {
        this.dimensione =dimensione;
    }

    //Getter e Setter Tipo
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo=tipo;
    }

    @Override
    public String toString() {
        return "Documento{" +
                "descrizione='" + descrizione + '\'' +
                ", titolo='" + titolo + '\'' +
                ", dataCreazione=" + dataCreazione +
                ", formato='" + formato + '\'' +
                ", dimensione=" + dimensione +
                ", tipo='" + tipo + '\'' +
                ", commenti=" + commenti +
                ", aggiornamenti=" + aggiornamenti +
                '}';
    }
}
