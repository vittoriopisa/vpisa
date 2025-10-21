
package model;

import model.exceptions.InvalidDataException;
import model.exceptions.RegistrazioneScadutaException;

import java.time.LocalDate;

/**
 * Rappresenta un concorrente iscritto a un {@link Hackathon}.
 * Un concorrente è un tipo di {@link Utente} che può appartenere a un {@link Team}
 */
public class Concorrente extends Utente {


    private Team team;


    /**
     * Crea un concorrente senza team iniziale.
     *
     * @param id                identificativo univoco del concorrente
     * @param nome              il nome del concorrente
     * @param cognome           il cognome del concorrente
     * @param email             l'email del concorrente
     * @param plainPassword     la password in chiaro (che verrà validata/gestita dal sistema)
     * @param dataRegistrazione la data di registrazione
     * @param hackathonScelto   l'hackathon a cui il concorrente partecipa
     * @throws InvalidDataException se uno dei parametri richiesti non è valido
     * @throws RegistrazioneScadutaException se le registrazioni all'hackathon sono chiuse
     */
    public Concorrente(int id, String nome, String cognome, String email,
                       String plainPassword, LocalDate dataRegistrazione,
                       Hackathon hackathonScelto) throws InvalidDataException, RegistrazioneScadutaException {
        super(id, nome, cognome, email, plainPassword, dataRegistrazione, hackathonScelto);

    }

    /**
     * Costruttore "leggero" con email (usa il costruttore protetto di Utente).
     * Utile per rappresentazioni in memoria (visualizzazione, parsing da DAO, ecc.).
     *
     *  @param id identificativo univoco dell'utente concorrente
     *  @param nome nome dell'utente concorrente
     *  @param cognome cognome dell'utente concorrente
     *  @param email email dell'utente concorrente
     * @throws InvalidDataException se uno dei dati non è valido
     * @throws RegistrazioneScadutaException se le registrazioni all'hackathon sono chiuse
     */
    public Concorrente(int id, String nome, String cognome, String email) throws InvalidDataException, RegistrazioneScadutaException {
        super(id, nome, cognome, email);
    }




    /**
     * Crea un concorrente con team e stato specificati.
     *
     * @param id                identificativo univoco del concorrente
     * @param nome              il nome del concorrente
     * @param cognome           il cognome del concorrente
     * @param email             l'email del concorrente
     * @param plainPassword     la password in chiaro
     * @param dataRegistrazione la data di registrazione
     * @param team              il team di appartenenza (può essere {@code null} se non assegnato)
     * @param hackathonScelto   l'hackathon a cui il concorrente partecipa
     * @throws InvalidDataException se uno dei parametri richiesti non è valido
     * @throws RegistrazioneScadutaException se le registrazioni all'hackathon sono chiuse
     */
    public Concorrente(int id, String nome, String cognome, String email,
                       String plainPassword, LocalDate dataRegistrazione,
                       Team team, Hackathon hackathonScelto) throws InvalidDataException,RegistrazioneScadutaException {
        super(id, nome, cognome, email, plainPassword, dataRegistrazione, hackathonScelto);
        this.team = team;

    }
    /**
     * Crea un concorrente minimale con solo id, nome e cognome.
     * Utile per scenari dove servono solo dati anagrafici.
     *
     * @param id      identificativo univoco del concorrente
     * @param nome    il nome del concorrente
     * @param cognome il cognome del concorrente
     * @throws InvalidDataException se nome o cognome non sono validi
     * @throws RegistrazioneScadutaException se le registrazioni all'hackathon sono chiuse
     */
    public Concorrente(int id, String nome, String cognome) throws InvalidDataException,RegistrazioneScadutaException {
        super(id, nome, cognome, "", "", null, null);
    }






    /**
     * Restituisce il team di appartenenza del concorrente.
     *
     * @return il {@link Team}, oppure {@code null} se non assegnato
     */
    public Team getTeam() { return team; }





    /**
     * Imposta internamente il team di appartenenza.
     * <p>
     * Questo metodo è {@code package-private} e dovrebbe essere usato
     * solo da classi del modello per gestire le associazioni.
     *
     * @param team il nuovo team (può essere {@code null})
     */
    public void setTeamInternal(Team team) {
        this.team = team;
    }


    /**
     * Restituisce una rappresentazione testuale del concorrente,
     * comprensiva di nome, team e stato.
     *
     * @return stringa leggibile con i principali dati del concorrente
     */
    @Override
    public String toString() {
        return String.format("Concorrente{nome='%s %s', team='%s', status=%s}",
                getNome(), getCognome(), team != null ? team.getNome() : "Nessuno"/*, status*/);
    }


}

