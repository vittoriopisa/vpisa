package model;

import model.exceptions.InvalidDataException;
import model.utils.ValidationUtils;


/**
 * Rappresenta un commento associato a un {@link Documento} e scritto da un {@link Giudice}.
 * Un commento contiene il testo e mantiene una relazione bidirezionale con il documento.
 */
public class Commento {

    private Documento documento;
    private Giudice giudice;
    private String testo;

    /**
     * Crea un nuovo {@code Commento}.
     *
     * @param documento il documento associato al commento (può essere {@code null})
     * @param giudice il giudice autore del commento (può essere {@code null})
     * @param testo il contenuto testuale del commento (non può essere vuoto o {@code null})
     * @throws InvalidDataException se uno dei dati obbligatori non è valido
     */
    public Commento(Documento documento, Giudice giudice, String testo) throws InvalidDataException {

        ValidationUtils.validateNotEmpty(testo, "Testo commento");
        this.testo = testo;



        ValidationUtils.validateNotNull(documento, "Documento");
            this.documento = documento;
            documento.aggiungiCommento(this);


        ValidationUtils.validateNotNull(giudice, "Giudice");
            this.giudice = giudice;


    }

    /**
     * Restituisce il {@link Documento} a cui è collegato il commento.
     *
     * @return il documento
     */
    public Documento getDocumento() { return documento; }
    /**
     * Restituisce il {@link Giudice} autore del commento.
     *
     * @return il giudice
     */
    public Giudice getGiudice() { return giudice; }
    /**
     * Restituisce il testo del commento.
     *
     * @return il testo, mai {@code null} o vuoto
     */
    public String getTesto() { return testo; }





    /**
     * Imposta il documento a cui associare il commento e aggiorna la relazione bidirezionale.
     *
     * @param documento il documento da associare
     * @throws InvalidDataException se il documento è nullo o invalido
     */
    public void setDocumento(Documento documento) throws InvalidDataException {
        ValidationUtils.validateNotNull(documento, "Documento");
        this.documento = documento;

            documento.aggiungiCommento(this);

    }
    /**
     * Imposta il giudice autore del commento.
     *
     * @param giudice il giudice da associare
     * @throws InvalidDataException se il giudice è nullo o invalido
     */
     public void setGiudice(Giudice giudice) throws InvalidDataException {
         ValidationUtils.validateNotNull(giudice, "Giudice");
        this.giudice = giudice;

    }

    /**
     * Restituisce il testo del commento.
     *
     * @param testo il nuovo testo (può essere vuoto, non viene validato)
     * @throws InvalidDataException se il testo è nullo o vuoto
     */
    public void setTesto(String testo) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(testo, "Testo commento");
        this.testo = testo.trim();
    }


    /**
     * Restituisce una rappresentazione testuale del commento,
     * contenente il nome del giudice e il titolo del documento.
     *
     * @return stringa descrittiva del commento
     */
    @Override
    public String toString() {
        return String.format("Commento{ giudice='%s', documento='%s'}",
                giudice != null ? giudice.getNome() + " " + giudice.getCognome() : "N/A",
                documento != null ? documento.getTitolo() : "N/A");
    }


}
