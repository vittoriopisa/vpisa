package model.exceptions;
/**
 * Eccezione lanciata quando si tenta di aggiungere un concorrente a un team
 * che ha già raggiunto il numero massimo consentito di membri.
 */
public class TeamFullException extends Exception {
    /**
     * Crea una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell’eccezione
     */
    public TeamFullException(String message) {
        super(message);
    }
}
