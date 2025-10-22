package dao;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

/**
 * Interfaccia DAO per la gestione degli utenti ({@code utente}) negli hackathon.
 * <p>
 * Fornisce i metodi per il salvataggio, l'assegnazione ai team,
 * la rimozione, l'autenticazione e il recupero di informazioni
 * specifiche in base al ruolo (giudice, concorrente, organizzatore).
 */
public interface UtenteDAO {

    /**
     * Salva un nuovo utente nel database.
     *
     * @param nome              nome dell'utente
     * @param cognome           cognome dell'utente
     * @param email             email dell'utente
     * @param passwordHash      password dell'utente (verrà salvata come hash)
     * @param dataRegistrazione data di registrazione
     * @param tipoUtente        tipo di utente ({@code concorrente}, {@code giudice}, {@code organizzatore})
     * @param hackathonId       identificativo dell'hackathon a cui appartiene (può essere {@code null})
     * @param teamId            identificativo del team a cui appartiene (può essere {@code null})
     * @throws SQLException se si verifica un errore durante l'inserimento
     */
    void save(String nome, String cognome, String email, String passwordHash,LocalDate dataRegistrazione, String tipoUtente,  Integer hackathonId, Integer teamId) throws SQLException;
    /**
     * Recupera tutti gli utenti di un certo tipo per un hackathon.
     *
     * @param hackathonId identificativo dell'hackathon
     * @param tipoUtente  tipo di utente da cercare
     * @return lista di stringhe con i dettagli degli utenti
     * @throws SQLException se si verifica un errore durante la query
     */
    List<String> findByTipoUtenteForHackathon(int hackathonId, String tipoUtente) throws SQLException;

    /**
     * Aggiunge un utente a un team (solo se è un concorrente e il team non è pieno).
     *
     * @param userId identificativo dell'utente
     * @param teamId identificativo del team
     * @throws SQLException se si verifica un errore durante l'aggiornamento
     */
    void addUserToTeam(int userId, int teamId) throws SQLException;

    /**
     * Rimuove un utente da un team.
     *
     * @param userId identificativo dell'utente
     * @return {@code true} se l'utente è stato rimosso con successo, {@code false} altrimenti
     * @throws SQLException se si verifica un errore durante l'aggiornamento
     */
    boolean removeUserFromTeam(int userId) throws SQLException;



    /**
     * Restituisce tutti i concorrenti di un team in un determinato hackathon.
     *
     * @param teamId      identificativo del team
     * @param hackathonId identificativo dell'hackathon
     * @return lista di stringhe con i dettagli dei concorrenti
     * @throws SQLException se si verifica un errore durante la query
     */
    List<String> getAllConcorrentiForTeam(int teamId,int hackathonId) throws SQLException;


    /**
     * Verifica le credenziali di accesso e restituisce l'ID dell'utente se valide.
     *
     * @param email    email dell'utente
     * @param password password in chiaro (verrà confrontata con l'hash)
     * @return ID utente se login valido, {@code -1} se fallisce
     * @throws SQLException se si verifica un errore durante la query
     */
    int checkLoginAndGetId(String email, String password) throws SQLException;


    /**
     * Recupera il nome di un giudice a partire dal suo ID.
     *
     * @param giudiceId l'identificativo del giudice
     * @return il nome del giudice
     * @throws SQLException se si verifica un errore durante la query
     */
    String getNomeGiudiceById(int giudiceId) throws SQLException;
    /**
     * Recupera il cognome di un giudice a partire dal suo ID.
     *
     * @param giudiceId l'identificativo del giudice
     * @return il cognome del giudice
     * @throws SQLException se si verifica un errore durante la query
     */
    String getCognomeGiudiceById(int giudiceId) throws SQLException;
    /**
     * Recupera l'email di un giudice a partire dal suo ID.
     *
     * @param giudiceId l'identificativo del giudice
     * @return l'email del giudice
     * @throws SQLException se si verifica un errore durante la query
     */
    String getEmailGiudiceById(int giudiceId) throws SQLException;
    /**
     * Recupera il nome di un organizzatore a partire dal suo ID.
     *
     * @param organizzatoreId l'identificativo dell'organizzatore
     * @return il nome dell'organizzatore
     * @throws SQLException se si verifica un errore durante la query
     */
    String getNomeOrgById(int organizzatoreId) throws SQLException;
    /**
     * Recupera il cognome di un organizzatore a partire dal suo ID.
     *
     * @param organizzatoreId l'identificativo dell'organizzatore
     * @return il cognome dell'organizzatore
     * @throws SQLException se si verifica un errore durante la query
     */
    String getCognomeOrgById(int organizzatoreId) throws SQLException;
    /**
     * Recupera l'email di un organizzatore a partire dal suo ID.
     *
     * @param organizzatoreId l'identificativo dell'organizzatore
     * @return l'email dell'organizzatore
     * @throws SQLException se si verifica un errore durante la query
     */
    String getEmailOrgById(int organizzatoreId) throws SQLException;
    /**
     * Recupera il nome di un concorrente a partire dal suo ID.
     *
     * @param concorrenteId l'identificativo del concorrente
     * @return il nome del concorrente
     * @throws SQLException se si verifica un errore durante la query
     */
     String getNomeConcById(int concorrenteId) throws SQLException;
    /**
     * Recupera il cognome di un concorrente a partire dal suo ID.
     *
     * @param concorrenteId l'identificativo del concorrente
     * @return il cognome del concorrente
     * @throws SQLException se si verifica un errore durante la query
     */
     String getCognomeConcById(int concorrenteId) throws SQLException;
    /**
     * Recupera l'email di un concorrente a partire dal suo ID.
     *
     * @param concorrenteId l'identificativo del concorrente
     * @return l'email del concorrente
     * @throws SQLException se si verifica un errore durante la query
     */
    public String getEmailConcById(int concorrenteId) throws SQLException;
    /**
     * Restituisce il tipo di utente.
     *
     * @param userId identificativo dell'utente
     * @return tipo di utente ({@code concorrente}, {@code giudice}, {@code organizzatore})
     * @throws SQLException se si verifica un errore durante la query
     */
     String getUserTypeById(int userId) throws SQLException;
    /**
     * Restituisce l'hackathon a cui appartiene un utente.
     *
     * @param userId identificativo dell'utente
     * @return ID hackathon
     * @throws SQLException se nessun utente trovato o errore query
     */
    int getHackathonIdByUser(int userId) throws SQLException;



    /**
     * Restituisce il team a cui appartiene un utente.
     *
     * @param userId identificativo dell'utente
     * @return ID team
     * @throws SQLException se nessun utente trovato o errore query
     */
    int getTeamIdByUser(int userId) throws SQLException;
}