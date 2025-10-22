package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Pannello Swing dedicato alla registrazione degli utenti.
 * <p>
 * Permette l’inserimento dei dati personali (nome, cognome, email, password),
 * la scelta del tipo utente ({@code organizzatore}, {@code giudice}, {@code concorrente}),
 * e, se necessario, l’associazione a un hackathon con registrazioni aperte.
 * </p>
 */
public class RegistrazionePanel extends JPanel {
    private final JTextField tfNome, tfCognome, tfEmail;
    private final JPasswordField pfPassword;
    private final JComboBox<String> cbTipoUtente, cbHackathon;
    private final JButton btnRegistrati, btnVaiLogin;

    private final Controller controller;
    private final Runnable onSwitchToLogin;

    /**
     * Crea un nuovo pannello di registrazione.
     *
     * @param controller       riferimento al controller per la logica applicativa
     * @param onSwitchToLogin  callback da eseguire per tornare al pannello di login
     */
    public RegistrazionePanel(Controller controller, Runnable onSwitchToLogin) {
        this.controller = controller;
        this.onSwitchToLogin = onSwitchToLogin;

        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Nome:"));
        tfNome = new JTextField();
        add(tfNome);

        add(new JLabel("Cognome:"));
        tfCognome = new JTextField();
        add(tfCognome);

        add(new JLabel("E-mail:"));
        tfEmail = new JTextField();
        add(tfEmail);

        add(new JLabel("Password:"));
        pfPassword = new JPasswordField();
        add(pfPassword);

        add(new JLabel("Tipo Utente:"));
        cbTipoUtente = new JComboBox<>(new String[]{"organizzatore", "giudice", "concorrente"});
        add(cbTipoUtente);

        add(new JLabel("Hackathon:"));
        cbHackathon = new JComboBox<>();
        try {
            for (String hackathon : controller.getHackathonsWithOpenRegistrationsCombo()) { // <- devi avere questo metodo in Controller
                cbHackathon.addItem(hackathon);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                     "Errore nel caricamento hackathon: " + e.getMessage());
        }
        add(cbHackathon);

        btnRegistrati = new JButton("Registrati");
        btnVaiLogin = new JButton("Vai al Login");

        add(btnRegistrati);
        add(btnVaiLogin);


        cbTipoUtente.addActionListener(_-> {
            String tipoSelezionato = (String) cbTipoUtente.getSelectedItem();
            if ("organizzatore".equalsIgnoreCase(tipoSelezionato)) {
                cbHackathon.setEnabled(false);
                cbHackathon.setSelectedItem(null); // opzionale: deseleziona
            } else {
                cbHackathon.setEnabled(true);
            }
        });

        // Listener registrazione
        btnRegistrati.addActionListener(_ -> {
            String nome = tfNome.getText().trim();
            String cognome = tfCognome.getText().trim();
            String email = tfEmail.getText().trim();
            String password = new String(pfPassword.getPassword());
            String tipo = (String) cbTipoUtente.getSelectedItem();
            //int hackathonId = Integer.parseInt(((String) cbHackathon.getSelectedItem()).split(" - ")[0]);
            String hackathonSelezionato = (String) cbHackathon.getSelectedItem();



            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi obbligatori!");
                return;
            }

            Integer hackathonId = null;
            if (!"organizzatore".equalsIgnoreCase(tipo)) {
                if (hackathonSelezionato == null) {
                    JOptionPane.showMessageDialog(this, "Seleziona un hackathon!");
                    return;
                }
                hackathonId = Integer.parseInt(hackathonSelezionato.split(" - ")[0]);
            }


            System.out.println("DEBUG: selected hackathon string = [" + hackathonSelezionato + "]");
            String msg = controller.saveUtente(
                    nome,
                    cognome,
                    email,
                    password,
                    LocalDate.now(),
                    tipo,
                    hackathonId,
                    null
            );
            JOptionPane.showMessageDialog(this, msg);

            if (msg.contains("successo")) {
                onSwitchToLogin.run(); // passo a login
            }
        });


        btnVaiLogin.addActionListener(_ -> onSwitchToLogin.run());
    }
}
