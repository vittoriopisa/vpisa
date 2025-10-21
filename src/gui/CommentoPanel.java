package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * Pannello Swing per la gestione dei commenti ai documenti.
 *
 * <p>Questa GUI permette ai giudici di:
 * <ul>
 *   <li>Aggiungere un nuovo commento a un documento</li>
 *   <li>Visualizzare i commenti associati a un documento</li>
 * </ul>
 *
 * <p>Il pannello si interfaccia con il {@link Controller} per tutte
 * le operazioni di business e di accesso ai dati.</p>
 */
public class CommentoPanel extends JPanel {
    private final Controller controller;
    private final int loggedUserId;
    private final String loggedUserTipo;


    private final JTextField tfDocumentoId;

    private final JTextArea taTesto;
    private final JTextArea taRisultati;


    /**
     * Crea e inizializza il pannello per la gestione dei commenti.
     *
     * @param controller     il controller che gestisce la logica applicativa
     * @param loggedUserId   l'ID dell’utente loggato
     * @param loggedUserTipo il tipo di utente loggato (es. "giudice", "concorrente", ecc.)
     */
    public CommentoPanel(Controller controller, int loggedUserId, String loggedUserTipo) {
        this.controller = controller;
        this.loggedUserId = loggedUserId;
        this.loggedUserTipo = loggedUserTipo;


        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));





        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Gestione Commenti"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID Documento:"), gbc);
        tfDocumentoId = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfDocumentoId, gbc);





        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        inputPanel.add(new JLabel("Testo:"), gbc);
        taTesto = new JTextArea(5, 20);
        taTesto.setWrapStyleWord(true);
        taTesto.setLineWrap(true);
        JScrollPane testoScroll = new JScrollPane(taTesto);
        gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(testoScroll, gbc);




        add(inputPanel, BorderLayout.CENTER);


        taRisultati = new JTextArea(10, 30);
        taRisultati.setEditable(false);
        JScrollPane resultScroll = new JScrollPane(taRisultati);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Risultati"));
        add(resultScroll, BorderLayout.EAST);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnSave = new JButton("Aggiungi Commento");

        JButton btnShow = new JButton("Mostra Commenti");



        btnSave.addActionListener(_ -> saveCommento());

        btnShow.addActionListener(_ -> showCommentiByDocumentoId());




        buttonPanel.add(btnSave);

        buttonPanel.add(btnShow);


        add(buttonPanel, BorderLayout.SOUTH);
    }



    private void saveCommento() {
        try {
            if (!"giudice".equalsIgnoreCase(loggedUserTipo)) {
                showResults(" Solo i giudici possono aggiungere commenti.");
                return;
            }

            int documentoId = Integer.parseInt(tfDocumentoId.getText());

            // Controllo hackathon (documento → team → hackathon)
            int documentoHackathonId = controller.getHackathonIdByDocumento(documentoId);
            int giudiceHackathonId = controller.getHackathonIdByUser(loggedUserId);

            if (documentoHackathonId != giudiceHackathonId) {
                showResults(" Non puoi commentare documenti di un altro hackathon.");
                return;
            }

            String testo = taTesto.getText();
            String result = controller.saveCommento(documentoId, loggedUserId, testo);
            showResults(result);
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        } catch (Exception ex) {
            showResults("Errore: " + ex.getMessage());
        }
    }



    private void showCommentiByDocumentoId() {
        try {
            int documentoId = Integer.parseInt(tfDocumentoId.getText());
            taRisultati.setText(""); // pulizia area risultati

            for (String commento : controller.getCommentiByDocumentoId(documentoId)) {
                showResults(commento);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        }
    }


    private void clearFields() {

        taTesto.setText("");
        tfDocumentoId.setText("");
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
