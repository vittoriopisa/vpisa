package security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe di utilità per la gestione sicura delle password.
 * <p>
 * Fornisce metodi statici per:
 * <ul>
 *   <li>Generare l'hash sicuro di una password in chiaro utilizzando l'algoritmo BCrypt.</li>
 *   <li>Verificare se una password in chiaro corrisponde a un hash salvato.</li>
 * </ul>
 * <p>
 * BCrypt gestisce internamente un salt casuale e rende più sicura la protezione
 * contro attacchi a forza bruta e rainbow tables.
 */
public class PasswordSecurity {
    /**
     * Genera un hash sicuro a partire da una password in chiaro.
     *
     * @param plainPassword la password in chiaro da proteggere (non deve essere {@code null} o vuota)
     * @return una stringa contenente l'hash BCrypt della password
     * @throws IllegalArgumentException se la password è {@code null} o vuota
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password non può essere null o vuota");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    /**
     * Verifica se una password in chiaro corrisponde a un hash precedentemente generato.
     *
     * @param plainPassword la password in chiaro da controllare
     * @param hashed        l'hash salvato con cui confrontare (deve essere un hash BCrypt valido)
     * @return {@code true} se la password corrisponde all'hash, {@code false} altrimenti
     * @throws IllegalArgumentException se l'hash fornito è {@code null} o non ha un formato valido
     */
    public static boolean checkPassword(String plainPassword, String hashed) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            return false;
        }
        if (hashed == null || !hashed.startsWith("$2a$")) {
            throw new IllegalArgumentException("Hash non valido");
        }
        return BCrypt.checkpw(plainPassword, hashed);
    }

}
