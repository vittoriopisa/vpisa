package model;

import model.exceptions.InvalidDataException;
import model.utils.ValidationUtils;


/**
 * Rappresenta un aggiornamento realizzato da un {@link Team} su un {@link Documento}.
 * Un aggiornamento contiene il contenuto testuale e mantiene relazioni
 * bidirezionali con il team e il documento a cui appartiene.
 */
public class Aggiornamento {

    private Team team;
    private Documento documento;
    private String contenuto;



    /**
     * Crea un nuovo {@code Aggiornamento}.
     *
     * @param team il team che ha creato l'aggiornamento )
     * @param documento il documento a cui si riferisce l'aggiornamento (può essere )
     * @param contenuto il contenuto testuale dell'aggiornamento (non può essere vuoto o )
     * @throws InvalidDataException se uno dei dati obbligaroei non è valido
     */
    public Aggiornamento(Team team, Documento documento, String contenuto) throws InvalidDataException {

        ValidationUtils.validateNotEmpty(contenuto, "Contenuto aggiornamento");
        this.contenuto = contenuto;



        ValidationUtils.validateNotNull(team, "Team");
            this.team = team;
            team.aggiungiAggiornamento(this);


        ValidationUtils.validateNotNull(documento, "Documento");
            this.documento = documento;
            documento.aggiungiAggiornamento(this);

    }


    /**
     * Restituisce il {@link Team} autore dell'aggiornamento.
     *
     * @return il team
     */
    public Team getTeam() { return team; }
    /**
     * Restituisce il {@link Documento} collegeto all'aggiornamento.
     *
     * @return il documento
     */
    public Documento getDocumento() { return documento; }
    /**
     * Restituisce il contenuto testuale dell'aggiornamento.
     *
     * @return il contenuto non nullo e non vuoto
     */
    public String getContenuto() { return contenuto; }





    /**
     * Imposta il team autore dell'aggiornamento e aggiorna la relazione bidirezionale.
     *
     * @param team il team da associare
     *@throws InvalidDataException se il team è nullo o invalido
     */
    public void setTeam(Team team) throws InvalidDataException {
        ValidationUtils.validateNotNull(team, "Team");
        this.team = team;
            team.aggiungiAggiornamento(this);

    }
    /**
     * Imposta il documento associato all'aggiornamento e aggiorna la relazione bidirezionale.
     *
     * @param documento il documento da associare
     *@throws InvalidDataException se il documento è nullo o invalido
     */
    public void setDocumento(Documento documento) throws InvalidDataException {
        ValidationUtils.validateNotNull(documento, "Documento");
        this.documento = documento;

            documento.aggiungiAggiornamento(this);

    }

    /**
     * Modifica il contenuto testuale dell'aggiornamento.
     *
     * @param contenuto nuovo contenuto
     *@throws InvalidDataException se il contenuto è nullo o vuoto
     */
    public void setContenuto(String contenuto) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(contenuto, "Contenuto aggiornamento");

        this.contenuto = contenuto;
    }

    /**
     * Restituisce una rappresentazione testuale dell'aggiornamento,
     * contenente il nome del team e il titolo del documento associato.
     *
     * @return stringa descrittiva dell'aggiornamento
     */
    @Override
    public String toString() {
        return String.format("Aggiornamento{team='%s', documento='%s'}",
                team != null ? team.getNome() : "N/A",
                documento != null ? documento.getTitolo() : "N/A");
    }
}

