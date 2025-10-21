package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * Pannello Swing per la gestione dell'unione ai team da parte dei concorrenti.
 * <p>
 * Permette agli utenti di:
 * <ul>
 *     <li>Aggiungersi a un team di un hackathon di cui fanno parte</li>
 *     <li>Rimuoversi da un team di cui fanno parte</li>
 * </ul>
 */
public class UnioneTeamPanel extends JPanel {
    private final Controller controller;
    private final int loggedUserId;





    private final JComboBox<String> cbTeam;

    private final JTextArea taRisultati;

    /**
     * Crea un pannello per gestire l’adesione a team.
     *
     * @param controller  riferimento al controller
     * @param loggedUserId ID dell’utente loggato
     */
    public UnioneTeamPanel(Controller controller, int loggedUserId) {
        this.controller = controller;
        this.loggedUserId = loggedUserId;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));




        formPanel.add(new JLabel("Team:"));

        cbTeam = new JComboBox<>();
        formPanel.add(cbTeam);



        add(formPanel, BorderLayout.NORTH);

        taRisultati = new JTextArea(10, 30);
        taRisultati.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taRisultati);
        add(scrollPane);



        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 10, 10));

        JButton btnAddToTeam = new JButton("Aggiungiti al Team");
        JButton btnRemoveFromTeam = new JButton("Esci dal Team");





        btnAddToTeam.addActionListener(_-> addUtenteToTeam());
        btnRemoveFromTeam.addActionListener(_ -> removeUtenteFromTeam());

        buttonPanel.add(btnAddToTeam);
        buttonPanel.add(btnRemoveFromTeam);


        add(buttonPanel, BorderLayout.SOUTH);

        loadTeamsForLoggedUser();
    }



    private void addUtenteToTeam() {
        try {

            String selected = (String) cbTeam.getSelectedItem();
            Integer teamId = null;
            if (selected != null && selected.contains(" - ")) {
                teamId = Integer.parseInt(selected.split(" - ")[0].trim());
            }

            if (teamId == null) {
                JOptionPane.showMessageDialog(this, "Seleziona un team valido.");
                return;
            }

            int currentTeamId = controller.getTeamIdByUser(loggedUserId);
            if (currentTeamId > 0) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi aggiungerti a un altro team: sei già nel team con ID " + currentTeamId);
                return;
            }


            int hackathonId = controller.getHackathonIdByTeam(teamId);


            int userHackathonId = controller.getHackathonIdByUser(loggedUserId);

            if (hackathonId != userHackathonId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi aggiungerti a questo team: appartiene a un altro hackathon.");
                return;
            }

            String result = controller.addUtenteToTeam(loggedUserId, teamId);
            showResults(result);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    private void removeUtenteFromTeam() {
        try {

            int currentTeamId = controller.getTeamIdByUser(loggedUserId);

            if (currentTeamId == -1 || currentTeamId == 0) {
                JOptionPane.showMessageDialog(this, "Non appartieni a nessun team.");
                return;
            }

            String result = controller.removeUtenteFromTeam(loggedUserId,currentTeamId);
            showResults(result);

            loadTeamsForLoggedUser();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }



    private void loadTeamsForLoggedUser() {
        cbTeam.removeAllItems();
        try {
            int hackathonId = controller.getHackathonIdByUser(loggedUserId);
            if (hackathonId == -1) {
                cbTeam.addItem("Nessun hackathon trovato");
                return;
            }

            var teams = controller.getTeamsByHackathonId(hackathonId);
            for (String t : teams) {
                cbTeam.addItem(t); // formato "id - nome"
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei team: " + e.getMessage());
        }
    }


    /**
     * Mostra un messaggio di output nell’area dei risultati.
     *
     * @param message testo da aggiungere all’area dei risultati
     */
    public void showResults(String message) {
        taRisultati.append(message + "\n");
    }

}
