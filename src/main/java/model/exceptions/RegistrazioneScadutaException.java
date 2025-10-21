package model.exceptions;
/**
 * Eccezione lanciata quando un utente tenta di iscriversi o selezionare un hackathon
 * le cui registrazioni sono chiuse.
 */
public class RegistrazioneScadutaException extends Exception {
    /**
     * Crea una nuova eccezione con il messaggio specificato.
     *
     * @param message messaggio descrittivo dell’eccezione
     */
    public RegistrazioneScadutaException(String message) {
        super(message);
    }
}
