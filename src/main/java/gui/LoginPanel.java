package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Pannello grafico Swing per la gestione del login degli utenti.
 * <p>
 * Questo pannello permette all'utente di:
 * <ul>
 *     <li>Inserire email e password per autenticarsi</li>
 *     <li>Effettuare l'accesso tramite il {@link Controller}</li>
 *     <li>Passare al pannello di registrazione se non è registrato</li>
 * </ul>
 * Dopo un login corretto, invoca un handler {@link LoginSuccessHandler}
 * per comunicare l'ID utente e il tipo (concorrente, giudice, organizzatore, ecc.).
 */
public class LoginPanel extends JPanel{
    private final JTextField tfEmail;
    private final JPasswordField pfPassword;
    private final JButton btnLogin, btnVaiRegistrazione;

    private final Controller controller;
    private final Runnable onSwitchToRegistrazione;
    private final LoginSuccessHandler  onLoginSuccess;

    /**
     * Costruttore del pannello di login.
     *
     * @param controller               il controller per la gestione del login
     * @param onSwitchToRegistrazione  azione da eseguire per cambiare al pannello di registrazione
     * @param onLoginSuccess           handler da eseguire in caso di login corretto
     */
    public LoginPanel(Controller controller, Runnable onSwitchToRegistrazione, LoginSuccessHandler onLoginSuccess) {
        this.controller = controller;
        this.onSwitchToRegistrazione = onSwitchToRegistrazione;
        this.onLoginSuccess = onLoginSuccess;

        setLayout(new GridLayout(3, 2, 5, 5));

        Font smallFont = new Font("SansSerif", Font.PLAIN, 12);

        add(new JLabel("E-mail:"));
        tfEmail = new JTextField();
        tfEmail.setFont(smallFont);
        tfEmail.setPreferredSize(new Dimension(120, 22));
        add(tfEmail);

        add(new JLabel("Password:"));
        pfPassword = new JPasswordField();
        pfPassword.setFont(smallFont);
        pfPassword.setPreferredSize(new Dimension(120, 22));
        add(pfPassword);

        btnLogin = new JButton("Login");
        btnLogin.setFont(smallFont);
        btnLogin.setPreferredSize(new Dimension(100, 25));

        btnVaiRegistrazione = new JButton("Vai a Registrazione");
        btnVaiRegistrazione.setFont(smallFont);
        btnVaiRegistrazione.setPreferredSize(new Dimension(140, 25));

        add(btnLogin);
        add(btnVaiRegistrazione);



        btnLogin.addActionListener(_-> {
            String email = tfEmail.getText().trim();
            String password = new String(pfPassword.getPassword());

            try {
                int userId = controller.login(email, password); // login ora restituisce l'ID
                if (userId > 0) {
                    String tipo = controller.getUserType(userId); // aggiungi un metodo in controller
                    JOptionPane.showMessageDialog(this, "Login effettuato!");
                    onLoginSuccess.run(userId, tipo);
                } else {
                    JOptionPane.showMessageDialog(this, "Credenziali errate.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errore login: " + ex.getMessage());
            }
        });

        // Listener cambio a registrazione
        btnVaiRegistrazione.addActionListener(_ -> onSwitchToRegistrazione.run());
    }
}
