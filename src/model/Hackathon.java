package model;

import model.exceptions.InvalidDataException;
import model.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;




/**
 * Rappresenta un Hackathon, ovvero un evento competitivo dove team di sviluppatori
 * lavorano a soluzioni progettuali in un tempo limitato.
 * <p>
 * Ogni hackathon è caratterizzato da:
 * <ul>
 *   <li>Un nome, una descrizione e un luogo</li>
 *   <li>Un intervallo temporale definito da {@link #dataInizio} e {@link #dataFine}</li>
 *   <li>Un {@link Organizzatore} responsabile</li>
 *   <li>Una lista di {@link Team} partecipanti</li>
 *   <li>Una lista di {@link Giudice} assegnati</li>
 * </ul>
 */
public class Hackathon {




    private String nome;
    private String descrizione;
    private String luogo;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    //private boolean statoRegistrazioni;
    private Organizzatore organizzatore;

    private final List<Team> teams= new ArrayList<>();
    private final List<Giudice> giudici= new ArrayList<>();
    /**
     * Crea un nuovo hackathon con tutte le informazioni principali.
     *
     * @param nome         nome dell’hackathon
     * @param descrizione  descrizione dell’evento
     * @param luogo        luogo in cui si svolge l’hackathon
     * @param dataInizio   data di inizio
     * @param dataFine     data di fine
     * @param organizzatore organizzatore dell’hackathon
     * @throws InvalidDataException se uno dei dati obbligatori non è valido
     */
    public Hackathon(String nome, String descrizione, String luogo,
                     LocalDate dataInizio, LocalDate dataFine,
                     Organizzatore organizzatore) throws InvalidDataException {

        setNome(nome);
        setDescrizione(descrizione);
        setLuogo(luogo);
        setDate(dataInizio, dataFine);

        ValidationUtils.validateNotNull(organizzatore, "Organizzatore");
        this.organizzatore = organizzatore;


    }
    /**
     * Crea un hackathon con solo nome e organizzatore.
     *
     * @param nome         nome dell’hackathon
     * @param organizzatore organizzatore dell’hackathon
     * @throws InvalidDataException se uno dei dati obbligatori non è valido
     */
    public Hackathon(String nome,
                     Organizzatore organizzatore) throws InvalidDataException {
        setNome(nome);
        ValidationUtils.validateNotNull(organizzatore, "Organizzatore");
        this.organizzatore=organizzatore;
    }
    /**
     * Crea un hackathon con solo il nome.
     *
     * @param nome nome dell’hackathon
     * @throws InvalidDataException se il nome dell'hackathon è nullo o  non è valido
     */
    public Hackathon(String nome) throws InvalidDataException {
        setNome(nome);

    }


  /*  /**
     * Crea un hackathon senza organizzatore assegnato.
     *
     * @param nome        nome dell’hackathon
     * @param descrizione descrizione dell’hackathon
     * @param luogo       luogo in cui si svolge
     * @param dataInizio  data di inizio
     * @param dataFine    data di fine
     * @throws InvalidDataException se i dati non sono validi
     */
    /*public Hackathon(String nome, String descrizione, String luogo,
                     LocalDate dataInizio, LocalDate dataFine) throws InvalidDataException {
        this(nome, descrizione, luogo, dataInizio, dataFine, null); // imposta l'Organizzatore su null
    }*/


    /**
     * Imposta le date di inizio e fine dell’hackathon, verificandone la coerenza.
     *
     * @param dataInizio data di inizio
     * @param dataFine   data di fine
     * @throws InvalidDataException se {@code dataFine} è precedente a {@code dataInizio}
     */
    private void setDate(LocalDate dataInizio, LocalDate dataFine) throws InvalidDataException {
        ValidationUtils.validateNotNull(dataInizio, "Data inizio");
        ValidationUtils.validateNotNull(dataFine, "Data fine");

        if (dataFine.isBefore(dataInizio)) {
            throw new InvalidDataException("Data fine non può essere precedente alla data inizio");
        }

        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }
    /**
     * Aggiunge un team all’hackathon se non già presente.
     *
     * @param team team da aggiungere
     * @return {@code true} se il team è stato aggiunto, {@code false} altrimenti
     */
    public boolean aggiungiTeam(Team team) {
        if (team != null && !teams.contains(team)) {
            teams.add(team);
            team.setHackathonInternal(this);
            return true;
        }
        return false;
    }



    /**
     * Verifica se l’hackathon è terminato.
     *
     * @return {@code true} se la data odierna è uguale o successiva alla {@code dataFine}
     */
    public boolean isTerminato() {
        return LocalDate.now().isAfter(dataFine) || LocalDate.now().isEqual(dataFine);
    }


    /**
     * Restituisce il nome dell'hackathon.
     *
     * @return nome
     */
    public String getNome() { return nome; }
    /**
     * Restituisce la descrizione dell'hackathon.
     *
     * @return descrizione
     */
    public String getDescrizione() { return descrizione; }
    /**
     * Restituisce la data di inizio dell'hackathon.
     *
     * @return data inizio
     */
    public LocalDate getDataInizio() { return dataInizio; }

    /**
     * Restituisce la data di fine dell'hackathon.
     *
     * @return data fine
     */
    public LocalDate getDataFine() { return dataFine; }

    /**
     * Restituisce l'organizzatore dell'hackathon.
     *
     * @return organizzatore
     */
    public Organizzatore getOrganizzatore() { return organizzatore; }
    /**
     * Restituisce il luogo che ospita l'hackathon.
     *
     * @return luogo
     */
    public String getLuogo() {
        return luogo;
    }

    /**
     * Restituisce lo stato delle registrazioni(aperte o chiuse).
     *
     * @return stato registrazioni
     */
    public boolean isStatoRegistrazioni() {
        if (dataInizio == null) {
            return false;
        }
        LocalDate oggi = LocalDate.now();
        return oggi.isBefore(dataInizio.minusDays(2));
    }
    /**
     * Restituisce la lista dei teams iscritti.
     *
     * @return lista teams
     */
    public List<Team> getTeams() {
        return teams;
    }
    /**
     * Restituisce la lista dei giudici candidati all'hackathon.
     *
     * @return lista giudici
     */
    public List<Giudice> getGiudici() {
        return giudici;
    }



    /**
     * Imposta il nome dell’hackathon.
     *
     * @param nome nome dell’hackathon
     * @throws InvalidDataException se il nome è vuoto o nullo
     */
    public void setNome(String nome) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(nome, "Nome hackathon");
        this.nome = nome.trim();
    }
    /**
     * Imposta la descrizione dell’hackathon.
     *
     * @param descrizione descrizione (opzionale, se null verrà sostituita con stringa vuota)
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione != null ? descrizione.trim() : "";
    }
    /**
     * Imposta il luogo dell’hackathon.
     *
     * @param luogo luogo dell’evento
     * @throws InvalidDataException se il luogo è nullo o vuoto
     */
    public void setLuogo(String luogo) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(luogo, "Luogo");
        this.luogo = luogo.trim();
    }



    /**
     * Imposta l’organizzatore dell’hackathon.
     *
     * @param organizzatore organizzatore
     * @throws InvalidDataException se l'organizzatore è nullo o invalido
     */
    public void setOrganizzatore(Organizzatore organizzatore) throws InvalidDataException {
        ValidationUtils.validateNotNull(organizzatore, "Organizzatore");
        this.organizzatore = organizzatore;
    }






    /**
     * Restituisce una rappresentazione testuale dell’hackathon,
     * includendo nome, descrizione, luogo, date e organizzatore.
     *
     * @return stringa con dettagli dell’hackathon
     */
    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s,%s",
                nome, descrizione, luogo, dataInizio, dataFine,
                organizzatore.getNome(),organizzatore.getCognome());
    }
}
