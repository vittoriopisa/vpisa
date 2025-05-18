package model;

import java.time.LocalDate;

public class HackathonTest {
    public static void main(String[] args) {
        System.out.println("Avvio test Hackathon...\n");

        // Test Hackathon
        Hackathon hackathon = new Hackathon("Hackathon 2025", "Evento tecnologico", "Napoli",
                "12-05-2025", "14-05-2025");

        System.out.println("Test Hackathon:");
        System.out.println("Nome: " + (hackathon.getNome().equals("Hackathon 2025") ? "OK" : "Errore"));
        System.out.println("Descrizione: " + (hackathon.getDescrizione().equals("Evento tecnologico") ? "OK" : "Errore"));
        System.out.println("Luogo: " + (hackathon.getLuogo().equals("Napoli") ? "OK" : "Errore"));

        // Test Organizzatore
        Organizzatore organizzatore = new Organizzatore("Marco", "Bianchi", "marco.bianchi@example.com",
                "Coordinatore", LocalDate.of(2024, 4, 1));

        System.out.println("\nTest Organizzatore:");
        System.out.println("Nome: " + (organizzatore.getNome().equals("Marco") ? "OK" : "Errore"));
        System.out.println("Ruolo: " + (organizzatore.getRuolo().equals("Coordinatore") ? "OK" : "Errore"));

        // Test Team
        Team team = new Team("Team Alpha", 5, hackathon);
        hackathon.aggiungiNuovoTeam(team);

        System.out.println("\nTest Team:");
        System.out.println("Nome: " + (team.getNome().equals("Team Alpha") ? "OK" : "Errore"));
        System.out.println("Dimensione: " + (team.getDimensione() == 5 ? "OK" : "Errore"));
        System.out.println("Hackathon: " + (team.getHackathon() != null ? "OK" : "Errore"));

        // Test Concorrente
        Concorrente concorrente = new Concorrente("Giulia", "Rossi", "giulia.rossi@example.com",
                LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 10), true, team);
        team.aggiungiNuovoConcorrente(concorrente);

        System.out.println("\nTest Concorrente:");
        System.out.println("Nome: " + (concorrente.getNome().equals("Giulia") ? "OK" : "Errore"));
        System.out.println("Status: " + (concorrente.getStatus() ? "OK" : "Errore"));
        System.out.println("Team: " + (concorrente.getTeam() != null ? "OK" : "Errore"));

        // Test Utente
        try {
            Utente utente = new Utente("Elena", "Verdi", "elena.verdi@example.com", LocalDate.of(2024, 3, 1));

            System.out.println("\nTest Utente:");
            System.out.println("Nome: " + (utente.getNome().equals("Elena") ? "OK" : "Errore"));
            System.out.println("Email valida: " + (utente.getEmail().contains("@") ? "OK" : "Errore"));
        } catch(IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        // Test Giudice
        Giudice giudice = new Giudice("Laura", "Verdi", "laura.verdi@example.com", LocalDate.now());

        System.out.println("\nTest Giudice:");
        System.out.println("Nome: " + (giudice.getNome().equals("Laura") ? "OK" : "Errore"));
        System.out.println("Cognome: " + (giudice.getCognome().equals("Verdi") ? "OK" : "Errore"));

        // Test Documento
        Documento documento = new Documento("Descrizione dettagliata", "Titolo Importante",
                LocalDate.now(), "DOCX", 2.0, "Relazione");

        System.out.println("\nTest Documento:");
        System.out.println("Descrizione: " + (documento.getDescrizione().equals("Descrizione dettagliata") ? "OK" : "Errore"));
        System.out.println("Titolo: " + (documento.getTitolo().equals("Titolo Importante") ? "OK" : "Errore"));
        System.out.println("Formato: " + (documento.getFormato().equals("DOCX") ? "OK" : "Errore"));

        // Test Valutazione
        try {
            Valutazione valutazione = new Valutazione(team, giudice, 9, "Ottima presentazione!");
            team.aggiungiNuovaValutazione(valutazione);
            giudice.aggiungiNuovaValutazione(valutazione);

            System.out.println("\nTest Valutazione:");
            System.out.println("Team: " + (valutazione.getTeam() != null ? "OK" : "Errore"));
            System.out.println("Giudice: " + (valutazione.getGiudice() != null ? "OK" : "Errore"));
            System.out.println("Punteggio: " + (valutazione.getPunteggio() == 9 ? "OK" : "Errore"));
            System.out.println("Feedback: " + (valutazione.getFeedback().equals("Ottima presentazione!") ? "OK" : "Errore"));
        } catch(IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
        }
        System.out.println("\nTest completati!");
    }
}
