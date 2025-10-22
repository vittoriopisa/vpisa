package dao.impl;

import dao.DocumentoDAO;
import db.DatabaseConnectionSingleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Implementazione concreta di {@link DocumentoDAO} che utilizza JDBC
 * per interagire con un database PostgreSQL.
 * <p>
 * Questa classe si occupa di:
 * <ul>
 *     <li>Inserire nuovi documenti nel database, previa verifica della presenza di un problema associato al team</li>
 *     <li>Recuperare titoli, team associati e hackathon legati ai documenti</li>
 *     <li>Estrarre documenti e i loro aggiornamenti per un team specifico</li>
 *     <li>Controllare la consistenza tra team, problemi e documenti</li>
 * </ul>
 */
public class DocumentoDAOImpl implements DocumentoDAO {


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitoloById(int documentoId) throws SQLException {
        String sql = "SELECT titolo FROM documento WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, documentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("titolo");
                }
            }
        }
        return null; // se non trovato
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(int teamId, String titolo, String descrizione, LocalDate data_creazione,String formato, double dimensione, String tipo) throws SQLException {
        // Verifica se il team ha un problema associato prima di salvare
        if (existsProblema(teamId)) {
            String sql = "INSERT INTO documento (titolo, descrizione, data_creazione,formato, dimensione, tipo, team_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, titolo);
                stmt.setString(2, descrizione);
                stmt.setDate(3, Date.valueOf(data_creazione));

                stmt.setString(4, formato);
                stmt.setDouble(5, dimensione);
                stmt.setString(6, tipo);
                stmt.setInt(7, teamId);
                stmt.executeUpdate();
                System.out.println("Documento salvato con successo.");
            }
        } else {
            System.out.println("Errore: Il team con ID " + teamId + " non ha un problema assegnato.");
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsProblema(int teamId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM problema WHERE team_id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Se il conteggio è maggiore di 0, esiste almeno un problema per quel team
            }
            return false;
        }
    }
    /**
     * {@inheritDoc}
     */
    public int getTeamIdByDocumento(int documentoId) throws SQLException {
        String sql = "SELECT team_id FROM documento WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, documentoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("team_id");
            } else {
                throw new SQLException("Documento con ID " + documentoId + " non trovato.");
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getHackathonIdByDocumento(int documentoId) throws SQLException {
        String sql = "SELECT t.hackathon_id " +
                "FROM documento d " +
                "JOIN team t ON d.team_id = t.id " +
                "WHERE d.id = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, documentoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hackathon_id");
                } else {
                    throw new SQLException("Documento non trovato con ID: " + documentoId);
                }
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override


    public List<String> findDocumentoByTeamId(int teamId, Integer documentoId) throws SQLException {
        List<String> docs = new ArrayList<>();

        String sql = """
        SELECT d.id AS documento_id, d.titolo, a.id AS aggiornamento_id, a.contenuto
        FROM documento d
        LEFT JOIN aggiornamento a ON d.id = a.documento_id
        WHERE d.team_id = ?
    """;

        if (documentoId != null) {
            sql += " AND d.id = ?";
        }

        sql += " ORDER BY d.id, a.id";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            if (documentoId != null) {
                stmt.setInt(2, documentoId.intValue());  // safe, perché qui NON è più null
            }

            try (ResultSet rs = stmt.executeQuery()) {
                int documentoCorrente = -1;
                while (rs.next()) {
                    int docId = rs.getInt("documento_id");
                    String titolo = rs.getString("titolo");
                    int aggiornamentoId = rs.getInt("aggiornamento_id");
                    String contenuto = rs.getString("contenuto");

                    if (docId != documentoCorrente) {
                        docs.add(docId + " - " + titolo);
                        documentoCorrente = docId;
                    }
                    if (aggiornamentoId > 0) {
                        docs.add(contenuto);
                    }
                }
            }
        }
        return docs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getIdsByTeamId(int teamId) throws SQLException {
        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT id FROM documento WHERE team_id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id"));
                }
            }
        }

        System.out.println("[DEBUG DocumentoDAO] getIdsByTeamId(" + teamId + ") -> trovati " + ids.size() + " documenti");
        return ids;
    }

}