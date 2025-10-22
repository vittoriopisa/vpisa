package dao.impl;

import dao.UtenteDAO;
import db.DatabaseConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Implementazione concreta dell'interfaccia {@link UtenteDAO}.
 * <p>
 * Fornisce l'accesso al database per la gestione degli utenti:
 * registrazione, autenticazione, gestione dei team e recupero informazioni
 * basate sul ruolo ({@code concorrente}, {@code giudice}, {@code organizzatore}).
 */
public class UtenteDAOImpl implements UtenteDAO {


    /**
     * {@inheritDoc}
     */
    @Override


    public void save(String nome, String cognome, String email, String passwordHash,
                     LocalDate dataRegistrazione, String tipoUtente,
                     Integer hackathonId, Integer teamId) throws SQLException {

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection()) {


            if (teamId != null && hackathonId != null) {
                String checkTeamSql = "SELECT COUNT(*) FROM team WHERE id = ? AND hackathon_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkTeamSql)) {
                    checkStmt.setInt(1, teamId);
                    checkStmt.setInt(2, hackathonId);

                    try (ResultSet rs = checkStmt.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) == 0) {
                            throw new SQLException("Errore: il team con ID " + teamId +
                                    " non appartiene all'hackathon con ID " + hackathonId);
                        }
                    }
                }
            }

            String normalizedEmail = (email != null) ? email.trim().toLowerCase() : null;



            //  Inserimento utente
            String sql = "INSERT INTO utente (nome, cognome, email, password_hash, data_registrazione, tipo_utente, hackathon_id, team_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nome);
                pstmt.setString(2, cognome);
                pstmt.setString(3, normalizedEmail);


                pstmt.setString(4, passwordHash);

                pstmt.setDate(5, Date.valueOf(dataRegistrazione));
                pstmt.setString(6, tipoUtente);


                if (hackathonId != null) {
                    pstmt.setInt(7, hackathonId);
                } else {
                    pstmt.setNull(7, java.sql.Types.INTEGER);
                }


                if (teamId != null) {
                    pstmt.setInt(8, teamId);
                } else {
                    pstmt.setNull(8, java.sql.Types.INTEGER);
                }


                pstmt.executeUpdate();


            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> findByTipoUtenteForHackathon(int hackathonId, String tipoUtente) throws SQLException {
        String sql = "SELECT * FROM utente WHERE hackathon_id = ? AND tipo_utente = ?";
        List<String> utenti = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, hackathonId);
            pstmt.setString(2, tipoUtente);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String utente = "ID: " + rs.getInt("id") +
                            ", Nome: " + rs.getString("nome") +
                            ", Cognome: " + rs.getString("cognome") +
                            ", Email: " + rs.getString("email") +
                            ", Tipo Utente: " + rs.getString("tipo_utente") +
                            ", Hackathon ID: " + rs.getInt("hackathon_id") +
                            ", Team ID: " + rs.getObject("team_id");
                    utenti.add(utente);
                }
            }
        }
        return utenti;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addUserToTeam(int userId, int teamId) throws SQLException {
        // Verifica che l'utente sia di tipo "concorrente"
        String checkUserTypeSql = "SELECT tipo_utente FROM utente WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkUserTypeSql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tipoUtente = rs.getString("tipo_utente");
                    if (!"concorrente".equals(tipoUtente)) {
                        System.out.println("Errore: l'utente non è un concorrente.");
                        return;
                    }
                } else {
                    System.out.println("Errore: utente non trovato.");
                    return;
                }
            }
        }

        // Verifica che il team non abbia raggiunto la dimensione massima
        String checkTeamSizeSql = "SELECT COUNT(*) FROM utente WHERE team_id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkTeamSizeSql)) {
            pstmt.setInt(1, teamId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int teamSize = rs.getInt(1);
                    if (teamSize >= 6) {
                        System.out.println("Errore: il team ha già raggiunto la dimensione massima di 6 membri.");
                        return;
                    }
                }
            }
        }

        // Aggiungi l'utente al team
        String sql = "UPDATE utente SET team_id = ? WHERE id = ? AND tipo_utente = 'concorrente' AND hackathon_id = (SELECT hackathon_id FROM utente WHERE id = ?)";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
            System.out.println("Utente aggiunto al team con successo.");
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeUserFromTeam(int userId) throws SQLException {
        String sql = "UPDATE utente SET team_id = NULL WHERE id = ? AND team_id IS NOT NULL";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int updated = pstmt.executeUpdate();
            return updated > 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAllConcorrentiForTeam(int teamId,int hackathonId) throws SQLException {
        String sql = "SELECT * FROM utente WHERE team_id = ? AND hackathon_id = ? AND tipo_utente = 'concorrente'";
        List<String> concorrenti = new ArrayList<>();

        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, teamId);
            pstmt.setInt(2, hackathonId);


            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String concorrente = "ID: " + rs.getInt("id") +
                            ", Nome: " + rs.getString("nome") +
                            ", Cognome: " + rs.getString("cognome") +
                            ", Email: " + rs.getString("email");
                    concorrenti.add(concorrente);
                }
            }
        }
        return concorrenti;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int checkLoginAndGetId(String email, String password) throws SQLException {


        if (email == null) return -1;
        String normalizedEmail = email.trim().toLowerCase();

        String sql = "SELECT id, password_hash FROM utente WHERE email = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, normalizedEmail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");


                    boolean ok = security.PasswordSecurity.checkPassword(password, storedHash);


                    if (security.PasswordSecurity.checkPassword(password, storedHash)) {
                        return rs.getInt("id"); // ritorna l'ID se login ok
                    }
                }
            }
        }
        return -1; // login fallito
    }

    /*public int checkLoginAndGetId(String email, String password) throws SQLException {
        String sql = "SELECT id, password_hash FROM utente WHERE email = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String storedHash = rs.getString("password_hash");



                    // mostra lunghezza e caratteri invisibili del plain (hex)

                    /*for (byte b : password.getBytes(java.nio.charset.StandardCharsets.UTF_8)) {
                        System.out.printf("%02x", b);
                    }*/
                    //System.out.println();


                    /*if (storedHash != null) {
                        // prova a creare hash usando storedHash come salt: BCrypt.hashpw(plain, storedHash)
                        String recomputed = org.mindrot.jbcrypt.BCrypt.hashpw(password, storedHash);
                        System.out.println("[DEBUG LOGIN] BCrypt.hashpw(..., storedHash) = " + recomputed);
                        System.out.println("[DEBUG LOGIN] recomputed equals stored? " + recomputed.equals(storedHash));
                    } else {
                        System.out.println("[DEBUG LOGIN] storedHash == null");
                    }

                    boolean ok = false;
                    try {
                        ok = security.PasswordSecurity.checkPassword(password, storedHash);
                    } catch (Exception ex) {
                        System.out.println("[DEBUG LOGIN] checkPassword threw: " + ex);
                    }
                    System.out.println("[DEBUG LOGIN] BCrypt.checkpw returned = " + ok);

                    if (ok) return id;
                }
            }
        }
        return -1;
    }*/


        /**
         * {@inheritDoc}
         */
    @Override
    public String getUserTypeById(int userId) throws SQLException {
        String sql = "SELECT tipo_utente FROM utente WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipo_utente");
                }
            }
        }
        return null;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getHackathonIdByUser(int userId) throws SQLException {
        String sql = "SELECT hackathon_id FROM utente WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("hackathon_id");
            } else {
                throw new SQLException("Nessun utente trovato con ID " + userId);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override

    public String getNomeGiudiceById(int giudiceId) throws SQLException {
        String sql = "SELECT id, nome FROM utente WHERE id = ? AND tipo_utente = 'giudice'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, giudiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                } else {
                    throw new SQLException("Giudice non trovato con ID: " + giudiceId);
                }
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override

    public String getCognomeGiudiceById(int giudiceId) throws SQLException {
        String sql = "SELECT id, cognome FROM utente WHERE id = ? AND tipo_utente = 'giudice'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, giudiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cognome");
                } else {
                    throw new SQLException("Giudice non trovato con ID: " + giudiceId);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override

    public String getEmailGiudiceById(int giudiceId) throws SQLException {
        String sql = "SELECT id, email FROM utente WHERE id = ? AND tipo_utente = 'giudice'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, giudiceId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                } else {
                    throw new SQLException("Giudice non trovato con ID: " + giudiceId);
                }
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNomeOrgById(int organizzatoreId) throws SQLException {
        String sql = "SELECT  id,nome FROM utente WHERE id = ? AND tipo_utente = 'organizzatore'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizzatoreId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                } else {
                    throw new SQLException("Organizzatore non trovato con ID: " + organizzatoreId);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCognomeOrgById(int organizzatoreId) throws SQLException {
        String sql = "SELECT id, cognome FROM utente WHERE id = ? AND tipo_utente = 'organizzatore'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizzatoreId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cognome");
                } else {
                    throw new SQLException("Organizzatore non trovato con ID: " + organizzatoreId);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailOrgById(int organizzatoreId) throws SQLException {
        String sql = "SELECT id, email FROM utente WHERE id = ? AND tipo_utente = 'organizzatore'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizzatoreId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                } else {
                    throw new SQLException("Organizzatore non trovato con ID: " + organizzatoreId);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNomeConcById(int concorrenteId) throws SQLException {
        String sql = "SELECT id, nome FROM utente WHERE id = ? AND tipo_utente = 'concorrente'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, concorrenteId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                } else {
                    throw new SQLException("Concorrente non trovato con ID: " + concorrenteId);
                }
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCognomeConcById(int concorrenteId) throws SQLException {
        String sql = "SELECT id, cognome FROM utente WHERE id = ? AND tipo_utente = 'concorrente'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, concorrenteId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("cognome");
                } else {
                    throw new SQLException("Concorrente non trovato con ID: " + concorrenteId);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmailConcById(int concorrenteId) throws SQLException {
        String sql = "SELECT id, email FROM utente WHERE id = ? AND tipo_utente = 'concorrente'";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, concorrenteId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                } else {
                    throw new SQLException("Concorrente non trovato con ID: " + concorrenteId);
                }
            }
        }
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public int getTeamIdByUser(int userId) throws SQLException {
        String sql = "SELECT team_id FROM utente WHERE id = ?";
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int teamId = rs.getInt("team_id");
                    // Se la colonna è NULL, getInt restituisce 0, ma possiamo gestirlo esplicitamente:
                    if (rs.wasNull()) {
                        return 0; // convenzione: 0 = nessun team
                    }
                    return teamId;
                } else {
                    // Nessun utente trovato
                    return -1;
                }
            }
        }
    }





}