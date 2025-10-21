
package model;

import model.exceptions.InvalidDataException;

import model.exceptions.RegistrazioneScadutaException;


import java.time.LocalDate;


/**
 * Rappresenta un organizzatore di un {@link Hackathon}.
 * <p>
 * L’organizzatore è un particolare tipo di {@link Utente}
 * che ha il compito di gestire e amministrare l’evento.
 * </p>
 */
public class Organizzatore extends Utente {


    /**
     * Crea un nuovo organizzatore senza hackathon associato.
     *
     * @param id               identificativo univoco dell’organizzatore
     * @param nome             nome dell’organizzatore
     * @param cognome          cognome dell’organizzatore
     * @param email            email di registrazione
     * @param plainPassword    password in chiaro
     * @param dataRegistrazione data di registrazione
     * @throws InvalidDataException se i dati non sono validi
     */
    public Organizzatore(int id,String nome, String cognome, String email,
                       String plainPassword, LocalDate dataRegistrazione) throws InvalidDataException {
        super(id,nome, cognome, email, plainPassword, dataRegistrazione);

    }






    /**
     * Costruttore "leggero" con email (usa il costruttore protetto di Utente).
     * Utile per rappresentazioni in memoria (visualizzazione, parsing da DAO, ecc.).
     *
     * @param id      identificativo univoco dell'organizzatore
     * @param nome    il nome dell'organizzatore
     * @param cognome il cognome dell'organizzatore
     * @param email l'email dell'organizzatore
     *  @throws InvalidDataException se i dati inseriti non sono validi
     */
    public Organizzatore(int id, String nome, String cognome, String email) throws InvalidDataException {
        super(id, nome, cognome, email);
    }

    /*public Organizzatore(int id, String nome, String cognome) throws InvalidDataException {
        super(id, nome, cognome, "", "", null);
    }*/






    /**
     * Restituisce una rappresentazione testuale dell’organizzatore,
     * includendo nome e cognome.
     *
     * @return stringa rappresentativa dell’organizzatore
     */
    @Override
    public String toString() {
        return String.format("Organizzatore{nome='%s %s'}",
                getNome(), getCognome());
    }
}


