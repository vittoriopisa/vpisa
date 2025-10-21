package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione delle valutazioni dei team.
 * <p>
 * Fornisce i metodi per salvare una nuova valutazione, recuperare
 * tutte le valutazioni associate a un team e verificare la presenza
 * di aggiornamenti per un determinato team.
 */
public interface ValutazioneDAO {

    /**
     * Salva una nuova valutazione associata a un team e a un giudice.
     *
     * @param teamId    ID del team valutato
     * @param giudiceId ID del giudice che effettua la valutazione
     * @param punteggio punteggio assegnato al team
     * @param feedback  eventuale commento o feedback testuale
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    void save(int teamId, int giudiceId, int punteggio, String feedback) throws SQLException;

    /**
     * Recupera tutte le valutazioni associate a un team.
     *
     * @param teamId ID del team di cui recuperare le valutazioni
     * @return lista di stringhe rappresentanti le valutazioni
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    List<String> getAllByTeam(int teamId) throws SQLException;

    /**
     * Verifica se esistono aggiornamenti per un team specifico.
     *
     * @param teamId ID del team da controllare
     * @return {@code true} se esiste almeno un aggiornamento per il team,
     *         {@code false} altrimenti
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    boolean existsAggiornamento(int teamId) throws SQLException;
}
