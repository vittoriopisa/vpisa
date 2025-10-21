package dao.impl;

import dao.ValutazioneDAO;
import db.DatabaseConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia {@link ValutazioneDAO}.
 * <p>
 * Gestisce le operazioni CRUD relative alle valutazioni dei team,
 * come salvataggio, recupero e verifica della presenza di aggiornamenti.
 * <br>
 * Utilizza {@link DatabaseConnectionSingleton} per la connessione al database.
 */
public class ValutazioneDAOImpl implements ValutazioneDAO {


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(int teamId, int giudiceId, int punteggio, String feedback) throws SQLException {

        if (!existsAggiornamento(teamId)) {
            throw new SQLException("Il team non ha aggiornamenti, non è possibile salvare la valutazione.");
        }

        String sql = "INSERT INTO valutazione (team_id, giudice_id, punteggio, feedback) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, giudiceId);
            pstmt.setInt(3, punteggio);
            pstmt.setString(4, feedback);
            pstmt.executeUpdate();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllByTeam(int teamId) throws SQLException {
        String sql = "SELECT id, giudice_id, punteggio, feedback FROM valutazione WHERE team_id = ?";
        List<String> valutazioni = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int giudiceId = rs.getInt("giudice_id");

                    int punteggio = rs.getInt("punteggio");
                    String feedback = rs.getString("feedback");
                    if (feedback == null) feedback = "";


                    String valutazione = giudiceId + " - " + punteggio + " - " + feedback;
                    valutazioni.add(valutazione);
                }
            }
        }
        return valutazioni;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsAggiornamento(int teamId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM aggiornamento WHERE team_id = ?";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}