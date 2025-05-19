package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Organizzatore extends Utente{
    private String ruolo;// Ruolo dell'organizzatore (es. coordinatore, assistente)
    private List<Giudice> giudici;

    // Costruttore
    public Organizzatore(String nome, String cognome, String email, String ruolo, LocalDate dataRegistrazione) {
        super(nome, cognome, email, dataRegistrazione); // Richiama il costruttore della classe base Utente
        this.ruolo = ruolo;
        this.giudici = new ArrayList<>();
    }

    // Metodo per aprire le registrazioni verificando la data di inizio
    public void apriRegistrazioni(Hackathon hackathon) throws RegistrazioneScadutaException {
        if (hackathon == null) return;

        LocalDate oggi = LocalDate.now();
        long giorniAllaDataInizio = ChronoUnit.DAYS.between(oggi, hackathon.getDataInizio());

        if (giorniAllaDataInizio < 2) {
            throw new RegistrazioneScadutaException("Registrazioni chiuse: meno di 2 giorni all'evento.");
        }

        hackathon.setStatoRegistrazioni(true);
        System.out.println("Registrazioni aperte per l'hackathon: " + hackathon.getNome());
    }

    // Metodo per selezionare i giudici tra quelli disponibili
    public void selezionaGiudici(List<Giudice> giudiciDisponibili, List<Giudice> giudiciDaSelezionare) {
        for (Giudice giudice : giudiciDaSelezionare) {
            if (giudiciDisponibili.contains(giudice) && !giudici.contains(giudice)) {
                giudici.add(giudice);
            }
        }
    }

    // Getter per ottenere la lista dei giudici selezionati
    public List<Giudice> getGiudiciSelezionati() {
        return giudici;
    }

    // Getter e Setter per il ruolo
    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", ruolo='" + ruolo +'}';
    }
}
