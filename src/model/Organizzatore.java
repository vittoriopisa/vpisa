package model;

import java.time.LocalDate;

public class Organizzatore extends Utente{
    private String ruolo; // Ruolo dell'organizzatore (es. coordinatore, assistente)

    // Costruttore
    public Organizzatore(String nome, String cognome, String email, String ruolo, LocalDate dataRegistrazione) {
        super(nome, cognome, email, dataRegistrazione); // Richiama il costruttore della classe base Utente
        this.ruolo = ruolo;
    }

    // Getter e Setter per il ruolo
    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }
}
