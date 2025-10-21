package security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe di utilità per l'aggiornamento sicuro delle password degli utenti nel database.
 * <p>
 * Utilizza la classe {@link PasswordSecurity} per trasformare la password in chiaro
 * in un hash sicuro con algoritmo BCrypt, e successivamente aggiorna il record
 * corrispondente nella tabella {@code utente}.
 * </p>
 * <p>
 * Connessione gestita tramite JDBC a un database PostgreSQL.
 * </p>
 */
public class PasswordUpdater {

    /**
     * Aggiorna la password di un utente specifico nel database.
     * <p>
     * Prima della scrittura nel database, la password in chiaro viene convertita
     * in hash sicuro con {@link PasswordSecurity#hashPassword(String)}.
     * </p>
     *
     * @param userId        l'identificativo univoco dell'utente nella tabella {@code utente}
     * @param plainPassword la nuova password in chiaro da assegnare all'utente
     * @throws IllegalArgumentException se la password è nulla o vuota
     */
    public static void updatePassword(int userId, String plainPassword) {
        // Genera l'hash della password
        String hashedPassword = PasswordSecurity.hashPassword(plainPassword);

        String url = "jdbc:postgresql://localhost:5432/hackathon_sql";               String username = "postgres";                                          String password = "marioruigoat6";

        // SQL per aggiornare la password
        String sql = "UPDATE utente SET password_hash = ? , updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Imposta i parametri nella query
            pstmt.setString(1, hashedPassword); // Imposta l'hash della password
            pstmt.setInt(2, userId);             // Imposta l'ID dell'utente

            // Esegui l'aggiornamento
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password aggiornata con successo.");
            } else {
                System.out.println("Nessun utente trovato con l'ID specificato.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo principale di test.
     * <p>
     * Dimostra come aggiornare in blocco le password di più utenti
     * partendo da array paralleli di ID utente e nuove password in chiaro.
     * </p>
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {

        int[] userIds = {70, 71, 76, 78};


        String[] passwords = {
                "nuovaPasswordGiulio",  // Password per l'utente con ID 70
                "passwordLorenzo",    // Password per l'utente con ID 71
                "hackathonPassMario",// Password per l'utente con ID 76
                "passGiovanni"       // Password per l'utente con ID 78

        };

        // Ciclo su tutti gli utenti e aggiorno la loro password
        for (int i = 0; i < userIds.length; i++) {
            updatePassword(userIds[i], passwords[i]);  // Passa la password per ogni ID
        }



    }
}
