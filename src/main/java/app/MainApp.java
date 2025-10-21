

package app;

import controller.Controller;
import gui.LoginPanel;
import gui.RegistrazionePanel;
import gui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principale dell'applicazione Hackathon.
 * <p>
 * Gestisce l'avvio dell'applicazione, mostrando inizialmente una finestra
 * di autenticazione che permette di effettuare il login o la registrazione.
 * Dopo il login, apre il {@link MainFrame} appropriato in base al tipo
 * di utente.
 * </p>
 * <p>
 * Utilizza Swing e {@link CardLayout} per alternare tra i pannelli di login
 * e registrazione nella stessa finestra.
 * </p>
 */
public class MainApp {
    /**
     * Punto di ingresso dell'applicazione.
     * <p>
     * Avvia l'interfaccia grafica su Event Dispatch Thread tramite
     * {@link SwingUtilities#invokeLater(Runnable)}.
     * Crea il controller, i pannelli di login e registrazione, e gestisce
     * la transizione tra di essi tramite {@link CardLayout}.
     * </p>
     *
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controller controller = new Controller();


            JFrame authFrame = new JFrame("Hackathon - Login/Registrazione");
            authFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            authFrame.setSize(400, 300);
            authFrame.setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);


            Runnable showLogin = () -> cardLayout.show(cardPanel, "login");
            Runnable showRegistrazione = () -> cardLayout.show(cardPanel, "registrazione");


            LoginPanel loginPanel = new LoginPanel(
                    controller,
                    showRegistrazione, //  uso la lambda che ho già definito
                    (userId, tipo) -> {

                        authFrame.dispose();

                        new MainFrame(controller, userId, tipo).setVisible(true);
                    }
            );
            RegistrazionePanel registrazionePanel = new RegistrazionePanel(controller, showLogin);

            cardPanel.add(loginPanel, "login");
            cardPanel.add(registrazionePanel, "registrazione");

            authFrame.add(cardPanel);
            authFrame.setVisible(true);


            cardLayout.show(cardPanel, "login");
        });
    }
}