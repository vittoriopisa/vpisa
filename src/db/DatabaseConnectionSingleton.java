

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione del pattern <b>Singleton</b> per la gestione delle connessioni
 * al database PostgreSQL dell’applicazione Hackathon.
 * <p>
 * Questa classe garantisce che esista una sola istanza condivisa di
 * {@link DatabaseConnectionSingleton}, semplificando la gestione della
 * configurazione di accesso al database.
 * </p>
 * <p>
 * Ogni chiamata a {@link #getConnection()} restituisce una <b>nuova connessione</b>
 * valida al database. La responsabilità di chiudere la connessione rimane al chiamante.
 * </p>
 */
public class DatabaseConnectionSingleton {

    private static DatabaseConnectionSingleton instance;

    private static final Logger logger = Logger.getLogger(DatabaseConnectionSingleton.class.getName());

    private final String url = "jdbc:postgresql://localhost:5432/hackathon_sql"; private final String user = "postgres";             private final String password = "marioruigoat6";

    private DatabaseConnectionSingleton() {
        // eventualmente carica driver se serve
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Driver PostgreSQL non trovato.", e);
        }
    }

    /**
     * Restituisce l’istanza unica di {@link DatabaseConnectionSingleton}.
     * Se non esiste ancora, viene creata in modo <i>thread-safe</i>.
     *
     * @return l’istanza singleton della classe
     */
    public static synchronized DatabaseConnectionSingleton getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionSingleton();
        }
        return instance;
    }

    /**
     * Crea e restituisce una nuova connessione al database PostgreSQL.
     * <p>
     * Ogni invocazione apre una connessione distinta: è responsabilità
     * del chiamante chiuderla correttamente con {@code conn.close()}.
     * </p>
     *
     * @return una nuova connessione valida al database
     * @throws SQLException se non è possibile stabilire la connessione
     */
    public Connection getConnection() throws SQLException {
        // ogni chiamata restituisce una nuova connessione valida
        return DriverManager.getConnection(url, user, password);
    }
}
