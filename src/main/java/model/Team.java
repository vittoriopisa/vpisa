package model;

import model.exceptions.InvalidDataException;
import model.exceptions.TeamFullException;
import model.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Rappresenta un team partecipante a un {@link Hackathon}.
 * <p>
 * Un team è identificato da un ID, un nome e può essere associato a un {@link Problema}
 * da risolvere. Contiene inoltre un insieme di {@link Concorrente concorrenti},
 * {@link Documento documenti}, {@link Aggiornamento aggiornamenti} e {@link Valutazione valutazioni}.
 * </p>
 * <p>
 * Un team ha un numero massimo di partecipanti definito dalla costante {@link #MAX_TEAM_SIZE}.
 * </p>
 */
public class Team {
    private static final int MAX_TEAM_SIZE = 6;

    private final int id;
    private String nome;
    private Hackathon hackathon;
    private Problema problema;

    private final List<Concorrente> concorrenti = new ArrayList<>();
    private final List<Documento> documenti = new ArrayList<>();
    private final List<Aggiornamento> aggiornamenti = new ArrayList<>();
    private final List<Valutazione> valutazioni = new ArrayList<>();

    /**
     * Costruisce un nuovo team associato a un {@link Hackathon}.
     *@param id        identificativo univoco del team

     * @param nome      nome del team
     * @param hackathon hackathon di appartenenza
     * @throws InvalidDataException se uno dei dati obbligatori non è valido
     */
    public Team(int id, String nome, Hackathon hackathon) throws InvalidDataException {
        this.id = id;
        setNome(nome);
        ValidationUtils.validateNotNull(hackathon, "Hackathon");
        this.hackathon = hackathon;
    }

    /**
     * Costruisce un team con solo ID e nome (senza hackathon).
     *@param id        identificativo univoco del team

     * @param nome nome del team
     *@throws InvalidDataException se il nome è nullo o vuoto
     */
    public Team(int id, String nome) throws InvalidDataException {
        this.id = id;
        setNome(nome);
    }

    /**
     * Aggiunge un concorrente al team, rispettando il limite massimo di partecipanti.
     *
     * @param concorrente concorrente da aggiungere
     * @throws TeamFullException     se il team ha già raggiunto {@link #MAX_TEAM_SIZE}
     * @throws InvalidDataException  se il concorrente è nullo
     */
    public void aggiungiConcorrente(Concorrente concorrente) throws TeamFullException, InvalidDataException {
        ValidationUtils.validateNotNull(concorrente, "Concorrente");

        if (concorrenti.size() >= MAX_TEAM_SIZE) {
            throw new TeamFullException("Il team ha raggiunto la capacità massima di " + MAX_TEAM_SIZE + " concorrenti");
        }

        if (!concorrenti.contains(concorrente)) {
            concorrenti.add(concorrente);
            concorrente.setTeamInternal(this);
        }
    }

    /**
     * Rimuove un concorrente dal team.
     *
     * @param concorrente concorrente da rimuovere
     * @return {@code true} se la rimozione è avvenuta con successo, {@code false} altrimenti
     */
    public boolean rimuoviConcorrente(Concorrente concorrente) {
        if (concorrenti.remove(concorrente)) {
            concorrente.setTeamInternal(null);
            return true;
        }
        return false;
    }

    /**
     * Assegna un problema al team, assicurando la bidirezionalità del legame.
     *
     * @param problema problema da assegnare
     * @throws InvalidDataException se il problema è nullo o se il team ne ha già uno
     */
    public void assegnaProblema(Problema problema) throws InvalidDataException {
        ValidationUtils.validateNotNull(problema, "Problema");
        if (this.problema != null) {
            throw new InvalidDataException("Il team " + nome + " ha già un problema assegnato");
        }
        this.problema = problema;
        problema.setTeam(this);
    }

    /**
     * Aggiunge un aggiornamento al team.
     *
     * @param aggiornamento aggiornamento da aggiungere
     */
    public void aggiungiAggiornamento(Aggiornamento aggiornamento) {
        if (aggiornamento != null && !aggiornamenti.contains(aggiornamento)) {
            aggiornamenti.add(aggiornamento);
        }
    }

    /**
     * Aggiunge una valutazione al team.
     *
     * @param valutazione valutazione da aggiungere
     */
    public void aggiungiValutazione(Valutazione valutazione) {
        if (valutazione != null && !valutazioni.contains(valutazione)) {
            valutazioni.add(valutazione);
        }
    }

    /**
     * Aggiunge un documento al team.
     *
     * @param documento documento da aggiungere
     */
    public void aggiungiDocumento(Documento documento) {
        if (documento != null && !documenti.contains(documento)) {
            documenti.add(documento);
        }
    }

    /**
     * Imposta internamente l'hackathon di appartenenza (usato da {@link Hackathon#aggiungiTeam(Team)}).
     *
     * @param hackathon hackathon associato
     */
    public void setHackathonInternal(Hackathon hackathon) {
        this.hackathon = hackathon;
    }


    /**
     * Restituisce l'identificativo univoco del team.
     *
     * @return id del team
     */
    public int getId() { return id; }

    /**
     * Restituisce il nome del team.
     *
     * @return nome del team
     */
    public String getNome() { return nome; }

    /**
     * Restituisce l'hackathon di cui fa parte il team.
     *
     * @return hackathon del team
     */
    public Hackathon getHackathon() { return hackathon; }

    /**
     * Restituisce il problema assegnato al team.
     *
     * @return problema assegnato al team o {@code null} se non assegnat
     */

    public Problema getProblema() { return problema; }

    /**
     * Restituisce la lista degli aggiornamenti rilasciati dal team sui suoi documenti.
     *
     * @return lista aggiornamenti
     */
    public List<Aggiornamento> getAggiornamenti() {
        return Collections.unmodifiableList(aggiornamenti);
    }

    /**
     * Restituisce la lista delle valutazioni ottenute dai team nel corso dell'hackathon.
     *
     * @return lista valutazioni
     */
    public List<Valutazione> getValutazioni() {
        return Collections.unmodifiableList(valutazioni);
    }

    /**
     * Restituisce la lista dei concorrenti che compongono il team.
     *
     * @return lista concorrenti
     */
    public List<Concorrente> getConcorrenti() {
        return concorrenti;
    }

    /**
     * Restituisce la lista dei documenti stilati dai team.
     *
     * @return lista documenti
     */
    public List<Documento> getDocumenti() {
        return documenti;
    }



    /**
     * Imposta il nome del team.
     *
     * @param nome nuovo nome del team
     * @throws InvalidDataException se il nome è nullo o vuoto
     */
    public void setNome(String nome) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(nome, "Nome team");
        this.nome = nome.trim();
    }

    /**
     * Imposta l'hackathon a cui il team partecipa.
     *
     * @param hackathon hackathon associato
     */
    public void setHackathon(Hackathon hackathon) throws InvalidDataException {
        ValidationUtils.validateNotNull(hackathon, "Hackathon");
        this.hackathon = hackathon;
    }

    /**
     * Imposta il problema assegnato al team .
     *
     * @param problema problema da associare
     */
    public void setProblema(Problema problema) {
        this.problema = problema;
    }

    /**
     * Restituisce una rappresentazione testuale del team,
     * con informazioni su nome, numero concorrenti, documenti e valutazioni.
     *
     * @return stringa rappresentativa del team
     */
    @Override
    public String toString() {
        return String.format("Team{nome='%s', concorrenti=%d/%d, documenti=%d, valutazioni=%d}",
                nome, concorrenti.size(), MAX_TEAM_SIZE, documenti.size(), valutazioni.size());
    }
}
