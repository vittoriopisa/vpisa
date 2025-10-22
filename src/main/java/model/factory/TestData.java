package model.factory;

import model.*;
import model.exceptions.InvalidDataException;
import model.exceptions.RegistrazioneScadutaException;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe di utilità per creare dati di test.
 *
 * <p>Fornisce metodi statici per creare hackathon, utenti e team di esempio
 * utilizzabili nei test o durante lo sviluppo.</p>
 */
public class TestData {

    /**
     * Crea un hackathon di test predefinito.
     *
     * @return un {@link Hackathon} di esempio
     * @throws InvalidDataException se i dati forniti non sono validi
     */
    public static Hackathon createTestHackathon() throws InvalidDataException {
        return new Hackathon(
                "Hackathon Napoli 2025",
                "Evento di test per sviluppatori",
                "Napoli",
                LocalDate.of(2025, 7, 12),
                LocalDate.of(2025, 7, 17),
                null);
    }

    /**
     * Crea una lista di utenti di test (concorrenti, organizzatori e giudici)
     * associati a un hackathon.
     *
     * @param hackathon l'hackathon a cui associare gli utenti
     * @return lista di {@link Utente} di esempio
     * @throws InvalidDataException se uno dei dati inseriti non è valido
     * @throws RegistrazioneScadutaException se la registrazione è scaduta
     */
    public static List<Utente> createTestUsers(Hackathon hackathon) throws InvalidDataException, RegistrazioneScadutaException {
        List<Utente> utenti = new ArrayList<>();

        // Concorrenti
        utenti.add(new Concorrente( -1,"Anna", "Bianchi", "anna.bianchi@example.com",
                "password123", LocalDate.of(2024, 4, 1), null,  hackathon));

        utenti.add(new Concorrente(-1,"Maria", "Rossi", "maria.rossi@example.com",
                "password123", LocalDate.of(2024, 4, 1), null,  hackathon));

        utenti.add(new Concorrente( -1,"Luca", "Neri", "luca.neri@example.com",
                "password123", LocalDate.of(2024, 3, 15), null,  hackathon));

        utenti.add(new Concorrente( -1,"Giulia", "Rosa", "giulia.rosa@example.com",
                "password123", LocalDate.of(2024, 3, 20), null,  hackathon));

        // Organizzatori
        utenti.add(new Organizzatore(- 1,"Marco", "Bianchi", "marco.bianchi@example.com",
                "password123", LocalDate.of(2024, 4, 1)));

        utenti.add(new Organizzatore( -1,"Luca", "Verdi", "luca.verdi@example.com",
                "password123", LocalDate.of(2024, 4, 1)));

        // Giudici
        utenti.add(new Giudice( -1,"Laura", "Verdi", "laura.verdi@example.com",
                "password123", LocalDate.of(2024, 4, 1), hackathon));

        utenti.add(new Giudice( -1,"Sara", "Blu", "sara.blu@example.com",
                "password123", LocalDate.of(2024, 4, 1), hackathon));

        return utenti;
    }

    /**
    * Crea un team di test associato a un hackathon e con un elenco di concorrenti.
     *
             * @param nome        nome del team
     * @param hackathon   hackathon a cui associare il team
     * @param concorrenti lista dei concorrenti da aggiungere al team
     * @return il {@link Team} creato
     * @throws Exception se si verifica un errore durante l'aggiunta dei concorrenti
            */
    public static Team createTestTeam(String nome, Hackathon hackathon,
                                      List<Concorrente> concorrenti) throws Exception {
        Team team = new Team(-1,nome, hackathon);

        for (Concorrente concorrente : concorrenti) {
            team.aggiungiConcorrente(concorrente);
        }

        hackathon.aggiungiTeam(team);
        return team;
    }
}
