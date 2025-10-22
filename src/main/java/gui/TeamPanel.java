package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Pannello Swing dedicato alla gestione dei team negli hackathon.
 * <p>
 * Consente agli organizzatori di:
 * <ul>
 *     <li>Creare nuovi team</li>
 *     <li>Eliminare team esistenti</li>
 *     <li>Visualizzare tutti i team di un hackathon</li>
 *     <li>Visualizzare team non ancora pieni</li>
 *     <li>Visualizzare concorrenti e valutazioni associate a ciascun team</li>
 * </ul>
 * L'accesso e le operazioni sono limitati agli organizzatori dell'hackathon selezionato.
 */
public class TeamPanel extends JPanel {
    private final Controller controller;
    private final int loggedOrganizzatoreId;
    private final List<Integer> teamIds = new ArrayList<>();


    private final JTextField tfNome;

    private final JComboBox<String> cbHackathon;

    private final JComboBox<String> cbTeam;
    private final JTextArea taRisultati;

    /**
     * Crea un pannello per la gestione dei team.
     *
     * @param controller            riferimento al controller per la logica applicativa
     * @param loggedOrganizzatoreId ID dell’organizzatore loggato
     */
    public TeamPanel(Controller controller,int loggedOrganizzatoreId) {
        this.controller = controller;
        this.loggedOrganizzatoreId = loggedOrganizzatoreId;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));


        formPanel.add(new JLabel("Nome Team:"));
        tfNome = new JTextField(20);
        formPanel.add(tfNome);



        formPanel.add(new JLabel("Seleziona Hackathon:"));
        cbHackathon = new JComboBox<>();
        formPanel.add(cbHackathon);




        formPanel.add(new JLabel("Team (per eliminare):"));
        cbTeam = new JComboBox<>();
        formPanel.add(cbTeam);

        add(formPanel, BorderLayout.NORTH);

        loadHackathonOptions();


        cbHackathon.addActionListener(e -> {
            String selected = (String) cbHackathon.getSelectedItem();
            if (selected != null) {
                int hackathonId = Integer.parseInt(selected.split(" - ")[0]); // assumo formato "id - nome"
                loadTeamOptions(hackathonId);
            }
        });

        taRisultati = new JTextArea(10, 30);
        taRisultati.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taRisultati);
        add(scrollPane,BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));


        JButton btnSave = new JButton("Aggiungi Team");

        JButton btnDelete = new JButton("Elimina Team");
        JButton btnShowTeams = new JButton("Visualizza Team per Hackathon");
        JButton btnShowAvailableTeams = new JButton("Visualizza Team Non Pieni");
        JButton btnShowConcorrenti = new JButton("Mostra Concorrenti per Team");
        JButton btnShowValutazioni = new JButton("Mostra Valutazioni Team");







        btnSave.addActionListener(_ -> saveTeam());

        btnDelete.addActionListener(_ -> deleteTeam());
        btnShowTeams.addActionListener(_ -> showTeamsByHackathonId());
        btnShowAvailableTeams.addActionListener(_-> showTeamsNotFull());
        btnShowConcorrenti.addActionListener(_ -> showConcorrentiByTeam());
        btnShowValutazioni.addActionListener(_ -> showValutazioniByTeam());


        buttonPanel.add(btnSave);

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnShowTeams);
        buttonPanel.add(btnShowAvailableTeams);
        buttonPanel.add(btnShowConcorrenti);
        buttonPanel.add(btnShowValutazioni);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void saveTeam() {
        try {
            String nome = tfNome.getText();
            //int hackathonId = Integer.parseInt(tfHackathonId.getText());
            String hackathonSelected = (String) cbHackathon.getSelectedItem();
            int hackathonId = Integer.parseInt(hackathonSelected.split(" - ")[0]);


            int organizzatoreIdDb = controller.getOrganizzatoreIdByHackathon(hackathonId);


            if (organizzatoreIdDb == -1) {
                JOptionPane.showMessageDialog(this, "Hackathon non trovato.");
                return;
            }

            if (organizzatoreIdDb != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi aggiungere un team a un hackathon che non è stato creato da te.");
                return;
            }

            String result = controller.saveTeam(nome, hackathonId);
            showResults(result);
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        }
    }



    private void deleteTeam() {
        try {

            String teamSelected = (String) cbTeam.getSelectedItem();
            if (teamSelected == null || teamSelected.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona un team da eliminare.");
                return;
            }
            int teamId = Integer.parseInt(teamSelected.split(" - ")[0]);

            String hackathonSelected = (String) cbHackathon.getSelectedItem();
            if (hackathonSelected == null || hackathonSelected.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleziona un hackathon.");
                return;
            }
            int hackathonId = Integer.parseInt(hackathonSelected.split(" - ")[0]);


            int organizzatoreIdDb = controller.getOrganizzatoreIdByHackathon(hackathonId);

            if (organizzatoreIdDb == -1) {
                JOptionPane.showMessageDialog(this, "Hackathon non trovato.");
                return;
            }

            if (organizzatoreIdDb != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        " Non puoi eliminare team da un hackathon che non è stato creato da te.");
                return;
            }


            int hackathonIdDb = controller.getHackathonIdByTeam(teamId);

            if (hackathonIdDb == -1) {
                JOptionPane.showMessageDialog(this, "Team non trovato.");
                return;
            }

            if (hackathonIdDb != hackathonId) {
                JOptionPane.showMessageDialog(this,
                        " Il team non appartiene all'hackathon con ID " + hackathonId);
                return;
            }


            String result = controller.deleteTeam(teamId);
            showResults(result);
            clearFields();

            loadTeamOptions(hackathonId);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        }
    }



    private void showTeamsByHackathonId() {
        try {

            String hackathonSelected = (String) cbHackathon.getSelectedItem();
            int hackathonId = Integer.parseInt(hackathonSelected.split(" - ")[0]);


            int ownerId = controller.getOrganizzatoreIdByHackathon(hackathonId);
            if (ownerId != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi visualizzare i team: l'hackathon non appartiene al tuo account.");
                return;
            }

            taRisultati.setText(""); // pulisco l'area
            var teams = controller.getTeamsByHackathonId(hackathonId);

            if (teams.isEmpty()) {
                showResults("Nessun team trovato per l'hackathon con ID " + hackathonId);
            } else {
                for (String team : teams) {
                    showResults(team);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }





    private void showTeamsNotFull() {
        try {
            String selectedHackathon = (String) cbHackathon.getSelectedItem();

            if (selectedHackathon == null || selectedHackathon.trim().isEmpty() ||
                    selectedHackathon.toLowerCase().contains("nessun hackathon")) {
                JOptionPane.showMessageDialog(this, "Seleziona un hackathon valido dalla lista.");
                return;
            }

            // --- Estrai un nome "pulito" dall'elemento selezionato ---
            String nomeEstratto = extractHackathonName(selectedHackathon);
            if (nomeEstratto == null || nomeEstratto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Impossibile determinare il nome dell'hackathon selezionato.");
                return;
            }

            // Recupera l'id usando il nome e l'organizzatore (metodo già presente in Controller)
            int hackathonId = controller.getHackathonIdByNameAndOrganizzatore(nomeEstratto, loggedOrganizzatoreId);
            if (hackathonId == -1) {
                JOptionPane.showMessageDialog(this, "Impossibile trovare l'hackathon selezionato.");
                return;
            }

            taRisultati.setText("");

            // Ottieni i team non pieni dal controller
            var teamsNotFull = controller.getTeamsNotFull(hackathonId);

            if (teamsNotFull == null || teamsNotFull.isEmpty()) {
                // Se non ci sono team non pieni, controlliamo se esistono team in generale
                var allTeams = controller.getTeamsByHackathonId(hackathonId);
                if (allTeams == null || allTeams.isEmpty()) {
                    showResults("Non sono presenti team per questo hackathon.");
                } else {
                    showResults("Tutti i team per l'hackathon con ID " + hackathonId + " sono già pieni.");
                }
                return;
            }

            showResults("Team non pieni per l'hackathon con ID " + hackathonId + ":");
            for (String team : teamsNotFull) {
                showResults(team);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Errore nei valori inseriti.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    /**
     * Helper che prova ad estrarre il nome "pulito" dell'hackathon dalla stringa mostrata in combo.
     * Gestisce formati come:
     *  - "42 - Hackathon AI"
     *  - "ID: 42, Nome: Hackathon AI, ..."
     *  - "Hackathon AI;descrizione;..."
     *  - "Hackathon AI"
     */
    private String extractHackathonName(String raw) {
        if (raw == null) return null;
        String s = raw.trim();


        if (s.contains(" - ")) {
            String[] parts = s.split(" - ", 2);
            if (parts.length > 1) return parts[1].trim();
        }


        if (s.contains("ID:") || s.contains("Nome:")) {
            String[] parts = s.split(",\\s*");
            for (String p : parts) {
                p = p.trim();
                if (p.startsWith("Nome:") || p.startsWith("Nome ")) {
                    int idx = p.indexOf(':');
                    if (idx >= 0 && idx + 1 < p.length()) return p.substring(idx + 1).trim();
                }
                // alcuni formati usano "Nome <val>" senza ':'
                if (p.toLowerCase().startsWith("nome") && p.contains(":") == false) {
                    String[] tokens = p.split("\\s+", 2);
                    if (tokens.length > 1) return tokens[1].trim();
                }
            }
        }

        // formato separato da ';' es. "Nome;Descrizione;..."
        if (s.contains(";")) {
            String[] parts = s.split(";", 2);
            if (parts.length > 0) return parts[0].trim();
        }

        // fallback: può essere già solo il nome
        return s;
    }





    private void showConcorrentiByTeam() {
        try {

            String hackathonSelected = (String) cbHackathon.getSelectedItem();
            String teamSelected = (String) cbTeam.getSelectedItem();
            if (hackathonSelected == null || teamSelected == null) {
                JOptionPane.showMessageDialog(this, "Seleziona sia un hackathon che un team.");
                return;
            }
            int hackathonId = Integer.parseInt(hackathonSelected.split(" - ")[0]);
            //int teamId = Integer.parseInt(tfTeamId.getText());

            int teamId = Integer.parseInt(teamSelected.split(" - ")[0]);


            int organizzatoreIdDb = controller.getOrganizzatoreIdByHackathon(hackathonId);

            if (organizzatoreIdDb == -1) {
                JOptionPane.showMessageDialog(this, "Hackathon non trovato.");
                return;
            }

            if (organizzatoreIdDb != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi visualizzare i concorrenti di un hackathon che non è stato creato da te.");
                return;
            }


            int hackathonIdDb = controller.getHackathonIdByTeam(teamId);

            if (hackathonIdDb == -1) {
                JOptionPane.showMessageDialog(this, "Team non trovato.");
                return;
            }

            if (hackathonIdDb != hackathonId) {
                JOptionPane.showMessageDialog(this,
                        "Il team con ID " + teamId + " non appartiene all'hackathon con ID " + hackathonId);
                return;
            }

            // Recupero concorrenti dal controller
            taRisultati.setText("");
            var concorrenti = controller.getAllConcorrentiForTeam(teamId, hackathonId);

            if (concorrenti.isEmpty()) {
                showResults("Nessun concorrente trovato per il team con ID " + teamId);
            } else {
                for (String concorrente : concorrenti) {
                    showResults(concorrente);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Inserisci valori numerici validi per Hackathon ID e Team ID.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }

    private void showValutazioniByTeam() {
        try {

            String hackathonSelected = (String) cbHackathon.getSelectedItem();
            String teamSelected = (String) cbTeam.getSelectedItem();
            if (hackathonSelected == null || teamSelected == null) {
                JOptionPane.showMessageDialog(this, "Seleziona sia un hackathon che un team.");
                return;
            }
            int hackathonId = Integer.parseInt(hackathonSelected.split(" - ")[0]);


            int teamId = Integer.parseInt(teamSelected.split(" - ")[0]);


            int ownerId = controller.getOrganizzatoreIdByHackathon(hackathonId);
            if (ownerId != loggedOrganizzatoreId) {
                JOptionPane.showMessageDialog(this,
                        "Non puoi visualizzare valutazioni di team in un hackathon che non è stato creato da te.");
                return;
            }


            int hackathonIdDb = controller.getHackathonIdByTeam(teamId);
            if (hackathonIdDb == -1) {
                JOptionPane.showMessageDialog(this, "Team non trovato.");
                return;
            }
            if (hackathonIdDb != hackathonId) {
                JOptionPane.showMessageDialog(this,
                        "Il team con ID " + teamId + " non appartiene all'hackathon con ID " + hackathonId);
                return;
            }


            taRisultati.setText(""); // pulisco area
            var valutazioni = controller.getValutazioniByTeam(teamId);

            if (valutazioni.isEmpty()) {
                showResults("Nessuna valutazione trovata per il team con ID: " + teamId);
            } else {
                valutazioni.forEach(this::showResults);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Inserisci valori numerici validi per Hackathon ID e Team ID.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
        }
    }


    private void loadHackathonOptions() {
        cbHackathon.removeAllItems();
        try {
            var hackathonList = controller.getHackathonByOrganizzatoreId(loggedOrganizzatoreId);
            if (hackathonList == null || hackathonList.isEmpty()) {
                cbHackathon.addItem("Nessun hackathon trovato");
                return;
            }
            for (String h : hackathonList) {
                cbHackathon.addItem(h); // formato: "id - nome"
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento degli hackathon: " + e.getMessage());
        }
    }




    private void loadTeamOptions(int hackathonId) {
        cbTeam.removeAllItems();
        try {
            var teams = controller.getTeamsByHackathonId(hackathonId);
            for (String t : teams) {
                cbTeam.addItem(t); // formato: "id - nome"
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dei team: " + e.getMessage());
        }
    }





    private void clearFields() {

        tfNome.setText("");
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
