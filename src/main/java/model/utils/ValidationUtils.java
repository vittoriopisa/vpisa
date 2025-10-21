package model.utils;

import model.exceptions.InvalidDataException;

import java.util.regex.Pattern;

/**
        * Classe di utilità per la validazione di dati nel sistema Hackathon.
 * <p>
 * Fornisce metodi statici per:
        * <ul>
 *     <li>Verificare la correttezza di un indirizzo email</li>
        *     <li>Verificare la validità di un punteggio</li>
        *     <li>Validare oggetti non nulli</li>
        *     <li>Validare stringhe non vuote</li>
        * </ul>
        */
public class ValidationUtils {
    /** Pattern regex per validare gli indirizzi email. */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    /**
     * Controlla se una stringa rappresenta un indirizzo email valido.
     *
     * @param email la stringa da validare
     * @return {@code true} se l'email è valida, {@code false} altrimenti
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    /**
     * Controlla se un punteggio è valido.
     * <p>
     * Un punteggio valido è compreso tra 0 e 10 inclusi.
     * </p>
     *
     * @param score punteggio da validare
     * @return {@code true} se il punteggio è valido, {@code false} altrimenti
     */
    public static boolean isValidScore(int score) {
        return score >= 0 && score <= 10;
    }
    /**
     * Verifica che un oggetto non sia {@code null}.
     *
     * @param obj       oggetto da controllare
     * @param fieldName nome del campo per il messaggio di errore
     * @throws InvalidDataException se l'oggetto è {@code null}
     */
    public static void validateNotNull(Object obj, String fieldName) throws InvalidDataException {
        if (obj == null) {
            throw new InvalidDataException(fieldName + " non può essere null");
        }
    }
    /**
     * Verifica che una stringa non sia {@code null} o vuota.
     *
     * @param str       stringa da controllare
     * @param fieldName nome del campo per il messaggio di errore
     * @throws InvalidDataException se la stringa è  vuota
     */
    public static void validateNotEmpty(String str, String fieldName) throws InvalidDataException {
        if (str == null || str.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " non può essere vuoto");
        }
    }
}
