package dao.impl;

import dao.CommentoDAO;
import db.DatabaseConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementazione concreta di {@link CommentoDAO} che utilizza JDBC
 * per interagire con un database PostgreSQL.
 * <p>
 * Questa classe si occupa di:
 * <ul>
 *     <li>Inserire un nuovo commento, verificando la coerenza tra giudice e documento</li>
 *     <li>Recuperare tutti i commenti associati a un documento</li>
 *     <li>Controllare se esistono aggiornamenti legati a un documento</li>
 * </ul>
 */
public class CommentoDAOImpl implements CommentoDAO {




    /**
     * {@inheritDoc}
     * <p>
     * Prima di salvare un commento, viene verificato che:
     * <ul>
     *     <li>Il documento abbia almeno un aggiornamento</li>
     *     <li>Il giudice e il documento appartengano allo stesso hackathon</li>
     * </ul>
     * In caso contrario viene sollevata un’{@link IllegalArgumentException}.
     */
    @Override


    public void save(int documentoId, int giudiceId, String testo) throws SQLException {

        if (!existsAggiornamento(documentoId)) {
            throw new IllegalArgumentException("Errore: nessun aggiornamento trovato per il documento ID: " + documentoId);
        }


        String checkSql = """
        SELECT COUNT(*)
        FROM documento d
        JOIN team t ON d.team_id = t.id
        JOIN utente g ON g.hackathon_id = t.hackathon_id
        WHERE d.id = ? AND g.id = ? AND g.tipo_utente = 'giudice'
    """;

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, documentoId);
            checkStmt.setInt(2, giudiceId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new IllegalArgumentException("Errore: giudice e documento non appartengono allo stesso hackathon.");
                }
            }
        }


        String query = "INSERT INTO commento (documento_id, giudice_id, testo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, documentoId);
            stmt.setInt(2, giudiceId);
            stmt.setString(3, testo);
            stmt.executeUpdate();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findByDocumentoId(int documentoId) throws SQLException {
        String sql = "SELECT testo FROM commento WHERE documento_id = ?";
        List<String> commenti = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, documentoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    commenti.add(rs.getString("testo"));
                }
            }
        }
        return commenti;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsAggiornamento(int documentoId) throws SQLException {
        String sql = "SELECT 1 FROM aggiornamento WHERE documento_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, documentoId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }


}