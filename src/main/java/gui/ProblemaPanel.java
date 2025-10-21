package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * Pannello Swing dedicato alla gestione dei problemi negli hackathon.
 * <p>
 * Permette ai giudici di:
 * <ul>
 *     <li>Creare un nuovo problema (specificando titolo e descrizione).</li>
 *     <li>Assegnare un problema esistente a un team specifico.</li>
 * </ul>
 * L’accesso è limitato agli utenti con ruolo {@code giudice}.
 */
public class ProblemaPanel extends JPanel {
    private final Controller controller;
    private final int loggedUserId;
    private final String loggedUserTipo;


    private final JTextField tfTitolo;
    private final JTextField tfDescrizione;
    private final JTextField tfTeamId;

    private final JTextArea taRisultati;

    /**
     * Crea un nuovo pannello per la gestione dei problemi.
     *
     * @param controller    riferimento al controller per la logica applicativa
     * @param loggedUserId  ID dell’utente attualmente loggato
     * @param loggedUserTipo tipo dell’utente loggato (es. {@code giudice})
     */
    public ProblemaPanel(Controller controller,int loggedUserId, String loggedUserTipo) {
        this.controller = controller;
        this.loggedUserId = loggedUserId;
        this.loggedUserTipo = loggedUserTipo;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));



        formPanel.add(new JLabel("Titolo:"));
        tfTitolo = new JTextField(20);
        formPanel.add(tfTitolo);

        formPanel.add(new JLabel("Descrizione:"));
        tfDescrizione = new JTextField(20);
        formPanel.add(tfDescrizione);

        formPanel.add(new JLabel("Team ID:"));
        tfTeamId = new JTextField(15);
        formPanel.add(tfTeamId);


        add(formPanel, BorderLayout.NORTH);


        taRisultati = new JTextArea(10, 30);
        taRisultati.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taRisultati);
        add(scrollPane);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));


        JButton btnAssign = new JButton("Assegna Problema al Team");






        btnAssign.addActionListener(_-> assignProblema());


        buttonPanel.add(btnAssign);

        add(buttonPanel, BorderLayout.SOUTH);
    }





    private void assignProblema() {
        try {
            if (!"giudice".equalsIgnoreCase(loggedUserTipo)) {
                showResults("Solo i giudici possono creare o assegnare problemi.");
                return;
            }

            String titolo = tfTitolo.getText().isEmpty() ? null : tfTitolo.getText();
            String descrizione = tfDescrizione.getText().isEmpty() ? null : tfDescrizione.getText();
            String teamText = tfTeamId.getText().trim();


            Integer teamId = teamText.isEmpty() ? null : Integer.parseInt(teamText);



            if (titolo.isEmpty() || descrizione.isEmpty() || teamId == null) {
                JOptionPane.showMessageDialog(this,
                        "Compila Titolo, Descrizione e Team ID per creare e assegnare il problema.");
                return;
            }
                int teamHackathonId = controller.getHackathonIdByTeam(teamId);
                int giudiceHackathonId = controller.getHackathonIdByUser(loggedUserId);

                if (teamHackathonId != giudiceHackathonId) {
                    showResults(" Non puoi assegnare problemi a team di un altro hackathon.");
                    return;
                }


            String result= controller.assignProblemaToTeam(titolo, descrizione, teamId, loggedUserId, null);;



            showResults(result);
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        } catch (Exception ex) {
            showResults("Errore: " + ex.getMessage());
        }
    }


    private void clearFields() {
        tfTeamId.setText("");

        tfTitolo.setText("");
        tfDescrizione.setText("");
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
