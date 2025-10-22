package model.exceptions;
/**
 * Eccezione generica che indica che i dati forniti non sono validi o non rispettano
 * le regole di validazione.
 */
public class InvalidDataException extends Exception {
    /**
     * Crea una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell’eccezione
     */
    public InvalidDataException(String message) {
        super(message);
    }
}
