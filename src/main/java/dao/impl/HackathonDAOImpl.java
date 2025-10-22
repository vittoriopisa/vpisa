package dao.impl;

import dao.HackathonDAO;
import db.DatabaseConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Implementazione concreta dell'interfaccia {@link HackathonDAO}.
 * <p>
 * Gestisce le operazioni CRUD sugli hackathon tramite JDBC,
 * sfruttando il {@link DatabaseConnectionSingleton} per ottenere
 * connessioni al database.
 */
public class HackathonDAOImpl implements HackathonDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNomeById(int hackathonId) throws SQLException {
        String sql = "SELECT nome FROM hackathon WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hackathonId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                } else {
                    return null; // Hackathon non trovato
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getDataFineById(int hackathonId) throws SQLException {
        String sql = "SELECT data_fine FROM hackathon WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hackathonId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Date sqlDate = rs.getDate("data_fine");
                        return (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    }
                }
            }

        return null;
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String nome, String descrizione, String luogo, LocalDate dataInizio, LocalDate dataFine,int organizzatoreId) throws SQLException {
        LocalDate oggi = LocalDate.now();
        long giorniDifferenza = ChronoUnit.DAYS.between(oggi, dataInizio);
        boolean statoRegistrazioni = giorniDifferenza >= 2;

        String sql = "INSERT INTO hackathon (nome, descrizione, luogo, data_inizio, data_fine,stato_registrazioni, organizzatore_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, descrizione);
            pstmt.setString(3, luogo);
            pstmt.setDate(4, Date.valueOf(dataInizio));
            pstmt.setDate(5, Date.valueOf(dataFine));
            pstmt.setBoolean(6, statoRegistrazioni);
            pstmt.setInt(7, organizzatoreId);
            pstmt.executeUpdate();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAll() throws SQLException {
        String sql = "SELECT * FROM hackathon";
        List<String> hackathons = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String hackathon = "ID: " + rs.getInt("id") +
                        ", Nome: " + rs.getString("nome") +
                        ", Descrizione: " + rs.getString("descrizione") +
                        ", Luogo: " + rs.getString("luogo") +
                        ", Data Inizio: " + rs.getString("data_inizio") +
                        ", Data Fine: " + rs.getString("data_fine") +
                        ", Stato Registrazioni: " + rs.getBoolean("stato_registrazioni") +
                        ", Organizzatore ID: " + rs.getInt("organizzatore_id");
                hackathons.add(hackathon);
            }
        }
        return hackathons;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findByRegistrazioniAperte(boolean statoRegistrazioni) throws SQLException {
        String sql = "SELECT id, nome FROM hackathon WHERE stato_registrazioni = ?";
        List<String> hackathons = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, statoRegistrazioni);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String hackathon = rs.getInt("id") + " - " + rs.getString("nome");
                    hackathons.add(hackathon);
                }
            }
        }
        return hackathons;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getClassifica(int hackathonId) throws SQLException {
        String sql = "SELECT t.id AS team_id, t.nome AS team_nome, AVG(v.punteggio) AS punteggio_medio " +
                "FROM team t " +
                "JOIN valutazione v ON t.id = v.team_id " +
                "WHERE t.hackathon_id = ? " +
                "GROUP BY t.id, t.nome " +
                "ORDER BY punteggio_medio DESC";

        List<String> classifica = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hackathonId);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int teamId = rs.getInt("team_id");
                    String teamNome = rs.getString("team_nome");
                    double punteggioMedio = rs.getDouble("punteggio_medio");


                    String riga = String.format("%d;%s;%.2f", teamId, teamNome.replace(";", " "), punteggioMedio);
                    classifica.add(riga);

                }
            }
        }
        return classifica;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrganizzatoreIdByHackathon(int hackathonId) throws SQLException {
        String sql = "SELECT organizzatore_id FROM hackathon WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hackathonId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("organizzatore_id");
            } else {
                throw new SQLException("Hackathon non trovato con ID: " + hackathonId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getHackathonByOrganizzatoreId(int organizzatoreId) throws SQLException {

        String sql = "SELECT id, nome FROM hackathon WHERE organizzatore_id = ?";
        List<String> result = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, organizzatoreId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    result.add(id + " - " + nome);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIdByNameAndOrganizzatore(String nome, int organizzatoreId) throws SQLException {
        String sql = "SELECT id FROM hackathon WHERE nome = ? AND organizzatore_id = ? LIMIT 1";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, organizzatoreId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1;
            }
        }
    }



}