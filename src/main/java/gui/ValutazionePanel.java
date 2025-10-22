package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;


/**
 * Pannello Swing dedicato alla gestione delle valutazioni dei team.
 * <p>
 * Consente ai giudici di:
 * <ul>
 *     <li>Inserire o aggiornare valutazioni (punteggio e feedback) per un team</li>
 *     <li>Visualizzare tutte le valutazioni di un team</li>
 * </ul>
 * L’accesso alle funzionalità è riservato agli utenti con ruolo {@code giudice}.
 */
public class ValutazionePanel extends JPanel{
    private final Controller controller;
    private final int loggedUserId;
    private final String loggedUserTipo;


    private final JTextField tfTeamId;
    private final JComboBox<Integer> cbPunteggio;


    private final JTextArea taFeedback;

    private final JTextArea taRisultati;

    /**
     * Crea un pannello per la gestione delle valutazioni.
     *
     * @param controller    riferimento al controller
     * @param loggedUserId  ID dell’utente loggato
     * @param loggedUserTipo tipo dell’utente loggato (es. {@code giudice})
     */
    public ValutazionePanel(Controller controller,int loggedUserId, String loggedUserTipo) {
        this.controller = controller;
        this.loggedUserId = loggedUserId;
        this.loggedUserTipo = loggedUserTipo;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));



        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row=0;


        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Team ID:"), gbc);
        tfTeamId = new JTextField(8);
        gbc.gridx = 1;
        formPanel.add(tfTeamId, gbc);
        row++;




        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(new JLabel("Punteggio:"), gbc);
        Integer[] scores = new Integer[11];
        for (int i = 0; i <= 10; i++) scores[i] = i;
        cbPunteggio = new JComboBox<>(scores);
        cbPunteggio.setSelectedIndex(0); // default 0
        gbc.gridx = 1;
        formPanel.add(cbPunteggio, gbc);
        row++;


        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Feedback:"), gbc);
        taFeedback = new JTextArea(5, 20);
        taFeedback.setLineWrap(true);
        taFeedback.setWrapStyleWord(true);
        JScrollPane feedbackScroll = new JScrollPane(taFeedback);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        formPanel.add(feedbackScroll, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;





        add(formPanel, BorderLayout.NORTH);


        taRisultati = new JTextArea(10, 40);
        taRisultati.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(taRisultati);
        resultScrollPane.setPreferredSize(new Dimension(500, 200));
        add(resultScrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));


        JButton btnSave = new JButton("Salva Valutazione");


        JButton btnShowValutazioni = new JButton("Mostra Valutazioni Team");





        btnSave.addActionListener(_ -> saveValutazione());


        btnShowValutazioni.addActionListener(_-> showValutazioniByTeam());


        buttonPanel.add(btnSave);


        buttonPanel.add(btnShowValutazioni);

        add(buttonPanel, BorderLayout.SOUTH);
    }




    private void saveValutazione() {
        try {
            if (!"giudice".equalsIgnoreCase(loggedUserTipo)) {
                showResults("Solo i giudici possono aggiungere valutazioni.");
                return;
            }

            int teamId = Integer.parseInt(tfTeamId.getText());

            int teamHackathonId = controller.getHackathonIdByTeam(teamId);
            int giudiceHackathonId = controller.getHackathonIdByUser(loggedUserId);

            if (teamHackathonId != giudiceHackathonId) {
                showResults(" Non puoi valutare team di un altro hackathon.");
                return;
            }

            int punteggio = (Integer)cbPunteggio.getSelectedItem();
            String feedback = taFeedback.getText();


            String result = controller.saveValutazione(teamId, loggedUserId, punteggio, feedback);
            showResults(result);
            clearFields();


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio: " + ex.getMessage());
        }
    }





    private void showValutazioniByTeam() {
        try {
            String teamIdStr = tfTeamId.getText().trim();
            if (teamIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inserisci l'ID del team.");
                return;
            }

            int teamId;
            try {
                teamId = Integer.parseInt(teamIdStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Team ID non valido.");
                return;
            }

            taRisultati.setText("");
            var valutazioni = controller.getValutazioniByTeam(teamId);

            if (valutazioni == null || valutazioni.isEmpty()) {
                showResults("Nessuna valutazione trovata per il team con ID: " + teamId);
            } else {
                valutazioni.forEach(this::showResults);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore durante la visualizzazione delle valutazioni: " + ex.getMessage());
        }
    }


    private void clearFields() {
        tfTeamId.setText("");

       cbPunteggio.setSelectedIndex(0);
        taFeedback.setText("");

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
