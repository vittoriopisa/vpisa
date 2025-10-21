package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object (DAO) per la gestione dei {@code Commenti}.
 * <p>
 * Definisce le operazioni di persistenza relative ai commenti associati
 * ai documenti, compreso il salvataggio, la ricerca e i controlli di esistenza.
 * </p>
 */
public interface CommentoDAO {

    /**
     * Salva un nuovo commento associato a un documento e a un giudice.
     *
     * @param documentoId identificativo del documento a cui il commento è riferito
     * @param giudiceId   identificativo del giudice autore del commento
     * @param testo       contenuto del commento
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    void save(int documentoId, int giudiceId, String testo) throws SQLException;


    /**
     * Recupera tutti i commenti associati a un documento specifico.
     *
     * @param documentoId identificativo del documento
     * @return lista di commenti testuali associati al documento;
     *         una lista vuota se non ci sono commenti
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    List<String> findByDocumentoId(int documentoId) throws SQLException;

    /**
     * Verifica se esistono aggiornamenti associati a un documento.
     * <p>
     * Questo controllo è utile per garantire che sia possibile commentare
     * un documento solo se esso è stato aggiornato almeno una volta.
     * </p>
     *
     * @param documentoId identificativo del documento
     * @return {@code true} se esiste almeno un aggiornamento,
     *         {@code false} altrimenti
     * @throws SQLException se si verifica un errore nell’accesso al database
     */
    boolean existsAggiornamento(int documentoId) throws SQLException;




}
