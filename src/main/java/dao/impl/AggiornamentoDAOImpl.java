package dao.impl;

import dao.AggiornamentoDAO;
import db.DatabaseConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementazione concreta di {@link AggiornamentoDAO} che utilizza JDBC
 * per interagire con un database PostgreSQL.
 * <p>
 * Si appoggia al {@link DatabaseConnectionSingleton} per ottenere le connessioni
 * e garantisce la validazione dei dati tramite query SQL prima di inserire o eliminare record.
 * </p>
 */
public class AggiornamentoDAOImpl implements AggiornamentoDAO {


    /**
     * {@inheritDoc}
     * <p>
     * Prima di inserire l’aggiornamento viene verificato che il documento
     * esista e appartenga al team specificato. In caso contrario viene
     * sollevata un’{@link IllegalArgumentException}.
     * </p>
     */
    @Override

    public void save(int teamId, int documentoId, String contenuto) throws SQLException {
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection()) {


            String checkSql = "SELECT COUNT(*) FROM documento WHERE id = ? AND team_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, documentoId);
                checkStmt.setInt(2, teamId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new IllegalArgumentException(
                                "Errore: il documento con id " + documentoId +
                                        " non appartiene al team con id " + teamId
                        );
                    }
                }
            }


            String sql = "INSERT INTO aggiornamento (team_id, documento_id, contenuto) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, teamId);
                stmt.setInt(2, documentoId);
                stmt.setString(3, contenuto);
                stmt.executeUpdate();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM aggiornamento WHERE id = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsDocumento(int documentoId) throws SQLException {
        String query = "SELECT COUNT(*) FROM documento WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, documentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Se non viene trovato alcun aggiornamento con l’ID specificato,
     * viene sollevata un’eccezione {@link SQLException}.
     * </p>
     */
    public int getDocumentoIdByAggiornamento(int aggiornamentoId) throws SQLException {
        String sql = "SELECT documento_id FROM aggiornamento WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, aggiornamentoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("documento_id");
                }
            }
        }
        throw new SQLException("Aggiornamento non trovato con id: " + aggiornamentoId);

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findByDocumentoId(int documentoId) throws SQLException {
        List<String> result = new ArrayList<>();

        String sql = "SELECT a.id AS agg_id, a.team_id, t.nome AS team_nome, a.contenuto " +
                "FROM aggiornamento a " +
                "LEFT JOIN team t ON a.team_id = t.id " +
                "WHERE a.documento_id = ? " +
                "ORDER BY a.id";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, documentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int aggId = rs.getInt("agg_id");
                    int teamId = rs.getInt("team_id");
                    String teamNome = rs.getString("team_nome");
                    String contenuto = rs.getString("contenuto");

                    // formato neutro: aggId;teamId;teamNome;contenuto
                    String row = aggId + ";" + teamId + ";" +
                            (teamNome != null ? teamNome : "") + ";" +
                            (contenuto != null ? contenuto : "");
                    result.add(row);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByDocumentoId(int documentoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM aggiornamento WHERE documento_id = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, documentoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("[DEBUG AggiornamentoDAO] existsByDocumentoId(" + documentoId + ") -> " + (count > 0));
                    return count > 0;
                }
            }
        }

        return false;
    }
}