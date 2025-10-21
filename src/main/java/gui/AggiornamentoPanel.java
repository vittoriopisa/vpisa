package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;

/**
 * Pannello Swing per la gestione degli aggiornamenti legati ai documenti di un team.
 *
 * <p>Consente a un utente concorrente di:
 * <ul>
 *   <li>Visualizzare i documenti associati al proprio team</li>
 *   <li>Aggiungere un nuovo aggiornamento a un documento</li>
 *   <li>Eliminare un aggiornamento esistente, se appartenente al proprio team</li>
 * </ul>
 *
 * <p>La classe si appoggia al {@link Controller} per la logica applicativa e per l’accesso ai dati.</p>
 */
public class AggiornamentoPanel extends JPanel {  // Cambiato da JFrame a JPanel
    private final Controller controller;
    private final int loggedUserId;       // id utente loggato




    private final JComboBox<String> cbDocumento;
    private final JTextArea taContenuto;
    private final JTextArea taRisultati;
    private final JTextField tfAggiornamentoId;

    /**
     * Crea e inizializza il pannello per la gestione degli aggiornamenti.
     *
     * @param controller riferimento al controller per interagire con il backend
     * @param loggedUserId ID dell’utente attualmente loggato
     */
    public AggiornamentoPanel(Controller controller, int loggedUserId) {
        this.controller = controller;
        this.loggedUserId = loggedUserId;




        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));





        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Gestione Aggiornamenti"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;




        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Documento:"), gbc);
        cbDocumento = new JComboBox<>();
        gbc.gridx = 1;
        inputPanel.add(cbDocumento, gbc);


        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        inputPanel.add(new JLabel("Contenuto:"), gbc);
        taContenuto = new JTextArea(5, 20);
        taContenuto.setWrapStyleWord(true);
        taContenuto.setLineWrap(true);
        JScrollPane contenutoScroll = new JScrollPane(taContenuto);
        gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(contenutoScroll, gbc);


        gbc.gridx = 0; gbc.gridy = 3; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(new JLabel("ID Aggiornamento:"), gbc);
        tfAggiornamentoId = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfAggiornamentoId, gbc);


        taRisultati = new JTextArea(10, 30);
        taRisultati.setEditable(false);
        JScrollPane risultatiScroll = new JScrollPane(taRisultati);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnSave = new JButton("Aggiungi Aggiornamento");

        JButton btnDelete = new JButton("Elimina Aggiornamento");



        btnSave.addActionListener(_ -> saveAggiornamento());



        btnDelete.addActionListener(_ -> deleteAggiornamento());




        buttonPanel.add(btnSave);

        buttonPanel.add(btnDelete);


        add(inputPanel, BorderLayout.CENTER);   // i campi al centro
        add(buttonPanel, BorderLayout.SOUTH);   // bottoni in basso
        add(risultatiScroll, BorderLayout.EAST);

        loadDocumentiForTeam();
    }

    public void loadDocumentiForTeam() {
        cbDocumento.removeAllItems();
        try {
            int teamId = controller.getTeamIdByUser(loggedUserId);
            if (teamId == -1) {
                cbDocumento.addItem("Nessun team trovato");
                return;
            }

            var docs = controller.getDocumentoByTeamId(teamId, null);
            if (docs.isEmpty()) {
                cbDocumento.addItem("Nessun documento trovato");
            } else {
                for (String d : docs) {
                    cbDocumento.addItem(d); // formato "id - titolo"
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento documenti: " + e.getMessage());
        }
    }



    private void saveAggiornamento() {
        try {

            String userType = controller.getUserType(loggedUserId);
            if (userType == null || !userType.equalsIgnoreCase("concorrente")) {
                JOptionPane.showMessageDialog(this,
                        "Solo gli utenti concorrenti possono aggiungere aggiornamenti.");
                return;
            }

            //int documentoId = Integer.parseInt(tfDocumentoId.getText());
            String selected = (String) cbDocumento.getSelectedItem();
            if (selected == null || !selected.contains(" - ")) {
                JOptionPane.showMessageDialog(this, "Seleziona un documento valido.");
                return;
            }
            int documentoId = Integer.parseInt(selected.split(" - ")[0].trim());
            String contenuto = taContenuto.getText();


            int teamId = controller.getTeamIdByUser(loggedUserId);

            String result = controller.saveAggiornamento(teamId, documentoId, contenuto);
            showResults(result);
            clearFields();

        }  catch (Exception ex) {
            showResults(" Errore durante il salvataggio: " + ex.getMessage());
        }
    }



    private void deleteAggiornamento() {
        try {
            int aggiornamentoId = Integer.parseInt(tfAggiornamentoId.getText());


            String userType = controller.getUserType(loggedUserId);
            if (userType == null || !userType.equalsIgnoreCase("concorrente")) {
                JOptionPane.showMessageDialog(this,
                        "Solo un concorrente può eliminare aggiornamenti.");
                return;
            }

            String selected = (String) cbDocumento.getSelectedItem();
            if (selected == null || !selected.contains(" - ")) {
                JOptionPane.showMessageDialog(this, "Seleziona un documento valido.");
                return;
            }



            int documentoId = controller.getDocumentoIdByAggiornamento(aggiornamentoId);


            int teamIdDocumento = controller.getTeamIdByDocumento(documentoId);


            int teamIdUtente = controller.getTeamIdByUser(loggedUserId);

            if (teamIdDocumento != teamIdUtente) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi eliminare un aggiornamento di un documento che non appartiene al tuo team.");
                return;
            }


            String result = controller.deleteAggiornamento(aggiornamentoId);
            showResults(result);
            clearFields();

        }  catch (Exception ex) {
            showResults("Errore durante l'eliminazione: " + ex.getMessage());
        }
    }



    private void clearFields() {

        taContenuto.setText("");
        tfAggiornamentoId.setText("");
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