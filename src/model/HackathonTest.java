package model;

import java.time.LocalDate;

public class HackathonTest {
    public static void main(String[] args) {
        // Creazione di un Hackathon
        Hackathon hackathon = new Hackathon(
                "Hackathon Creatività",
                "Evento per sviluppatori innovativi",
                "Roma",
                "2025-06-01",
                "2025-06-05"

        );

        // Creazione di organizzatori
        Organizzatore organizzatore = new Organizzatore(
                "Luca",
                "Verdi",
                "luca.verdi@example.com",
                "Coordinatore",
                LocalDate.of(2023, 5, 15) // Data di registrazione

        );



        // Aggiunta degli organizzatori all'Hackathon
        hackathon.aggiungiNuovoOrganizzatore(organizzatore);


        // Creazione di team
        Team team1 = new Team("Squadra Alfa");
        Team team2 = new Team("Squadra Beta");

        // Impostare Hackathon nei team
        team1.setHackathon(hackathon);
        team2.setHackathon(hackathon);

        // Aggiunta dei team all'Hackathon
        hackathon.aggiungiNuovoTeam(team1);
        hackathon.aggiungiNuovoTeam(team2);

        // Creazione di concorrenti
        Concorrente concorrente1 = new Concorrente(
                "Mario",
                "Rossi",
                "mario.rossi@example.com",
                LocalDate.of(2023, 6, 1), // Data di registrazione
                LocalDate.now(), // Data di iscrizione
                true,
                team1
        );

        Concorrente concorrente2 = new Concorrente(
                "Giulia",
                "Bianchi",
                "giulia.bianchi@example.com",
                LocalDate.of(2023, 8, 20), // Data di registrazione
                LocalDate.now(), // Data di iscrizione
                true,
                team2
        );

        // Aggiunta dei concorrenti ai team
        team1.aggiungiNuovoConcorrente(concorrente1);
        team2.aggiungiNuovoConcorrente(concorrente2);

        // Creazione di documenti
        Documento documento1 = new Documento();
        documento1.setDescrizione("Descrizione del documento 1");
        documento1.setTitolo("Titolo del documento 1");
        team1.aggiungiDocumento(documento1);

        Documento documento2 = new Documento();
        documento2.setDescrizione("Descrizione del documento 2");
        documento2.setTitolo("Titolo del documento 2");
        team2.aggiungiDocumento(documento2);

        // Creazione di valutazioni
        Valutazione valutazione1 = new Valutazione();
        valutazione1.setTeam(team1);
        valutazione1.setPunteggio(8);
        valutazione1.setFeedback("Ottimo lavoro!");
        team1.aggiungiNuovaValutazione(valutazione1);

        Valutazione valutazione2 = new Valutazione();
        valutazione2.setTeam(team2);
        valutazione2.setPunteggio(7);
        valutazione2.setFeedback("Buon risultato!");
        team2.aggiungiNuovaValutazione(valutazione2);

        // Creazione di aggiornamenti
        Aggiornamento aggiornamento1 = new Aggiornamento();
        aggiornamento1.setTeam(team1);
        aggiornamento1.setContenuto("Aggiornamento tecnico relativo al progetto.");
        team1.aggiungiNuovoAggiornamento(aggiornamento1);

        Aggiornamento aggiornamento2 = new Aggiornamento();
        aggiornamento2.setTeam(team2);
        aggiornamento2.setContenuto("Aggiornamento creativo sul design.");
        team2.aggiungiNuovoAggiornamento(aggiornamento2);

        // Output: Verifica delle relazioni e dei dati
        System.out.println(hackathon);
        System.out.println("Organizzatori: " + hackathon.getOrganizzatori());
        System.out.println("Teams: " + hackathon.getTeams());
        System.out.println("Concorrenti nel team 1: " + team1.getConcorrenti());
        System.out.println("Documenti nel team 1: " + team1.getDocumenti());
        System.out.println("Valutazioni nel team 1: " + team1.getValutazioni());
        System.out.println("Aggiornamenti nel team 1: " + team1.getAggiornamenti());

        System.out.println("Concorrenti nel team 2: " + team2.getConcorrenti());
        System.out.println("Documenti nel team 2: " + team2.getDocumenti());
        System.out.println("Valutazioni nel team 2: " + team2.getValutazioni());
        System.out.println("Aggiornamenti nel team 2: " + team2.getAggiornamenti());
    }
}
