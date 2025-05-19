package model;

import java.time.LocalDate;

public class HackathonTest {
    public static void main(String[] args) {
        System.out.println("Avvio test Hackathon...\n");

        // Creazione di un Hackathon
        Hackathon hackathon = new Hackathon("Hackathon 2025", "Evento tecnologico", "Napoli", LocalDate.of(2025, 5, 12), LocalDate.of(2025, 5, 14));
        System.out.println("Hackathon creato: " + hackathon.getNome());

        // Creazione di un Organizzatore
        Organizzatore organizzatore = new Organizzatore("Marco", "Bianchi", "marco.bianchi@example.com",
                "Coordinatore", LocalDate.of(2024, 4, 1));
        System.out.println("Organizzatore creato: " + organizzatore.getNome());

        // Creazione di un Team
        Team team = new Team("Team Alpha",5, hackathon);
        hackathon.aggiungiNuovoTeam(team);
        System.out.println("Team aggiunto: " + team.getNome());

        // Creazione di un Giudice
        Giudice giudice = new Giudice("Laura","Verdi","laura.verdi@example.com",LocalDate.of(2024, 4, 1));
        System.out.println("Giudice creato");

        // Test assegnazione problema prima della data di inizio
        Problema problema1 = new Problema(team, giudice, "Difficoltà nell'integrazione API.");
        System.out.println("Problema assegnato al team: " + (team.getProblema() != null ? "OK" : "Errore"));

        // Test assegnazione di un secondo problema (deve fallire)
        Problema problema2 = new Problema(team, giudice, "Altro problema.");
        System.out.println("Tentativo di assegnare un secondo problema: " + (team.getProblema() != problema2 ? "OK" : "Errore"));

        // Test apertura registrazioni hackathon
        try {
            organizzatore.apriRegistrazioni(hackathon);
            System.out.println("Registrazioni aperte: " + (hackathon.isStatoRegistrazioni() ? "OK" : "Errore"));
        } catch (RegistrazioneScadutaException e) {
            System.out.println("Errore apertura registrazioni: " + e.getMessage());
        }

        // Test pubblicazione classifica (prima della fine dell'hackathon, quindi deve fallire)
        hackathon.pubblicaClassifica();

        System.out.println("\nTest completati!");
    }
}