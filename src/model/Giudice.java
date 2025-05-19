package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Giudice extends Utente{
    private List<Valutazione> valutazioni; // Valutazioni effettuate dal giudice
    private List<Commento> commenti; // Commenti scritti dal giudice
    private List<Problema> problemi; // Problemi assegnati dal giudice

    // Costruttore
    public Giudice(String nome, String cognome, String email, LocalDate dataRegistrazione) {
        super(nome, cognome, email, dataRegistrazione); // Richiama il costruttore della classe Utent
        this.valutazioni = new ArrayList<>();
        this.commenti = new ArrayList<>();
        this.problemi = new ArrayList<>();
    }



    // Metodo per aggiungere una nuova valutazione
    public void aggiungiNuovaValutazione(Valutazione nuovaValutazione) {
        if (nuovaValutazione != null && !this.valutazioni.contains(nuovaValutazione)) {
            this.valutazioni.add(nuovaValutazione);
        }
    }

    // Metodo per aggiungere un nuovo commento
    public void aggiungiNuovoCommento(Commento nuovoCommento) {
        if (nuovoCommento != null && !this.commenti.contains(nuovoCommento)) {
            this.commenti.add(nuovoCommento);
        }
    }

    public void aggiungiNuovoProblema(Problema problema) {
        if (problema != null) {
            problemi.add(problema);
        }
    }

    // Getter per la lista delle valutazioni
    public List<Valutazione> getValutazioni() {
        return valutazioni;
    }

    // Getter per la lista dei commenti
    public List<Commento> getCommenti() {
        return commenti;
    }

    // Getter per la lista dei problemi
    public List<Problema> getProblemi() {
        return problemi;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", valutazioni=" + valutazioni +
                ", commenti=" + commenti +
                '}';
    }
}
