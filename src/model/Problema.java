

package model;

import model.exceptions.InvalidDataException;
import model.utils.ValidationUtils;


/**
 * Rappresenta un problema proposto da un {@link Giudice}
 * e assegnato a un {@link Team} durante un {@link Hackathon}.
 * <p>
 * Un problema è costituito da un titolo e una descrizione, e può
 * essere associato a un giudice che lo propone e a un team che deve risolverlo.
 * </p>
 */
public class Problema {

    private Team team;
    private Giudice giudice;
    private String titolo;
    private String descrizione;

    /**
     * Costruisce un nuovo problema con titolo, descrizione e giudice associato.
     *
     * @param titolo      titolo del problema
     * @param descrizione descrizione dettagliata del problema
     * @param giudice     giudice che ha proposto il problema (può essere {@code null})
     * @throws InvalidDataException se uno dei dati obbligatori non è valido
     */
    public Problema( String titolo, String descrizione, Giudice giudice) throws InvalidDataException {

        ValidationUtils.validateNotEmpty(titolo, "Titolo problema");
        this.titolo = titolo;
        ValidationUtils.validateNotEmpty(descrizione, "Descrizione problema");
        this.descrizione = descrizione;
        setTitolo(titolo);
        setDescrizione(descrizione);


        ValidationUtils.validateNotNull(giudice, "Giudice");
            this.giudice = giudice;


    }






    /**
     * Restituisce il team a cui è stato assegnato il problema.
     *
     * @return team assegnatario
     */
    public Team getTeam() { return team; }
    /**
     * Restituisce il giudice che ha proposto il problema.
     *
     * @return giudice associato
     */
    public Giudice getGiudice() { return giudice; }
    /**
     * Restituisce il titolo del problema.
     *
     * @return titolo del problema
     */
    public String getTitolo() { return titolo; }
    /**
     * Restituisce la descrizione del problema.
     *
     * @return descrizione del problema
     */
    public String getDescrizione() { return descrizione; }



    /**
     * Imposta un nuovo titolo per il problema.
     *
     * @param titolo nuovo titolo
     * @throws InvalidDataException se il titolo è nullo o vuoto
     */
    public void setTitolo(String titolo) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(titolo, "Titolo problema");
        this.titolo = titolo.trim();
    }

    /**
     * Imposta una nuova descrizione per il problema.
     *
     * @param descrizione nuova descrizione
     * @throws InvalidDataException se la descrizione è nulla o vuota
     */
    public void setDescrizione(String descrizione) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(descrizione, "Descrizione problema");
        this.descrizione = descrizione.trim();
    }

    /**
     * Imposta il giudice che ha proposto il problema.
     *
     * @param giudice giudice associato
     * @throws InvalidDataException se il giudice è nulla o invalido
     */
    public void setGiudice(Giudice giudice) throws InvalidDataException {
        ValidationUtils.validateNotNull(giudice, "Giudice");
        this.giudice = giudice;

    }

    /**
     * Imposta il team a cui il problema è stato assegnato.
     *
     * @param team team associato
     * @throws InvalidDataException se il team è nulla o invalido
     */
    public void setTeam(Team team) throws InvalidDataException {
        ValidationUtils.validateNotNull(team, "Team");
        this.team = team;
    }


    /**
     * Restituisce una rappresentazione testuale del problema,
     * comprensiva di titolo, team e giudice associato.
     *
     * @return stringa rappresentativa del problema
     */
    @Override
    public String toString() {
        return String.format("Problema{titolo='%s', team='%s', giudice='%s'}",
                 titolo, team != null ? team.getNome() : "Non assegnato",
                giudice != null ? giudice.getNome() + " " + giudice.getCognome() : "N/A");
    }
}
