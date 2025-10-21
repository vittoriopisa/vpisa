package gui;

import controller.Controller;
import javax.swing.*;

/**
 * Finestra principale dell'applicazione, mostrata dopo un login avvenuto con successo.
 * <p>
 * La classe estende {@link JFrame} e utilizza un {@link JTabbedPane} per
 * organizzare le diverse funzionalità dell'applicazione, variando le schede disponibili
 * in base al tipo di utente autenticato:
 * <ul>
 *     <li><b>Organizzatore</b>: gestione hackathon e team</li>
 *     <li><b>Concorrente</b>: unione team, gestione documenti e aggiornamenti</li>
 *     <li><b>Giudice</b>: gestione problemi, valutazioni e commenti</li>
 * </ul>
 */
public class MainFrame extends JFrame {

    private final Controller controller;
    private final int loggedUserId;
    private final String loggedUserTipo;

    /**
     * Costruisce la finestra principale dell'applicazione.
     * <p>
     * In base al tipo di utente, vengono caricati i pannelli corrispondenti
     * all'interno di un {@link JTabbedPane}.
     *
     * @param controller istanza del controller che gestisce la logica applicativa
     * @param userId     identificativo dell'utente loggato
     * @param tipo       tipo di utente loggato (organizzatore, concorrente o giudice)
     */
    public MainFrame(Controller controller, int userId, String tipo) {
        this.controller = controller;
        this.loggedUserId = userId;
        this.loggedUserTipo = tipo;

        setTitle("Gestione Hackathon");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centra la finestra

        // TabbedPane per organizzare i pannelli
        JTabbedPane tabbedPane = new JTabbedPane();

        // Aggiungi ogni pannello come tab
        if (loggedUserTipo.equalsIgnoreCase("organizzatore")) {
            tabbedPane.addTab("Hackathon", new HackathonPanel(controller, loggedUserId));
            tabbedPane.addTab("Team", new TeamPanel(controller, loggedUserId));

        }




        if (loggedUserTipo.equalsIgnoreCase("concorrente")) {
            tabbedPane.addTab("Unione Team", new UnioneTeamPanel(controller, loggedUserId));
            AggiornamentoPanel aggiornamentoPanel = new AggiornamentoPanel(controller, loggedUserId);


            DocumentoPanel documentoPanel = new DocumentoPanel(controller, loggedUserId, () -> {

                SwingUtilities.invokeLater(aggiornamentoPanel::loadDocumentiForTeam);
            });


            tabbedPane.addTab("Documenti", documentoPanel);
            tabbedPane.addTab("Aggiornamenti", aggiornamentoPanel);

        }







        if (loggedUserTipo.equalsIgnoreCase("giudice")) {
            tabbedPane.addTab("Problemi", new ProblemaPanel(controller, loggedUserId, loggedUserTipo));
            tabbedPane.addTab("Valutazioni", new ValutazionePanel(controller, loggedUserId, loggedUserTipo));
            tabbedPane.addTab("Commenti", new CommentoPanel(controller, loggedUserId, loggedUserTipo));

        }




        add(tabbedPane);

        setVisible(true);
    }
}