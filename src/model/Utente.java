package model;

import java.time.LocalDate;

public class Utente {
    protected String nome;
    protected String cognome;
    protected String email;
    protected LocalDate dataRegistrazione;

    // Costruttore senza parametri
    public Utente() {
        // Inizializza i campi con valori di default
        this.nome = "";
        this.cognome = "";
        this.email = "";
    }

    // Costruttore con parametri
    public Utente(String nome, String cognome, String email,LocalDate dataRegistrazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.dataRegistrazione = dataRegistrazione;
    }

    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email.contains("@")) {
            this.email = email;
        }
    }

    public LocalDate getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(LocalDate dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
}
