package gui;

/**
 * Interfaccia funzionale che rappresenta un'azione da eseguire
 * dopo un login effettuato con successo.
 * <p>
 * Viene utilizzata da {@link LoginPanel} per passare al livello superiore
 * (es. apertura della {@link MainFrame}) comunicando i dati dell'utente autenticato.
 */
public interface LoginSuccessHandler {
    /**
     * Metodo da implementare per gestire l'evento di login avvenuto con successo.
     *
     * @param userId identificativo numerico dell'utente autenticato
     * @param tipo   tipo di utente autenticato (es. "organizzatore", "concorrente", "giudice")
     */
    void run(int userId, String tipo);
}
