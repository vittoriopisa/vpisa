package gui;

import controller.Controller; // Import del controller
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pannello Swing per la gestione degli hackathon da parte degli organizzatori.
 *
 * <p>Questa GUI consente all’organizzatore loggato di:
 * <ul>
 *   <li>Creare un nuovo hackathon</li>
 *   <li>Visualizzare tutti gli hackathon</li>
 *   <li>Mostrare solo quelli con registrazioni aperte</li>
 *   <li>Accedere alla classifica dei team partecipanti</li>
 *   <li>Visualizzare gli utenti (concorrenti o giudici) iscritti a un hackathon</li>
 * </ul>
 *
 * <p>Il pannello interagisce con il {@link Controller} per tutte le operazioni.</p>
 */
public class HackathonPanel extends JPanel {
    private final Controller controller;


    private final JTextField tfNome;
    private final JTextArea taDescrizione;
    private final JTextField tfLuogo;
    private final JTextField tfDataInizio;
    private final JTextField tfDataFine;
    private final JComboBox<String> cbHackathonId;
    private final java.util.List<Integer> hackathonIds = new ArrayList<>();
    private final JTextArea taRisultati;
    private final int loggedOrganizzatoreId;
    private final JComboBox<String> cbTipoUtente;

    /**
     * Crea e inizializza il pannello per la gestione degli hackathon.
     *
     * @param controller            il controller che gestisce la logica applicativa
     * @param loggedOrganizzatoreId l’ID dell’organizzatore loggato
     */
    public HackathonPanel(Controller controller,int loggedOrganizzatoreId) {
        this.controller = controller;
        this.loggedOrganizzatoreId = loggedOrganizzatoreId;



        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));



        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Gestione Hackathon"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;


        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Nome Hackathon:"), gbc);
        tfNome = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfNome, gbc);


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
        inputPanel.add(new JLabel("Luogo:"), gbc);
        tfLuogo = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfLuogo, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Data Inizio (YYYY-MM-DD):"), gbc);
        tfDataInizio = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfDataInizio, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Data Fine (YYYY-MM-DD):"), gbc);
        tfDataFine = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(tfDataFine, gbc);




        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Hackathon:"), gbc);

        cbHackathonId = new JComboBox<>();
        gbc.gridx = 1;
        inputPanel.add(cbHackathonId, gbc);


        loadHackathonsForOrganizzatore();

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        inputPanel.add(new JLabel("Tipo Utente:"), gbc);
        cbTipoUtente = new JComboBox<>(new String[]{"concorrente", "giudice"});
        gbc.gridx = 1;
        inputPanel.add(cbTipoUtente, gbc);

        add(inputPanel, BorderLayout.CENTER);


        taRisultati = new JTextArea(8, 20);
        taRisultati.setEditable(false);
        JScrollPane resultScroll = new JScrollPane(taRisultati);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Risultati"));
        add(resultScroll, BorderLayout.EAST);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        JButton btnSave = new JButton("Aggiungi Hackathon");


        JButton btnShowAll = new JButton("Mostra Tutti gli Hackathon");
        JButton btnShowOpen = new JButton("Mostra Hackathon con Registrazioni Aperte");

        JButton btnShowClassifica = new JButton("Mostra Classifica");
        JButton btnShowUsersByType = new JButton("Mostra Utenti per Tipo");



        btnSave.addActionListener(_ -> saveHackathon());


        btnShowAll.addActionListener(_ -> showAllHackathons());
        btnShowOpen.addActionListener(_-> showHackathonsWithOpenRegistrations());

        btnShowClassifica.addActionListener(_ -> showClassifica());
        btnShowUsersByType.addActionListener(_ -> showUsersByTypeForHackathon());




        buttonPanel.add(btnSave);


        buttonPanel.add(btnShowAll);
        buttonPanel.add(btnShowOpen);

        buttonPanel.add(btnShowClassifica);
        buttonPanel.add(btnShowUsersByType);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void saveHackathon() {
        try {
            String nome = tfNome.getText();
            String descrizione = taDescrizione.getText();
            String luogo = tfLuogo.getText();
            LocalDate dataInizio = LocalDate.parse(tfDataInizio.getText());
            LocalDate dataFine = LocalDate.parse(tfDataFine.getText());


            String result = controller.saveHackathon(nome, descrizione, luogo, dataInizio, dataFine, loggedOrganizzatoreId);
            showResults(result);
            clearFields();
            if (result!=null && result.toLowerCase().contains("successo")) {
                loadHackathonsForOrganizzatore();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        }



    }






    private void showAllHackathons() {
        taRisultati.setText("");
        for (String h : controller.getAllHackathons(loggedOrganizzatoreId)) {
            showResults(h);
        }
    }


    private void showHackathonsWithOpenRegistrations() {
        taRisultati.setText("");
        for (String h : controller.getHackathonsWithOpenRegistrations(loggedOrganizzatoreId)) {
            showResults(h);
        }
    }





    private void showClassifica() {
        String selected = (String) cbHackathonId.getSelectedItem();
        if (selected == null || selected.trim().isEmpty() || "Nessun hackathon trovato".equals(selected)) {
            JOptionPane.showMessageDialog(this, "Seleziona un hackathon dalla lista.");
            return;
        }


        String nomeHackathon = selected.trim();

        try {
            int hackathonId = controller.getHackathonIdByNameAndOrganizzatore(nomeHackathon, loggedOrganizzatoreId);
            if (hackathonId <= 0) {
                JOptionPane.showMessageDialog(this, "Impossibile trovare l'ID dell'hackathon selezionato.");
                return;
            }


            int organizzatoreId = controller.getOrganizzatoreIdByHackathon(hackathonId);
            if (organizzatoreId != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi vedere la classifica di un hackathon che non ti appartiene.");
                return;
            }

            taRisultati.setText("");
            for (String entry : controller.getClassificaHackathon(hackathonId)) {
                showResults(entry);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il recupero della classifica: " + ex.getMessage());
        }
    }




    private void clearFields() {

        cbHackathonId.setSelectedIndex(-1);
        tfNome.setText("");
        tfDataInizio.setText("");
        taDescrizione.setText("");
        tfDataFine.setText("");

        tfLuogo.setText("");
    }


    private void showUsersByTypeForHackathon() {
        String selected = (String) cbHackathonId.getSelectedItem();
        if (selected == null || selected.trim().isEmpty() || "Nessun hackathon trovato".equals(selected)) {
            JOptionPane.showMessageDialog(this, "Seleziona un hackathon dalla lista.");
            return;
        }

        String nomeHackathon = selected.trim();

        try {
            int hackathonId = controller.getHackathonIdByNameAndOrganizzatore(nomeHackathon, loggedOrganizzatoreId);
            if (hackathonId <= 0) {
                JOptionPane.showMessageDialog(this, "Impossibile trovare l'ID dell'hackathon selezionato.");
                return;
            }

            int organizzatoreId = controller.getOrganizzatoreIdByHackathon(hackathonId);
            if (organizzatoreId != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi visualizzare utenti di un hackathon che non ti appartiene.");
                return;
            }

            String tipoUtente = (String) cbTipoUtente.getSelectedItem();
            taRisultati.setText("");
            var utenti = controller.getUtentiByTipoUtenteForHackathon(hackathonId, tipoUtente);

            if (utenti == null || utenti.isEmpty()) {
                showResults("Nessun utente trovato per il tipo " + tipoUtente + " nell'hackathon " + hackathonId);
            } else {
                utenti.forEach(this::showResults);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore durante il recupero utenti: " + ex.getMessage());
        }
    }


    private void loadHackathonsForOrganizzatore() {
        cbHackathonId.removeAllItems();
        hackathonIds.clear();

        try {
            var hackathons = controller.getAllHackathons(loggedOrganizzatoreId);

            if (hackathons == null || hackathons.isEmpty()) {
                cbHackathonId.addItem("Nessun hackathon trovato");
                return;
            }

            for (String row : hackathons) {
                if (row == null || row.trim().isEmpty()) continue;

                String nome = null;
                int id = -1;

                // --- Tentativi di parsing robusti per trovare ID e nome ---

                // 1) Formato "id;nome;..."
                if (row.contains(";")) {
                    String[] parts = row.split(";", 6);
                    String first = parts[0].trim();
                    try {
                        id = Integer.parseInt(first);
                        if (parts.length > 1) nome = parts[1].trim();
                    } catch (NumberFormatException ignored) {}
                }

                // 2) Formato "ID: 42, Nome: ..."
                if (id == -1 && row.contains("ID:")) {
                    try {
                        String[] parts = row.split(",\\s*");
                        for (String p : parts) {
                            p = p.trim();
                            if (p.startsWith("ID:")) {
                                id = Integer.parseInt(p.split(":")[1].trim());
                            } else if (p.startsWith("Nome:") || p.startsWith("Nome ")) {
                                nome = p.substring(p.indexOf(':') + 1).trim();
                            }
                        }
                    } catch (Exception ignored) {}
                }

                // 3) Formato "42 - Nome..."
                if (id == -1 && row.contains(" - ")) {
                    try {
                        String[] parts = row.split(" - ", 2);
                        id = Integer.parseInt(parts[0].trim());
                        nome = parts.length > 1 ? parts[1].trim() : null;
                    } catch (NumberFormatException ignored) {}
                }

                // 4) Fallback: se non abbiamo id ma la riga ha almeno un nome
                if (nome == null) {
                    try {
                        if (row.contains(";")) {
                            nome = row.split(";", 2)[0].trim();
                        } else {
                            nome = row.trim();
                        }
                    } catch (Exception ignored) {}
                }

                // --- Aggiungo solo il nome nella combo, e l'id nella lista parallela ---
                cbHackathonId.addItem(nome != null && !nome.isEmpty() ? nome : row);
                hackathonIds.add(id);
            }

            if (cbHackathonId.getItemCount() == 0) {
                cbHackathonId.addItem("Nessun hackathon trovato");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento degli hackathon: " + ex.getMessage());
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
