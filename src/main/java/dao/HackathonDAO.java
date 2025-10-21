package dao;

import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

/**
 * Data Access Object (DAO) per la gestione delle operazioni relative agli hackathon.
 * <p>
 * Questa interfaccia definisce i metodi principali per salvare,
 * recuperare e gestire le informazioni sugli hackathon.
 */
public interface HackathonDAO {

    /**
     * Salva un nuovo hackathon nel database.
     *
     * @param nome             nome dell'hackathon
     * @param descrizione      descrizione dell'hackathon
     * @param luogo            luogo di svolgimento
     * @param dataInizio       data di inizio
     * @param dataFine         data di fine
     * @param organizzatoreId  ID dell'organizzatore
     * @throws SQLException se si verifica un errore durante l'inserimento
     */
    void save(String nome, String descrizione, String luogo, LocalDate dataInizio, LocalDate dataFine,  int organizzatoreId) throws SQLException;


    /**
     * Restituisce il nome di un hackathon a partire dal suo ID.
     *
     * @param hackathonId ID dell'hackathon
     * @return nome dell'hackathon, oppure {@code null} se non trovato
     * @throws SQLException se si verifica un errore nella query
     */
    String getNomeById(int hackathonId) throws SQLException;

    /**
     * Restituisce la data di fine di un hackathon a partire dal suo ID.
     *
     * @param hackathonId ID dell'hackathon
     * @return data fine dell'hackathon, oppure {@code null} se non trovato
     * @throws SQLException se si verifica un errore nella query
     */
    LocalDate getDataFineById(int hackathonId) throws SQLException;



    /**
     * Restituisce una lista di tutti gli hackathon presenti.
     *
     * @return lista di stringhe contenenti i dettagli degli hackathon
     * @throws SQLException se si verifica un errore durante il recupero
     */
    List<String> getAll() throws SQLException;


    /**
     * Restituisce la classifica dei team di un hackathon in base al punteggio medio.
     *
     * @param hackathonId ID dell'hackathon
     * @return lista ordinata di stringhe con i team e i relativi punteggi medi
     * @throws SQLException se si verifica un errore nella query
     */
    List<String> getClassifica(int hackathonId) throws SQLException;
    /**
     * Restituisce gli hackathon in base allo stato delle registrazioni.
     *
     * @param statoRegistrazioni {@code true} se si vogliono solo quelli con registrazioni aperte,
     *                           {@code false} altrimenti
     * @return lista di hackathon filtrati per stato delle registrazioni
     * @throws SQLException se si verifica un errore nella query
     */
    List<String> findByRegistrazioniAperte(boolean statoRegistrazioni) throws SQLException;
    /**
     * Restituisce l'ID dell'organizzatore di un hackathon.
     *
     * @param hackathonId ID dell'hackathon
     * @return ID dell'organizzatore
     * @throws SQLException se l'hackathon non esiste o si verifica un errore
     */
    int getOrganizzatoreIdByHackathon(int hackathonId) throws SQLException;
    /**
     * Restituisce gli hackathon creati da un organizzatore specifico.
     *
     * @param organizzatoreId ID dell'organizzatore
     * @return lista di hackathon associati all'organizzatore
     * @throws SQLException se si verifica un errore nella query
     */
    List<String> getHackathonByOrganizzatoreId(int organizzatoreId) throws SQLException;

    /**
            * Restituisce l'id dell' hackathon creati dal nome e dall'id di un organizzatore specifico.
            *
            * @param nome dell'organizzatore
            * @param organizzatoreId ID dell'organizzatore
            * @return lista di hackathon associati all'organizzatore
            * @throws SQLException se si verifica un errore nella query
     */
    int getIdByNameAndOrganizzatore(String nome, int organizzatoreId) throws SQLException;
}
