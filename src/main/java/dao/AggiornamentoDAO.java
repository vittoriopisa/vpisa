package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object (DAO) per la gestione degli {@code Aggiornamenti}.
 * <p>
 * Definisce le operazioni di persistenza relative agli aggiornamenti associati
 * ai documenti e ai team, come salvataggio, eliminazione e ricerca.
 * </p>
 */
public interface AggiornamentoDAO {
    /**
     * Salva un nuovo aggiornamento nel database.
     *
     * @param teamId      identificativo del team autore dell’aggiornamento
     * @param documentoId identificativo del documento associato
     * @param contenuto   contenuto dell’aggiornamento
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    void save(int teamId, int documentoId, String contenuto) throws SQLException;
    /**
     * Elimina un aggiornamento dal database.
     *
     * @param id identificativo dell’aggiornamento da eliminare
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    void delete(int id) throws SQLException;
    /**
     * Verifica l’esistenza di un documento nel database.
     *
     * @param documentoId identificativo del documento
     * @return {@code true} se il documento esiste, {@code false} altrimenti
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    boolean existsDocumento(int documentoId) throws SQLException;
    /**
     * Restituisce l’ID del documento associato a un aggiornamento.
     *
     * @param aggiornamentoId identificativo dell’aggiornamento
     * @return identificativo del documento associato
     * @throws SQLException se l’aggiornamento non esiste o si verifica un errore nel database
     */
    int getDocumentoIdByAggiornamento(int aggiornamentoId) throws SQLException;

    /**
     * Restituisce l’ID del documento associato a un aggiornamento.
     *
     * @param documentoId identificativo del documento
     * @return identificativo dell'aggiornamento associato
     * @throws SQLException se il documento non esiste o si verifica un errore nel database
     */
     List<String> findByDocumentoId(int documentoId) throws SQLException;

    /**
     * Restituisce l’ID del documento associato a un aggiornamento.
     *
     * @param documentoId identificativo del documento
     * @return identificativo del documento associato
     * @throws SQLException se l'aggiornamento non esiste o si verifica un errore nel database
     */
    boolean existsByDocumentoId(int documentoId) throws SQLException;
}
