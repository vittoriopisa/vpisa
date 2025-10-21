package gui;


import controller.Controller;
import javax.swing.*;
import java.awt.*;

import java.time.LocalDate;

/**
 * Pannello Swing per la gestione dei documenti caricati dai team.
 *
 * <p>Questa GUI permette agli utenti (membri di un team) di:
 * <ul>
 *   <li>Aggiungere un nuovo documento al proprio team</li>
 *   <li>Visualizzare i documenti associati al proprio team</li>
 *   <li>Selezionare un documento specifico per la consultazione</li>
 * </ul>
 *
 * <p>Il pannello comunica con il {@link Controller} per tutte le
 * operazioni di salvataggio e recupero dati.</p>
 */
public class DocumentoPanel extends JPanel {
    private final Runnable onDocumentSaved;
    private final Controller controller;
    private final int loggedUserId;       // id utente loggato




    private final JTextField tfTitolo;
    private final JTextArea taDescrizione;
    private final JTextField tfFormato;
    private final JTextField tfDimensione;
    private final JTextField tfTipo;

    private final JComboBox<String> cbDocumento;

    private final JTextArea taRisultati;

    /**
     * Crea e inizializza il pannello per la gestione dei documenti.
     *
     * @param controller   il controller che gestisce la logica applicativa
     * @param loggedUserId l’ID dell’utente loggato
     * @param onDocumentSaved  azione da eseguire quando un documento viene salvato
     */
    public DocumentoPanel(Controller controller, int loggedUserId, Runnable onDocumentSaved) {
        this.controller = controller;
        this.loggedUserId = loggedUserId;
        this.onDocumentSaved = onDocumentSaved;




        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));




        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Gestione Documenti"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;








        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Titolo Documento:"), gbc);
        tfTitolo = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfTitolo, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        inputPanel.add(new JLabel("Descrizione:"), gbc);
        taDescrizione = new JTextArea(4, 20);
        taDescrizione.setLineWrap(true);
        taDescrizione.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(taDescrizione);
        gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        inputPanel.add(descScroll, gbc);

        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;





        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Formato:"), gbc);
        tfFormato = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfFormato, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Dimensione (byte):"), gbc);
        tfDimensione = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfDimensione, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Tipo Documento:"), gbc);
        tfTipo = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfTipo, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Documento:"), gbc);

        cbDocumento = new JComboBox<>();
        gbc.gridx = 1;
        inputPanel.add(cbDocumento, gbc);

        add(inputPanel, BorderLayout.CENTER);


        taRisultati = new JTextArea(16, 30);
        taRisultati.setEditable(false);
        JScrollPane resultScroll = new JScrollPane(taRisultati);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Risultati"));
        add(resultScroll, BorderLayout.EAST);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnSave = new JButton("Aggiungi Documento");


        JButton btnShow = new JButton("Mostra Documenti");




        btnSave.addActionListener(_ -> saveDocumento());


        btnShow.addActionListener(_ -> showDocumentiByTeamId());


        buttonPanel.add(btnSave);


        buttonPanel.add(btnShow);

        add(buttonPanel, BorderLayout.SOUTH);
        loadDocumentiForLoggedUser();
    }


    private void saveDocumento() {
        try {

            int teamId = controller.getTeamIdByUser(loggedUserId);
            if (teamId <= 0) {
                JOptionPane.showMessageDialog(this, "Non appartieni a nessun team. Impossibile aggiungere documenti.");
                return;
            }
            String titolo = tfTitolo.getText();
            String descrizione = taDescrizione.getText();
            LocalDate dataCreazione = LocalDate.now();
            String formato = tfFormato.getText();
            long dimensione =0;
            try {
                String dimStr = tfDimensione.getText().trim();
                dimensione = dimStr.isEmpty() ? 0 : Long.parseLong(dimStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Inserisci una dimensione valida (numero intero).");
                return;
            }
            String tipo = tfTipo.getText();

            String result = controller.saveDocumento(teamId, titolo, descrizione, dataCreazione, formato, dimensione, tipo);
            showResults(result);
            clearFields();
            if (onDocumentSaved != null) {
                onDocumentSaved.run();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        }
    }






    private void showDocumentiByTeamId() {
        try {

            int teamId = controller.getTeamIdByUser(loggedUserId);

            if (teamId <= 0) {
                JOptionPane.showMessageDialog(this, "Non appartieni a nessun team.");
                return;
            }

            taRisultati.setText("");


            String selected = (String) cbDocumento.getSelectedItem();
            Integer documentoId = null;

            if (selected != null && selected.contains(" - ")) {
                documentoId = Integer.parseInt(selected.split(" - ")[0].trim());
            }



            var documenti = controller.getDocumentoByTeamId(teamId,documentoId);

            if (documenti == null || documenti.isEmpty()) {
                showResults("Nessun documento trovato per il team con ID: " + teamId);
            } else {
                for (String msg : documenti) {
                    showResults(msg);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori selezionati.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    private void loadDocumentiForLoggedUser() {
        cbDocumento.removeAllItems();
        try {
            int teamId = controller.getTeamIdByUser(loggedUserId);
            if (teamId <=0) {
                cbDocumento.addItem("Nessun team trovato");
                return;
            }

            Integer documentoId = null;

            var docs = controller.getDocumentoByTeamId(teamId,documentoId); // lista di stringhe con titoli/documenti
            if (docs == null || docs.isEmpty()) {
                cbDocumento.addItem("Nessun documento trovato");
            } else {
                for (String d : docs) {
                    cbDocumento.addItem(d);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei documenti: " + e.getMessage());
        }
    }


    private void clearFields() {


        tfTitolo.setText("");
        taDescrizione.setText("");
        tfTipo.setText("");
        tfFormato.setText("");
        tfDimensione.setText("");
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
