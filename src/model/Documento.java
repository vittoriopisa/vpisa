

package model;

import model.exceptions.InvalidDataException;
import model.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

/**
 * Rappresenta un documento caricato da un {@link Team} durante un {@link Hackathon}.
 * <p>
 * Un documento può contenere commenti da parte dei giudici e aggiornamenti
 * effettuati dai membri del team. È caratterizzato da un titolo,
 * una descrizione, un formato, una dimensione e un tipo.
 * </p>
 */
public class Documento {
    private final int id;
    private String titolo;
    private String descrizione;
    private final LocalDate dataCreazione= LocalDate.now();
    private String formato;
    private double dimensione;
    private String tipo;
    private Team team;

    private final List<Commento> commenti= new ArrayList<>();
    private final List<Aggiornamento> aggiornamenti= new ArrayList<>();

    /**
     * Crea un nuovo documento con tutte le informazioni principali.
     *
     * @param id        identificativo univoco del team
     * @param titolo      titolo del documento
     * @param descrizione descrizione del contenuto
     * @param formato     formato del file (es. PDF, DOCX, ecc.)
     * @param dimensione  dimensione del file in MB
     * @param tipo        tipologia del documento (es. relazione, codice, presentazione)
     * @param team        il team proprietario del documento
     * @throws InvalidDataException se uno dei dati obbligatori non è valido
     */
    public Documento(int id,String titolo, String descrizione,
                     String formato, double dimensione, String tipo, Team team) throws InvalidDataException {
        this.id = id;
        setTitolo(titolo);
        this.descrizione = descrizione;

        this.formato = formato;
        setDimensione(dimensione);
        this.tipo = tipo;

        ValidationUtils.validateNotNull(team, "Team");
        this.team = team;
        team.aggiungiDocumento(this);

    }
    /**
     * Crea un documento con solo ID e titolo.
     * Utile per scenari semplificati o caricamenti parziali.
     * @param id        identificativo univoco del team

     * @param titolo titolo del documento
     *  @throws InvalidDataException se il titolo è nullo o invalido
     */
    public Documento(int id, String titolo) throws InvalidDataException {
        this.id = id;
        //this.titolo = titolo;
        setTitolo(titolo);
    }

    /**
     * Aggiunge un commento al documento, solo se esiste almeno un aggiornamento.
     *
     * @param commento il commento da aggiungere
     * @throws InvalidDataException se il documento non ha ancora aggiornamenti
     */
    public void aggiungiCommento(Commento commento) throws InvalidDataException {
        if (aggiornamenti.isEmpty()) {
            throw new InvalidDataException("Impossibile aggiungere un commento: il documento non ha ancora aggiornamenti.");
        }

        if (commento != null && !commenti.contains(commento)) {
            commenti.add(commento);
        }
    }

    /**
     * Aggiunge un aggiornamento al documento.
     *
     * @param aggiornamento l’aggiornamento da aggiungere
     */
    public void aggiungiAggiornamento(Aggiornamento aggiornamento) {
        if (aggiornamento != null && !aggiornamenti.contains(aggiornamento)) {
            aggiornamenti.add(aggiornamento);
        }
    }

    /**
     * Restituisce la data di creazione del documento.
     *
     * @return la data di creazione
     */
    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    /**
     * Restituisce l'id del documento.
     *
     * @return l'id del documento
     */
    public int getId() { return id; }
    /**
     * Restituisce il titolo del documento.
     *
     * @return il titolo del documento
     */
    public String getTitolo() { return titolo; }
    /**
     * Restituisce la descrizione del documento.
     *
     * @return descrizione
     */
    public String getDescrizione() { return descrizione; }
    /**
     * Restituisce il tipo di documento.
     *
     * @return tipo documento
     */
    public String getTipo() { return tipo; }
    /**
     * Restituisce il team che ha stilato il documento.
     *
     * @return team del documento
     */
    public Team getTeam() { return team; }
    /**
     * Restituisce la lista dei commenti associatio.
     *
     * @return lista commenti
     */
    public List<Commento> getCommenti() {
        return commenti;
    }
    /**
     * Restituisce la lista degli aggiornamenti associatio.
     *
     * @return lista aggiornamenti
     */
    public List<Aggiornamento> getAggiornamenti() {
        return aggiornamenti;
    }
    /**
     * Restituisce la lista degli aggiornamenti associatio(es. PDF, DOCX, ecc.).
     *
     * @return formato
     */

    public String getFormato() {
        return formato;
    }




    /**
     * Imposta il titolo del documento.
     *
     * @param titolo il nuovo titolo
     * @throws InvalidDataException se il titolo è vuoto o nullo
     */
    public void setTitolo(String titolo) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(titolo, "Titolo documento");
        this.titolo = titolo.trim();
    }
    /**
     * Imposta la descrizione del documento.
     *
     * @param descrizione la nuova descrizione (se {@code null} verrà impostata come stringa vuota)
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione != null ? descrizione.trim() : "";
    }



    /**
     * Imposta la dimensione del documento in MB.
     *
     * @param dimensione la nuova dimensione
     * @throws InvalidDataException se la dimensione è negativa
     */
    public void setDimensione(double dimensione) throws InvalidDataException {
        if (dimensione < 0) {
            throw new InvalidDataException("Dimensione non può essere negativa");
        }
        this.dimensione = dimensione;
    }
    /**
     * Imposta il tipo del documento.
     *
     * @param tipo la tipologia
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    /**
     * Assegna il documento a un team e aggiorna la relazione bidirezionale.
     *
     * @param team il team da assegnare
     *@throws InvalidDataException se il team è nullo o invalido
     */
    public void setTeam(Team team) throws InvalidDataException {
        ValidationUtils.validateNotNull(team, "Team");
        this.team = team;

            team.aggiungiDocumento(this);

    }
    /**
     * Imposta il formato del documento.
     *
     * @param formato il nuovo formato (es. PDF, DOCX, ecc.)
     */
    public void setFormato(String formato) {
        this.formato = formato;
    }

    /**
     * Restituisce una rappresentazione leggibile del documento,
     * comprensiva di titolo, formato, dimensione e numero di commenti/aggiornamenti.
     *
     * @return stringa leggibile con le principali informazioni del documento
     */
    @Override
    public String toString() {
        return String.format("Documento{titolo='%s', formato='%s', dimensione=%.2fMB, commenti=%d, aggiornamenti=%d}",
                titolo, formato, dimensione, commenti.size(), aggiornamenti.size());
    }




}

