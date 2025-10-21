package model;


import model.exceptions.InvalidDataException;
import model.exceptions.RegistrazioneScadutaException;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

/**
 * Rappresenta un giudice di un {@link Hackathon}.
 * <p>
 * Un giudice può:
 * <ul>
 *   <li>Assegnare un {@link Problema} a un {@link Team}</li>
 *   <li>Commentare un {@link Documento}</li>
 *   <li>Valutare un {@link Team} tramite una {@link Valutazione}</li>
 * </ul>
 * Ogni giudice mantiene lo storico delle valutazioni, commenti e problemi assegnati.
 */
public class Giudice extends Utente {
    private final List<Valutazione> valutazioni= new ArrayList<>();
    private final List<Commento> commenti= new ArrayList<>();





    private final List<Problema> problemi= new ArrayList<>();
    /**
     * Crea un nuovo giudice con tutte le informazioni principali.
     *
     * @param id                  identificativo univoco del giudice
     * @param nome                nome del giudice
     * @param cognome             cognome del giudice
     * @param email               email del giudice
     * @param plainPassword       password in chiaro (verrà gestita in sicurezza dal sistema)
     * @param dataRegistrazione   data di registrazione del giudice
     * @param hackathonDaGiudicare l’hackathon che il giudice deve valutare
     * @throws InvalidDataException se i dati forniti non sono validi
     * @throws RegistrazioneScadutaException se le registrazioni all'hackathon sono chiuse
     */
    public Giudice(int id,String nome, String cognome, String email,
                   String plainPassword, LocalDate dataRegistrazione,
                   Hackathon hackathonDaGiudicare) throws InvalidDataException, RegistrazioneScadutaException {
        super(id,nome, cognome, email, plainPassword, dataRegistrazione, hackathonDaGiudicare);

    }


    /**
     * Costruttore "leggero" con email (usa il costruttore protetto di Utente).
     * Utile per rappresentazioni in memoria (visualizzazione, parsing da DAO, ecc.).
     */
    public Giudice(int id, String nome, String cognome, String email) throws InvalidDataException, RegistrazioneScadutaException {
        super(id, nome, cognome, email);
    }


    /**
     * Assegna un problema a un team.
     * <p>
     * Se il team ha già un problema assegnato, viene generata un’eccezione.
     * </p>
     *
     * @param titolo      titolo del problema
     * @param descrizione descrizione del problema
     * @param team        team a cui assegnare il problema
     * @throws InvalidDataException se il team è nullo o ha già un problema assegnato
     */
    public void assegnaProblemaATeam(String titolo, String descrizione, Team team) throws InvalidDataException {
        if (team == null) {
            throw new InvalidDataException("Team non valido (null).");
        }
        // Controllo che il team non abbia già un problema
        if (team.getProblema() != null) {
            throw new InvalidDataException("Il team '" + team.getNome() + "' ha già un problema assegnato.");
        }

        // Crea il Problema collegandolo automaticamente al Giudice (il costruttore di Problema chiama giudice.aggiungiProblema(this))
        Problema problema = new Problema(titolo, descrizione, this);

        // Assegna il problema al team (Team.assegnaProblema effettua la validazione e imposta la relazione bidirezionale)
        team.assegnaProblema(problema);
    }
    /**
     * Restituisce la lista delle valutazioni effettuate dal giudice.
     *
     * @return lista di valutazioni
     */
    public List<Valutazione> getValutazioni() {
        return valutazioni;
    }
    /**
     * Restituisce la lista dei commenti scritti dal giudice.
     *
     * @return lista di commenti
     */
    public List<Commento> getCommenti() {
        return commenti;
    }
    /**
     * Restituisce la lista dei problemi assegnati dal giudice.
     *
     * @return lista di problemi
     */
    public List<Problema> getProblemi() {
        return problemi;
    }
    /**
     * Permette al giudice di commentare un documento.
     *
     * @param contenuto contenuto del commento
     * @param documento documento da commentare
     * @throws InvalidDataException se il documento è nullo o non valido
     */
    public void commentaDocumento(String contenuto, Documento documento) throws InvalidDataException {
        if (documento == null) {
            throw new InvalidDataException("Documento non valido (null). Non è possibile commentare.");
        }

        // Creiamo il commento associandolo al Giudice
        Commento commento = new Commento(documento,this, contenuto);

        // Aggiungiamo il commento al documento (verrà validato in Documento.aggungiCommento)
        documento.aggiungiCommento(commento);
    }
    /**
     * Permette al giudice di valutare un team.
     * <p>
     * Una valutazione può essere effettuata solo se il team ha almeno un aggiornamento
     * e non è già stata inserita dal giudice.
     * </p>
     *
     * @param nuovaValutazione valutazione da aggiungere
     * @throws InvalidDataException se la valutazione è priva di team o se il team non ha aggiornamenti
     */
        public void valutaTeam(Valutazione nuovaValutazione) throws InvalidDataException {
        if (nuovaValutazione == null) return;

        Team team = nuovaValutazione.getTeam();
        if (team == null) {
            throw new InvalidDataException("Valutazione non valida: team non associato.");
        }

        if (team.getAggiornamenti().isEmpty()) {
            throw new InvalidDataException("Il team '" + team.getNome() + "' non ha ancora aggiunto alcun aggiornamento e non può essere valutato.");
        }

        if (!valutazioni.contains(nuovaValutazione)) {
            valutazioni.add(nuovaValutazione);
            team.aggiungiValutazione(nuovaValutazione);
        }
    }







    /**
     * Restituisce una rappresentazione testuale del giudice,
     * comprensiva di numero di valutazioni, commenti e problemi assegnati.
     *
     * @return stringa leggibile con informazioni sintetiche sul giudice
     */
    @Override
    public String toString() {
        return String.format("Giudice{nome='%s %s', valutazioni=%d, commenti=%d, problemi=%d}",
                getNome(), getCognome(), valutazioni.size(), commenti.size(), problemi.size());
    }
}

