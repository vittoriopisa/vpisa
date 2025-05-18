package model;

import java.time.LocalDate;

public class Utente {
    protected String nome;
    protected String cognome;
    protected String email;
    protected LocalDate dataRegistrazione;



    // Costruttore
    public Utente(String nome, String cognome, String email,LocalDate dataRegistrazione) throws IllegalArgumentException{
        this.nome = nome;
        this.cognome = cognome;
        if(!email.contains("@")) {
            throw new IllegalArgumentException("l'email deve contenere @");
        }
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

    public void setEmail(String email) throws IllegalArgumentException {
        if(!email.contains("@")) {
            throw new IllegalArgumentException("l'email deve contenere @");
        }
            this.email = email;

    }

    public LocalDate getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(LocalDate dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome +
                ", cognome='" + cognome +
                ", email='" + email +
                ", dataRegistrazione=" + dataRegistrazione +
                '}';
    }
}
