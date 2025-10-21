package dao;

import java.sql.SQLException;

/**
 * Data Access Object (DAO) per la gestione dei problemi negli hackathon.
 * <p>
 * Definisce le operazioni per creare un nuovo problema oppure
 * assegnare un problema esistente a un team.
 */
public interface ProblemaDAO {

    /**
     * Assegna un problema a un team oppure ne crea uno nuovo.
     * <p>
     * La logica si divide in due casi:
     * <ul>
     *   <li><b>Creazione nuovo problema</b>: se {@code problemaId} è {@code null}, viene creato un nuovo record
     *       con titolo, descrizione e giudice che lo ha proposto. Può essere associato subito a un team.</li>
     *   <li><b>Assegnazione problema esistente</b>: se {@code problemaId} non è {@code null}, il problema viene
     *       assegnato a un team, purché quest'ultimo non abbia già un problema e appartenga allo stesso hackathon
     *       del giudice.</li>
     * </ul>
     *
     * @param titolo      titolo del problema
     * @param descrizione descrizione del problema
     * @param teamId      ID del team a cui assegnare il problema (può essere {@code null})
     * @param giudiceId   ID del giudice che propone o assegna il problema
     * @param problemaId  ID del problema esistente da assegnare (se {@code null} viene creato un nuovo problema)
     * @return {@code true} se l’operazione è andata a buon fine, {@code false} altrimenti
     * @throws SQLException se si verifica un errore durante l’accesso al database
     */
    boolean assignProblemaToTeam(String titolo, String descrizione, Integer teamId, int giudiceId, Integer problemaId) throws SQLException;


}
