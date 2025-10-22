package dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Data Access Object (DAO) per la gestione dei {@code Documenti}.
 * <p>
 * Definisce le operazioni di persistenza relative ai documenti prodotti dai team
 * durante un hackathon, compreso il salvataggio, la ricerca e i controlli
 * di consistenza con problemi e team associati.
 * </p>
 */
public interface DocumentoDAO {

    /**
     * Salva un nuovo documento associato a un team.
     *
     * @param teamId        identificativo del team che produce il documento
     * @param titolo        titolo del documento
     * @param descrizione   descrizione del documento
     * @param data_creazione data di creazione del documento
     * @param formato       formato del documento (es. PDF, DOCX)
     * @param dimensione    dimensione del file in MB
     * @param tipo          tipologia del documento (es. relazione, codice, presentazione)
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    void save(int teamId, String titolo, String descrizione, LocalDate data_creazione, String formato, double dimensione, String tipo) throws SQLException;

    /**
     * Verifica se il team ha almeno un problema assegnato.
     *
     * @param teamId identificativo del team
     * @return {@code true} se il team ha almeno un problema associato, {@code false} altrimenti
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    boolean existsProblema(int teamId) throws SQLException;

    /**
     * Restituisce l’ID del team a cui appartiene un documento.
     *
     * @param documentoId identificativo del documento
     * @return l’ID del team associato al documento
     * @throws SQLException se il documento non esiste o in caso di errore nell’accesso al database
     */
    int getTeamIdByDocumento(int documentoId) throws SQLException;
    /**
     * Restituisce l’ID dell’hackathon a cui è legato un documento.
     *
     * @param documentoId identificativo del documento
     * @return l’ID dell’hackathon associato
     * @throws SQLException se il documento non esiste o in caso di errore nell’accesso al database
     */
    int getHackathonIdByDocumento(int documentoId) throws SQLException;
    /**
     * Recupera i documenti (e relativi aggiornamenti) associati a un team.
     * <p>
     * Se viene passato anche un {@code documentoId}, il risultato sarà filtrato
     * su quel documento specifico.
     * </p>
     *
     * @param teamId      identificativo del team
     * @param documentoId identificativo del documento (opzionale, può essere {@code null})
     * @return lista di stringhe che rappresentano documenti e aggiornamenti
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    List<String> findDocumentoByTeamId(int teamId,Integer documentoId) throws SQLException;
    /**
     * Restituisce il titolo di un documento a partire dal suo ID.
     *
     * @param documentoId identificativo del documento
     * @return titolo del documento, oppure {@code null} se non trovato
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    String getTitoloById(int documentoId) throws SQLException;

    /**
            * Recupera gli id dei documenti associati ad un team.
            *
            * @param teamId      identificativo del team

            * @return lista di id dei documenti associati al team
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    List<Integer> getIdsByTeamId(int teamId) throws SQLException;
}
