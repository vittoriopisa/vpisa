
package security;

/**
 * Classe di utilità per testare e aggiornare password di utenti nel database.
 * <p>
 * Questa classe fornisce un {@code main} che:
 * <ul>
 *   <li>Hasha un insieme di password di esempio utilizzando {@link PasswordSecurity#hashPassword(String)}.</li>
 *   <li>Aggiorna nel database la password in chiaro associata a determinati ID utente
 *       tramite {@link PasswordUpdater#updatePassword(int, String)}.</li>
 *   <li>Stampa su console gli ID e le password hashate corrispondenti.</li>
 * </ul>
 */
public class PasswordHasher {
    /**
     * Punto di ingresso del programma. Cicla su un insieme di password ed ID utente di esempio,
     * calcola l'hash delle password, aggiorna le password nel database e stampa a console
     * i risultati.
     *
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Esempi di password da hashare
        String[] passwords = {
                "hashedpassword321",
                "hashedpassword456",
                "hashedpassword123",
                "hashedpassword678"
        };


        // Gli ID degli utenti per cui vuoi aggiornare la password (esempio)
        int[] userIds = { 70, 71, 76, 78 };

        // Ciclo su ogni password e aggiorno la password dell'utente nel database
        for (int i = 0; i < passwords.length; i++) {
            // Usa la classe PasswordSecurity per ottenere l'hash
            String hashed = PasswordSecurity.hashPassword(passwords[i]);

            // Aggiorno la password nel database per ciascun utente
            PasswordUpdater.updatePassword(userIds[i], passwords[i]); // Passa la password per ogni ID

            // Stampa l'ID dell'utente e l'hash della password
            System.out.println("Utente con ID " + userIds[i] + " ha aggiornato la password a: " + hashed);
        }
    }
}
