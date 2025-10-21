package dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei {@code Team} negli hackathon.
 * <p>
 * Definisce le operazioni CRUD principali e le query di utilità
 * per la gestione dei team e delle loro relazioni con utenti e hackathon.
 */
public interface TeamDAO {

    /**
     * Salva un nuovo team nel database.
     *
     * @param nome        nome del team
     * @param hackathonId identificativo dell'hackathon a cui appartiene il team
     * @throws SQLException se si verifica un errore durante l'inserimento
     */
    void save(String nome, int hackathonId) throws SQLException;

    /**
     * Elimina un team dal database.
     *
     * @param id identificativo del team da eliminare
     * @throws SQLException se si verifica un errore durante l'eliminazione
     */
    void delete(int id) throws SQLException;

    /**
     * Recupera tutti i team appartenenti a un determinato hackathon.
     *
     * @param hackathonId identificativo dell'hackathon
     * @return lista di stringhe contenenti le informazioni sui team (id e nome)
     * @throws SQLException se si verifica un errore durante la query
     */
    List<String> findByHackathonId(int hackathonId) throws SQLException;

    /**
     * Recupera tutti i team di un hackathon che non hanno ancora raggiunto il numero massimo di concorrenti.
     * <p>
     * Si assume che il numero massimo di concorrenti per team sia 6.
     *
     * @param hackathonId identificativo dell'hackathon
     * @return lista di stringhe con le informazioni dei team non completi
     * @throws SQLException se si verifica un errore durante la query
     */
    List<String> findTeamsNotFull(int hackathonId) throws SQLException;
    /**
     * Restituisce l'identificativo dell'hackathon a cui appartiene un team.
     *
     * @param teamId identificativo del team
     * @return id dell'hackathon oppure -1 se non trovato
     * @throws SQLException se si verifica un errore durante la query
     */
    int getHackathonIdByTeam(int teamId) throws SQLException;
    /**
     * Restituisce l'identificativo del team a cui appartiene un utente.
     *
     * @param userId identificativo dell'utente
     * @return id del team
     * @throws SQLException se l'utente non appartiene a nessun team o in caso di errore
     */
    int getTeamIdByUser(int userId) throws SQLException;
    /**
     * Restituisce le informazioni di un team dato il suo ID.
     *
     * @param teamId identificativo del team
     * @return stringa contenente id e nome del team separati da {@code ;}, oppure {@code null} se non trovato
     * @throws SQLException se si verifica un errore durante la query
     */
    String getNomeTeamById(int teamId) throws SQLException;

}
