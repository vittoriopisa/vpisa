package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String nome;
    private int dimensione;// Nome del team
    private Hackathon hackathon; // Hackathon associato al team
    private List<Concorrente> concorrenti; // Concorrenti associati al team
    private List<Documento> documenti; // Documenti associati al team
    private List<Aggiornamento> aggiornamenti; // Lista degli aggiornamenti per il team
    private List<Valutazione> valutazioni; // Lista delle valutazioni per il team
    private Problema problema;

    // Costruttore
    public Team(String nome,int dimensione,Hackathon hackathon) {
        this.nome = nome;
        this.dimensione = dimensione;
        this.hackathon = hackathon;
        this.concorrenti = new ArrayList<>();
        this.documenti = new ArrayList<>();
        this.aggiornamenti = new ArrayList<>();
        this.valutazioni = new ArrayList<>();
        this.problema=problema;
    }

    // Metodo per assegnare un problema
    public void assegnaProblema(Problema problema) {
        if (this.problema == null) {
            this.problema = problema;
        } else {
            System.out.println("Errore: Il team " + nome + " ha già un problema assegnato!");
        }
    }

    // Getter per ottenere il problema assegnato
    public Problema getProblema() {
        return problema;
    }

    // Metodo per aggiungere un nuovo aggiornamento
    public void aggiungiNuovoAggiornamento(Aggiornamento nuovoAggiornamento) {
        if (nuovoAggiornamento != null && !this.aggiornamenti.contains(nuovoAggiornamento)) {
            this.aggiornamenti.add(nuovoAggiornamento);
        }
    }

    // Getter per la lista degli aggiornamenti
    public List<Aggiornamento> getAggiornamenti() {
        return aggiornamenti;
    }

    // Metodo per aggiungere una nuova valutazione
    public void aggiungiNuovaValutazione(Valutazione nuovaValutazione) {
        if (nuovaValutazione != null && !this.valutazioni.contains(nuovaValutazione)) {
            this.valutazioni.add(nuovaValutazione);
        }
    }

    // Getter per la lista delle valutazioni
    public List<Valutazione> getValutazioni() {
        return valutazioni;
    }

    // Metodo per aggiungere un concorrente
    public void aggiungiNuovoConcorrente(Concorrente concorrente) {
        if (concorrente != null && !concorrenti.contains(concorrente)) {
            concorrenti.add(concorrente);
            concorrente.setTeam(this);
        }
    }

    // Getter per la lista dei concorrenti
    public List<Concorrente> getConcorrenti() {
        return concorrenti;
    }

    // Metodo per aggiungere un documento
    public void aggiungiDocumento(Documento documento) {
        if (documento != null && !documenti.contains(documento)) {
            documenti.add(documento);
        }
    }

    // Getter per la lista dei documenti
    public List<Documento> getDocumenti() {
        return documenti;
    }

    // Metodo per impostare l'Hackathon
    public void setHackathon(Hackathon hackathon) {
        this.hackathon = hackathon;
    }

    // Getter per l'Hackathon
    public Hackathon getHackathon() {
        return hackathon;
    }

    // Getter e Setter per il nome del team
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Getter e Setter per la dimensione del team
    public int getDimensione() {
        return dimensione;
    }

    public void setDimensione(int dimensione) {
        this.dimensione = dimensione;
    }

    @Override
    public String toString() {
        return "Team{" +
                "nome='" + nome +
                ", dimensione=" + dimensione +
                ", hackathon=" + (hackathon != null ? hackathon.getNome() : "null") +
                ", concorrenti=" + concorrenti +
                ", documenti=" + documenti +
                ", aggiornamenti=" + aggiornamenti +
                ", valutazioni=" + valutazioni +
                '}';
    }
}
