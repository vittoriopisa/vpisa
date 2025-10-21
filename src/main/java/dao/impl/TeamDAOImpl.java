package dao.impl;

import dao.TeamDAO;
import db.DatabaseConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link TeamDAO}.
 * <p>
 * Fornisce l'accesso e la gestione dei dati relativi ai team
 * all'interno del database tramite JDBC.
 */
public class TeamDAOImpl implements TeamDAO {


    /**
     * {@inheritDoc}
     */
    @Override
    public String getNomeTeamById(int teamId) throws SQLException {
        String sql = "SELECT id, nome FROM team WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");


                    // Restituisci tutto in un'unica stringa separata da ";"
                    return id + ";" + nome;
                } else {
                    return null; // Team non trovato
                }
            }
        }
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String nome, int hackathonId) throws SQLException {
        String sql = "INSERT INTO team (nome, hackathon_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, hackathonId);

            pstmt.executeUpdate();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM team WHERE id = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override


    public List<String> findByHackathonId(int hackathonId) throws SQLException {
        String sql = "SELECT id, nome FROM team WHERE hackathon_id = ?";
        List<String> result = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hackathonId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    result.add(id + " - " + nome); // <-- formato già pronto
                }
            }
        }
        return result;
    }
    /**
     * {@inheritDoc}
     */
    @Override

    public List<String> findTeamsNotFull(int hackathonId) throws SQLException {
        String sql =
                "SELECT t.id, t.nome, t.hackathon_id " +
                        "FROM team t " +
                        "LEFT JOIN utente u " +
                        "  ON u.team_id = t.id " +
                        " AND u.tipo_utente = 'concorrente' " +
                        " AND u.hackathon_id = t.hackathon_id " + // coerenza hackathon (opzionale ma consigliata)
                        "WHERE t.hackathon_id = ? " +
                        "GROUP BY t.id, t.nome, t.hackathon_id " +
                        "HAVING COUNT(u.id) < 6"; // team con meno di 6 concorrenti

        List<String> teams = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hackathonId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String team = "ID: " + rs.getInt("id") +
                            ", Nome: " + rs.getString("nome") +
                            ", Hackathon ID: " + rs.getInt("hackathon_id");
                    teams.add(team);
                }
            }
        }
        return teams;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getHackathonIdByTeam(int teamId) throws SQLException {
        String sql = "SELECT hackathon_id FROM team WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, teamId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("hackathon_id");
                }
            }
        }
        return -1;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getTeamIdByUser(int userId) throws SQLException {
        String sql = "SELECT team_id FROM utente WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("team_id");
            } else {
                throw new SQLException("L'utente con ID " + userId + " non appartiene a nessun team.");
            }
        }
    }

}