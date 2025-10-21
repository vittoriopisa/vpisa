
package model;


import model.exceptions.InvalidDataException;
import model.utils.ValidationUtils;


/**
 * Rappresenta la valutazione di un {@link Team} da parte di un {@link Giudice}
 * durante un hackathon.
 * <p>
 * Una valutazione è composta da:
 * <ul>
 *   <li>un team valutato</li>
 *   <li>un giudice che assegna il punteggio</li>
 *   <li>un punteggio numerico (0–10)</li>
 *   <li>un feedback testuale facoltativo</li>
 * </ul>
 * <p>
 * La relazione è bidirezionale: quando una valutazione viene creata o aggiornata,
 * viene automaticamente aggiunta al team e al giudice corrispondenti.
 * </p>
 */
public class Valutazione {

    private Team team;
    private Giudice giudice;
    private int punteggio;
    private String feedback;

    /**
     * Costruisce una nuova valutazione assegnando team, giudice, punteggio e feedback.
     *
     * @param team      team valutato
     * @param giudice   giudice che effettua la valutazione
     * @param punteggio punteggio assegnato (deve essere compreso tra 0 e 10)
     * @param feedback  commento testuale del giudice (facoltativo)
     * @throws InvalidDataException se uno dei parametri obbligatori è nullo
     *                              o se il punteggio non è valido
     */
    public Valutazione(Team team, Giudice giudice, int punteggio, String feedback)
            throws InvalidDataException {

        setPunteggio(punteggio);
        this.feedback = feedback != null ? feedback.trim() : "";



            ValidationUtils.validateNotNull(team, "Team");
            this.team = team;
            team.aggiungiValutazione(this);


            ValidationUtils.validateNotNull(giudice, "Giudice");
            this.giudice = giudice;
            giudice.valutaTeam(this); // Può lanciare l'eccezione

    }
    /**
     * Costruisce una valutazione parziale, con solo punteggio e feedback.
     * <p>
     * Il team e il giudice dovranno essere impostati in seguito.
     * </p>
     *
     * @param punteggio punteggio assegnato (deve essere compreso tra 0 e 10)
     * @param feedback  commento testuale del giudice (facoltativo)
     * @throws InvalidDataException se il punteggio non è valido
     */
    public Valutazione(int punteggio, String feedback) throws InvalidDataException {

        setPunteggio(punteggio);
        this.feedback = feedback != null ? feedback.trim() : "";

    }


    /** @return il team associato alla valutazione */
    public Team getTeam() { return team; }
    /** @return il giudice che ha effettuato la valutazione */
    public Giudice getGiudice() { return giudice; }
    /** @return il punteggio assegnato (0-10) */
    public int getPunteggio() { return punteggio; }
    /** @return il feedback testuale associato alla valutazione (può essere vuoto) */
    public String getFeedback() {
        return feedback;
    }





    /**
     * Associa la valutazione a un team.
     * <p>
     * La relazione è bidirezionale: la valutazione viene automaticamente
     * aggiunta alla lista di valutazioni del team.
     * </p>
     *
     * @param team team da associare
     * @throws InvalidDataException se il team è {@code null}
     */
    public void setTeam(Team team) throws InvalidDataException {
        ValidationUtils.validateNotNull(team, "Team");
        this.team = team;

            team.aggiungiValutazione(this); // Aggiorna anche il team con questa valutazione

    }
    /**
     * Associa la valutazione a un giudice.
     * <p>
     * La relazione è bidirezionale: la valutazione viene automaticamente
     * registrata anche dal giudice.
     * </p>
     *
     * @param giudice giudice da associare
     * @throws InvalidDataException se il giudice è {@code null}
     */
    public void setGiudice(Giudice giudice) throws InvalidDataException{
        ValidationUtils.validateNotNull(giudice, "Giudice");
        this.giudice = giudice;

            giudice.valutaTeam(this); // Aggiorna anche il giudice con questa valutazione

    }
    /**
     * Imposta il punteggio della valutazione.
     *
     * @param punteggio valore numerico tra 0 e 10
     * @throws InvalidDataException se il punteggio non è valido
     */
    public void setPunteggio(int punteggio) throws InvalidDataException {
        if (!ValidationUtils.isValidScore(punteggio)) {
            throw new InvalidDataException("Punteggio deve essere compreso tra 0 e 10");
        }
        this.punteggio = punteggio;
    }



    /**
     * Imposta il feedback testuale associato alla valutazione.
     *
     * @param feedback nuovo commento; se {@code null}, viene salvato come stringa vuota
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    /**
     * Restituisce una rappresentazione testuale della valutazione,
     * contenente team, giudice, punteggio e feedback.
     *
     * @return stringa descrittiva della valutazione
     */
    @Override
    public String toString() {
        return String.format("Valutazione{team='%s', giudice='%s', punteggio=%d, feedback=%s}",
                team != null ? team.getNome() : "N/A",
                giudice != null ? giudice.getNome() + " " + giudice.getCognome() : "N/A",
                punteggio,feedback);
    }
}
