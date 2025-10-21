package dao.impl;

import dao.ProblemaDAO;
import db.DatabaseConnectionSingleton;

import java.sql.*;

/**
 * Implementazione concreta dell’interfaccia {@link ProblemaDAO}.
 * <p>
 * Gestisce tramite JDBC la creazione e l’assegnazione dei problemi ai team,
 * assicurando i vincoli di coerenza tra team, giudice e hackathon.
 */
public class ProblemaDAOImpl implements ProblemaDAO {

    /**
     * {@inheritDoc}
     * <p>
     * La logica esegue i seguenti controlli:
     * <ul>
     *   <li>Se viene specificato un {@code teamId}, verifica che il team e il giudice appartengano
     *       allo stesso hackathon.</li>
     *   <li>Se {@code problemaId} è {@code null}, viene creato un nuovo problema con i dati forniti.
     *       Se associato a un team, viene controllato che il team non abbia già un problema.</li>
     *   <li>Se {@code problemaId} non è {@code null}, il problema esistente viene assegnato al team
     *       (sempre dopo aver verificato che il team sia libero).</li>
     * </ul>
     *
     * @param titolo      titolo del problema
     * @param descrizione descrizione del problema
     * @param teamId      ID del team a cui assegnare il problema (può essere {@code null})
     * @param giudiceId   ID del giudice che propone o assegna il problema
     * @param problemaId  ID del problema esistente da assegnare (se {@code null} viene creato un nuovo problema)
     * @return {@code true} se il problema è stato creato o assegnato correttamente,
     *         {@code false} se il team non appartiene allo stesso hackathon del giudice
     *         o se ha già un problema associato
     * @throws SQLException se si verifica un errore durante l’accesso al database
     */
    @Override
    public boolean assignProblemaToTeam(String titolo, String descrizione, Integer teamId, int giudiceId, Integer problemaId) throws SQLException {
        try (Connection conn = DatabaseConnectionSingleton.getInstance().getConnection()) {


            if (teamId != null) {
                Integer teamHackathonId = null;
                Integer giudiceHackathonId = null;


                String sqlTeam = "SELECT hackathon_id FROM team WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlTeam)) {
                    stmt.setInt(1, teamId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            teamHackathonId = rs.getInt("hackathon_id");
                        }
                    }
                }


                String sqlGiudice = "SELECT hackathon_id FROM utente WHERE id = ? AND tipo_utente = 'giudice'";
                try (PreparedStatement stmt = conn.prepareStatement(sqlGiudice)) {
                    stmt.setInt(1, giudiceId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            giudiceHackathonId = rs.getInt("hackathon_id");
                        }
                    }
                }

                // Se non appartengono allo stesso hackathon → blocco
                if (teamHackathonId == null || giudiceHackathonId == null || !teamHackathonId.equals(giudiceHackathonId)) {
                    return false;
                }
            }


            if (problemaId == null) {
                if (teamId != null) {
                    // Verifico che il team non abbia già un problema
                    String checkSql = "SELECT COUNT(*) FROM problema WHERE team_id = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                        checkStmt.setInt(1, teamId);
                        try (ResultSet rs = checkStmt.executeQuery()) {
                            if (rs.next() && rs.getInt(1) > 0) {
                                return false; // Team già occupato
                            }
                        }
                    }
                }


                String sql = "INSERT INTO problema (titolo, descrizione, team_id, giudice_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, titolo);
                    pstmt.setString(2, descrizione);
                    if (teamId != null) {
                        pstmt.setInt(3, teamId);
                    } else {
                        pstmt.setNull(3, java.sql.Types.INTEGER);
                    }
                    pstmt.setInt(4, giudiceId);
                    pstmt.executeUpdate();
                }
                return true;
            }


            if (teamId != null) {
                String checkSql = "SELECT COUNT(*) FROM problema WHERE team_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, teamId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            return false; // Team già occupato
                        }
                    }
                }

                String assignSql = "UPDATE problema SET team_id = ? WHERE id = ?";
                try (PreparedStatement assignStmt = conn.prepareStatement(assignSql)) {
                    assignStmt.setInt(1, teamId);
                    assignStmt.setInt(2, problemaId);
                    assignStmt.executeUpdate();
                }
                return true;
            }

            return false;
        }
    }


}


