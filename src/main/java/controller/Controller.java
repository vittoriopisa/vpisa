package controller;

import dao.*;
import dao.impl.*;
import model.*;
import model.exceptions.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La classe {@code Controller} funge da livello intermedio tra
 * il livello di persistenza (DAO) e la logica applicativa.
 * <p>
 * Gestisce le operazioni principali sugli oggetti di dominio come
 * {@link Aggiornamento}, {@link Documento}, {@link Team}, ecc.,
 * orchestrando le chiamate ai rispettivi DAO.
 */
public class Controller {

    private final AggiornamentoDAO aggiornamentoDAO;
    private final CommentoDAO commentoDAO;
    private final DocumentoDAO documentoDAO;
    private final HackathonDAO hackathonDAO;
    private final ProblemaDAO problemaDAO;
    private final TeamDAO teamDAO;
    private final UtenteDAO utenteDAO;
    private final ValutazioneDAO valutazioneDAO;

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    /**
     * Costruttore predefinito.
     * <p>
     * Inizializza le implementazioni concrete dei DAO utilizzati
     * dal controller per gestire le operazioni sul database.
     */
    public Controller() {

        this.aggiornamentoDAO = new AggiornamentoDAOImpl();
        this.commentoDAO = new CommentoDAOImpl();
        this.documentoDAO = new DocumentoDAOImpl();
        this.hackathonDAO = new HackathonDAOImpl();
        this.problemaDAO = new ProblemaDAOImpl();
        this.teamDAO = new TeamDAOImpl();
        this.utenteDAO = new UtenteDAOImpl();
        this.valutazioneDAO = new ValutazioneDAOImpl();
    }


    /**
     * Salva un nuovo aggiornamento associato a un team e a un documento.
     *
     * @param teamId      ID del team autore dell’aggiornamento
     * @param documentoId ID del documento collegato all’aggiornamento
     * @param contenuto   testo dell’aggiornamento
     * @return un messaggio che descrive l’esito dell’operazione:
     *         <ul>
     *             <li>Conferma di avvenuto inserimento</li>
     *             <li>Messaggio di errore in caso di dati non validi o
     *                 problemi di accesso al database</li>
     *         </ul>
     */
    public String saveAggiornamento(int teamId, int documentoId, String contenuto) {
        try {

            if (!aggiornamentoDAO.existsDocumento(documentoId)) {
                return "Errore: Documento con ID " + documentoId + " non esiste.";
            }


            String nomeTeam = teamDAO.getNomeTeamById(teamId);
            String titoloDocumento = documentoDAO.getTitoloById(documentoId); // idem

            if (nomeTeam == null) return "Errore: Team con ID " + teamId + " non trovato.";
            if (titoloDocumento == null) return "Errore: Documento con ID " + documentoId + " non trovato.";


            Team team = new Team(teamId, nomeTeam);
            Documento documento = new Documento(documentoId, titoloDocumento);
            Aggiornamento aggiornamento = new Aggiornamento(team, documento, contenuto);


            aggiornamentoDAO.save(team.getId(), documento.getId(), aggiornamento.getContenuto());

            return "Aggiunto nuovo aggiornamento.";
        } catch (InvalidDataException e) {
            return "Errore di validazione: " + e.getMessage();
        } catch (SQLException e) {
            return "Errore durante il salvataggio: " + e.getMessage();
        }
    }



    /**
     * Elimina un aggiornamento esistente dal database.
     *
     * @param aggiornamentoId ID dell’aggiornamento da eliminare
     * @return un messaggio che descrive l’esito dell’operazione:
     *         <ul>
     *             <li>Conferma di eliminazione</li>
     *             <li>Messaggio di errore in caso di team o documento mancanti,
     *                 o problemi di accesso al database</li>
     *         </ul>
     */
    public String deleteAggiornamento(int aggiornamentoId) {
        try {

            int documentoId = aggiornamentoDAO.getDocumentoIdByAggiornamento(aggiornamentoId);


            int teamId = documentoDAO.getTeamIdByDocumento(documentoId);
            String nomeTeam = teamDAO.getNomeTeamById(teamId);
            String titoloDocumento = documentoDAO.getTitoloById(documentoId);

            if (nomeTeam == null || titoloDocumento == null) {
                return "Errore: Team o Documento non trovato.";
            }


            aggiornamentoDAO.delete(aggiornamentoId);

            return "Aggiornamento eliminato.";

        } catch (SQLException e) {
            return "Errore durante l'eliminazione: " + e.getMessage();
        }
    }

    /**
     * Recupera l’ID del documento collegato a un aggiornamento.
     *
     * @param aggiornamentoId ID dell’aggiornamento
     * @return l’ID del documento associato, oppure {@code -1} in caso di errore
     */
    public int getDocumentoIdByAggiornamento(int aggiornamentoId) {
        try {
            return aggiornamentoDAO.getDocumentoIdByAggiornamento(aggiornamentoId);
        } catch (SQLException e) {
            System.err.println("Errore recupero documento: " + e.getMessage());
            return -1;
        }
    }


    /**
     * Salva un nuovo commento associato a un documento e a un giudice.
     *
     * @param documentoId ID del documento a cui associare il commento
     * @param giudiceId   ID del giudice autore del commento
     * @param testo       contenuto testuale del commento
     * @return un messaggio che descrive l’esito dell’operazione:
     *         <ul>
     *             <li>Conferma di inserimento del commento</li>
     *             <li>Messaggio di errore in caso di documento o giudice non trovati</li>
     *             <li>Errore se non esistono aggiornamenti collegati al documento</li>
     *             <li>Messaggio di errore in caso di eccezioni SQL o di validazione</li>
     *         </ul>
     */
    public String saveCommento(int documentoId, int giudiceId, String testo) {
        try {

            String titoloDocumento = documentoDAO.getTitoloById(documentoId);
            String giudiceNome = utenteDAO.getNomeGiudiceById(giudiceId);
            String giudiceCognome = utenteDAO.getCognomeGiudiceById(giudiceId);
            String giudiceEmail = utenteDAO.getEmailGiudiceById(giudiceId);

            if (titoloDocumento == null || titoloDocumento.isEmpty()) {
                return "Errore: Documento non trovato con ID " + documentoId;
            }
            if (giudiceNome == null || giudiceNome.isEmpty()
                    || giudiceCognome == null || giudiceCognome.isEmpty()
                    || giudiceEmail == null || giudiceEmail.isEmpty()) {
                return "Errore: Giudice non trovato con ID " + giudiceId;
            }


            List<String> rawAggList = aggiornamentoDAO.findByDocumentoId(documentoId);
            if (rawAggList == null || rawAggList.isEmpty()) {
                return "Errore: Nessun aggiornamento trovato per il documento ID: " + documentoId;
            }


            Documento documento;
            Giudice giudice;
            try {
                documento = new Documento(documentoId, titoloDocumento);
                giudice = new Giudice(giudiceId, giudiceNome, giudiceCognome, giudiceEmail);
            } catch (InvalidDataException | RegistrazioneScadutaException e) {

                return "Errore nei dati del commento: " + e.getMessage();
            }

            boolean atLeastOneAdded = false;
            for (String row : rawAggList) {
                if (row == null || row.trim().isEmpty()) continue;

                String[] parts = row.split(";", 4);

                int parsedTeamId = -1;
                String parsedTeamNome = "";
                String parsedContenuto = "";

                try {
                    if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                        parsedTeamId = Integer.parseInt(parts[1].trim());
                    }
                } catch (NumberFormatException ignored) {
                    parsedTeamId = -1;
                }
                if (parts.length > 2) parsedTeamNome = parts[2].trim();
                if (parts.length > 3) parsedContenuto = parts[3];

                Team t;
                try {
                    t = new Team(parsedTeamId, parsedTeamNome);
                } catch (InvalidDataException e) {

                    continue;
                }

                Aggiornamento a;
                try {
                    a = new Aggiornamento(t, documento, parsedContenuto);
                } catch (InvalidDataException e) {

                    continue;
                }

                try {
                    documento.aggiungiAggiornamento(a);
                    atLeastOneAdded = true;
                } catch (Exception ignored) {

                }
            }

            if (!atLeastOneAdded) {
                return "Errore nei dati del commento: impossibile popolare gli aggiornamenti nel modello per il documento ID " + documentoId;
            }


            try {
                giudice.commentaDocumento(testo, documento);
            } catch (InvalidDataException e) {
                return "Errore nei dati del commento: " + e.getMessage();
            }

            try {
                commentoDAO.save(documentoId, giudiceId, testo);
            } catch (SQLException e) {
                return "Errore durante il salvataggio del commento: " + e.getMessage();
            }

            return "Nuovo commento aggiunto con successo.";

        } catch (SQLException e) {

            return "Errore durante il salvataggio del commento: " + e.getMessage();
        }
    }



    /**
     * Recupera tutti i commenti associati a un documento specifico.
     *
     * @param documentoId ID del documento di cui recuperare i commenti
     * @return una lista di stringhe contenenti i commenti:
     *         <ul>
     *             <li>I commenti presenti nel database</li>
     *             <li>Un messaggio che segnala l’assenza di commenti, se non trovati</li>
     *             <li>Un messaggio di errore in caso di eccezioni SQL</li>
     *         </ul>
     */
    public List<String> getCommentiByDocumentoId(int documentoId) {
        try {

            List<String> commentiDb = commentoDAO.findByDocumentoId(documentoId);
            if (commentiDb.isEmpty()) {
                return List.of("Nessun commento trovato per il documento ID: " + documentoId);
            }

            List<String> commentiModel = new ArrayList<>();
            for (String testo : commentiDb) {

                commentiModel.add(testo);
            }

            return commentiModel;

        } catch (SQLException e) {
            return List.of("Errore ricerca commenti: " + e.getMessage());
        }
    }

    /**
     * Salva un nuovo documento associato a un team.
     *
     * <p>Prima di salvare il documento vengono effettuati i seguenti controlli:
     * <ul>
     *     <li>Verifica che il team abbia un problema assegnato</li>
     *     <li>Verifica che il team esista nel database</li>
     * </ul>
     *
     * @param teamId        ID del team a cui associare il documento
     * @param titolo        titolo del documento
     * @param descrizione   descrizione del contenuto del documento
     * @param dataCreazione data di creazione del documento
     * @param formato       formato del documento (es. PDF, DOCX, ecc.)
     * @param dimensione    dimensione del documento in MB
     * @param tipo          tipologia del documento (es. relazione, codice, presentazione)
     * @return un messaggio che descrive l’esito dell’operazione:
     *         <ul>
     *             <li>Messaggio di conferma se il documento viene salvato con successo</li>
     *             <li>Errore se il team non esiste o non ha un problema assegnato</li>
     *             <li>Errore in caso di eccezioni SQL o di validazione</li>
     *         </ul>
     */
    public String saveDocumento(int teamId, String titolo, String descrizione, LocalDate dataCreazione,
                                String formato, double dimensione, String tipo) {
        try {
            if (!documentoDAO.existsProblema(teamId)) {
                return "Errore: Il team con ID " + teamId + " non ha un problema assegnato.";
            }

            String nomeTeam = teamDAO.getNomeTeamById(teamId);
            if (nomeTeam == null) {
                return "Errore: Team con ID " + teamId + " non trovato.";
            }

            Team team = new Team(teamId, nomeTeam);
            Documento documento = new Documento(0, titolo, descrizione, formato, dimensione, tipo, team);

            documentoDAO.save(teamId, titolo, descrizione, dataCreazione, formato, dimensione, tipo);

            return "Documento '" + documento.getTitolo() + "' salvato con successo per il team '" + team.getNome() + "'.";

        } catch (InvalidDataException e) {
            return "Errore di validazione: " + e.getMessage();
        } catch (SQLException e) {
            return "Errore durante il salvataggio del documento: " + e.getMessage();
        }
    }

    /**
     * Recupera l'ID del team a cui è associato un documento.
     *
     * @param documentoId ID del documento da cui risalire al team
     * @return l'ID del team se trovato, -1 in caso di errore
     */
    public int getTeamIdByDocumento(int documentoId) {
        try {
            return documentoDAO.getTeamIdByDocumento(documentoId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero teamId per documentoId=" + documentoId, e);
            return -1;
        }
    }

    /**
     * Recupera i documenti associati a un team.
     *
     * @param teamId      ID del team
     * @param documentoId ID del documento (può essere usato per filtrare o dettagliare la ricerca)
     * @return una lista di descrizioni testuali dei documenti oppure un messaggio di errore
     */
    public List<String> getDocumentoByTeamId(int teamId,Integer documentoId) {
        try {
            return documentoDAO.findDocumentoByTeamId(teamId,documentoId);
        } catch (SQLException e) {
            return List.of("Errore nel recupero dei documenti: " + e.getMessage());
        }
    }

    /**
     * Recupera l'ID dell'hackathon a cui è associato un documento.
     *
     * @param documentoId ID del documento
     * @return l'ID dell'hackathon se trovato, -1 in caso di errore
     */
    public int getHackathonIdByDocumento(int documentoId) {
        try {
            return documentoDAO.getHackathonIdByDocumento(documentoId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero hackathonId per teamId=" + documentoId, e);
            return -1;
        }
    }

    /**
     * Salva un nuovo hackathon associato a un organizzatore.
     *
     * <p>Vengono effettuati i seguenti controlli prima del salvataggio:
     * <ul>
     *     <li>L'utente specificato deve esistere e avere ruolo "organizzatore"</li>
     *     <li>Vengono verificati nome e cognome dell’organizzatore</li>
     *     <li>Vengono validati i dati dell'hackathon</li>
     * </ul>
     *
     * @param nome            nome dell'hackathon
     * @param descrizione     descrizione dell'evento
     * @param luogo           luogo in cui si terrà l'hackathon
     * @param dataInizio      data di inizio
     * @param dataFine        data di fine
     * @param organizzatoreId ID dell’organizzatore associato
     * @return un messaggio che indica l’esito dell’operazione
     */
    public String saveHackathon(String nome, String descrizione, String luogo,
                                LocalDate dataInizio, LocalDate dataFine, int organizzatoreId) {
        try {

            String tipo = utenteDAO.getUserTypeById(organizzatoreId);
            String nomeOrganizzatore = utenteDAO.getNomeOrgById(organizzatoreId);
            String cognomeOrganizzatore = utenteDAO.getCognomeOrgById(organizzatoreId);
            String emailOrganizzatore = utenteDAO.getEmailOrgById(organizzatoreId);

            if (tipo == null || !tipo.equalsIgnoreCase("organizzatore")) {
                return "Errore: l'utente con ID " + organizzatoreId + " non è un organizzatore valido.";
            }
            if (nomeOrganizzatore == null || cognomeOrganizzatore == null || emailOrganizzatore == null) {
                return "Errore: dati dell'organizzatore non trovati.";
            }

            try {

                Organizzatore org = new Organizzatore(organizzatoreId, nomeOrganizzatore, cognomeOrganizzatore,emailOrganizzatore);
                new Hackathon(nome, descrizione, luogo, dataInizio, dataFine, org);
            } catch (InvalidDataException e) {
                return "Errore di validazione Organizzatore: " + e.getMessage();
            }

            hackathonDAO.save(nome, descrizione, luogo, dataInizio, dataFine, organizzatoreId);

            return "Hackathon salvato con successo.";

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio dell'hackathon", e);
            return "Errore durante il salvataggio dell'hackathon: " + e.getMessage();
        }
    }

    /**
            * Recupera la lista di tutti gli hackathon dal database.
     *
     * @param organizzatoreId      identificativo univoco dell'organizzatore
             * <p>I dati grezzi ottenuti dalla DAO vengono trasformati in oggetti {@link Hackathon},
            * arricchiti con i dati dell’organizzatore, e convertiti in stringa.</p>
            *
            * @return lista di stringhe con le informazioni sugli hackathon
     */


    public List<String> getAllHackathons(int organizzatoreId) {
        List<String> result = new ArrayList<>();

        try {

            List<String> allHackathonsRaw = hackathonDAO.getAll();

            if (allHackathonsRaw == null || allHackathonsRaw.isEmpty()) {
                result.add("Nessun hackathon trovato.");
                return result;
            }


            for (String row : allHackathonsRaw) {
                try {
                    int hackathonId = -1;

                    if (row.contains("ID:")) {
                        String[] parts = row.split(",");
                        for (String p : parts) {
                            p = p.trim();
                            if (p.startsWith("ID:")) {
                                try {
                                    hackathonId = Integer.parseInt(p.split(":")[1].trim());
                                } catch (NumberFormatException ignored) { }
                                break;
                            }
                        }
                    }
                    if (hackathonId == -1 && row.contains(" - ")) {
                        try {
                            hackathonId = Integer.parseInt(row.split(" - ")[0].trim());
                        } catch (NumberFormatException ignored) { }
                    }

                    if (hackathonId == -1) {
                        logger.log(Level.WARNING, "Formato hackathon non riconosciuto: " + row);
                        continue;
                    }

                    int orgIdFromDb;
                    try {
                        orgIdFromDb = hackathonDAO.getOrganizzatoreIdByHackathon(hackathonId);
                    } catch (SQLException e) {
                        logger.log(Level.WARNING, "Impossibile ottenere organizzatore per hackathonId=" + hackathonId, e);
                        continue;
                    }

                    if (orgIdFromDb != organizzatoreId) {
                        continue;
                    }

                    String nome = "";
                    String descrizione = "";
                    String luogo = "";
                    LocalDate dataInizio = null;
                    LocalDate dataFine = null;

                    try {
                        String[] parts = row.split(",");
                        for (String part : parts) {
                            part = part.trim();
                            if (part.startsWith("Nome:")) nome = part.split(":", 2)[1].trim();
                            else if (part.startsWith("Descrizione:")) descrizione = part.split(":", 2)[1].trim();
                            else if (part.startsWith("Luogo:")) luogo = part.split(":", 2)[1].trim();
                            else if (part.startsWith("Data Inizio:") || part.startsWith("DataInizio:"))
                                dataInizio = LocalDate.parse(part.split(":", 2)[1].trim());
                            else if (part.startsWith("Data Fine:") || part.startsWith("DataFine:"))
                                dataFine = LocalDate.parse(part.split(":", 2)[1].trim());
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Parsing parziale fallito per hackathonId=" + hackathonId, e);
                    }

                    String nomeOrg = "";
                    String cognomeOrg = "";
                    String emailOrg = "";
                    try {
                        nomeOrg = utenteDAO.getNomeOrgById(orgIdFromDb);
                        cognomeOrg = utenteDAO.getCognomeOrgById(orgIdFromDb);
                        emailOrg = utenteDAO.getEmailOrgById(orgIdFromDb);
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "Errore recupero organizzatore id=" + orgIdFromDb, e);
                    }


                    Hackathon hackathon;
                    if (dataInizio != null && dataFine != null) {
                        hackathon = new Hackathon(
                                nome, descrizione, luogo, dataInizio, dataFine,
                                new Organizzatore(orgIdFromDb, nomeOrg, cognomeOrg, emailOrg)
                        );
                    } else {
                        hackathon = new Hackathon(
                                nome,
                                new Organizzatore(orgIdFromDb, nomeOrg, cognomeOrg, emailOrg)
                        );
                    }


                    result.add(hackathon.toString());

                } catch (Exception e) {
                    logger.log(Level.WARNING, "Errore durante il parsing di una riga hackathon: " + row, e);
                }
            }

        } catch (SQLException e) {
            result.add("Errore durante la visualizzazione degli hackathon: " + e.getMessage());
        }

        if (result.isEmpty()) {
            result.add("Nessun hackathon trovato per questo organizzatore.");
        }

        return result;
    }


    /**
     * Recupera la lista di hackathon con registrazioni ancora aperte.
     *
     * @param organizzatoreId      identificativo univoco dell'organizzatore
     * @return lista di stringhe contenenti hackathon con iscrizioni aperte,
     *         oppure un messaggio che segnala l'assenza di eventi disponibili
     */

    public List<String> getHackathonsWithOpenRegistrations(int organizzatoreId) {
        List<String> result = new ArrayList<>();
        try {
            List<String> rawList = hackathonDAO.findByRegistrazioniAperte(true);

            if (rawList.isEmpty()) {
                result.add("Nessun hackathon con registrazioni aperte.");
                return result;
            }

            for (String row : rawList) {
                try {

                    if (row.contains(", ") && row.contains("ID:")) {
                        String[] parts = row.split(", ");

                        int id = -1;
                        try {

                            for (String p : parts) {
                                if (p.trim().startsWith("ID:")) {
                                    id = Integer.parseInt(p.split(":")[1].trim());
                                    break;
                                }
                            }
                        } catch (NumberFormatException ignore) {}

                        // se richiesto filtrare per organizzatore
                        if (organizzatoreId != -1 && id != -1) {
                            int orgIdFromRow = hackathonDAO.getOrganizzatoreIdByHackathon(id);
                            if (orgIdFromRow != organizzatoreId) {
                                continue;
                            }
                        }


                        result.add(row);
                    }

                    else if (row.contains(" - ")) {

                        int id = -1;
                        try {
                            id = Integer.parseInt(row.split(" - ")[0].trim());
                        } catch (NumberFormatException ignore) {}

                        if (organizzatoreId != -1 && id != -1) {
                            int orgIdFromRow = hackathonDAO.getOrganizzatoreIdByHackathon(id);
                            if (orgIdFromRow != organizzatoreId) {
                                continue;
                            }
                        }

                        result.add(row);
                    }

                    else {
                        result.add(row);
                    }
                } catch (Exception e) {

                    logger.log(Level.WARNING, "Parsing hackathon riga failed: " + row, e);

                    result.add(row);
                }
            }

        } catch (SQLException e) {
            result.add("Errore durante la visualizzazione degli hackathon con registrazioni aperte: " + e.getMessage());
        }
        return result;
    }

    /**
     * Recupera tutti gli hackathon creati da un organizzatore specifico.
     *
     * @param nome dell'organizzatore
     * @param organizzatoreId ID dell’organizzatore
     * @return id dell'hackathon
     */

    public int getHackathonIdByNameAndOrganizzatore(String nome, int organizzatoreId) {
        try {
            return hackathonDAO.getIdByNameAndOrganizzatore(nome, organizzatoreId);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Errore DB getHackathonIdByNameAndOrganizzatore", e);
            return -1;
        }
    }


    /**
            * Recupera tutti gli hackathon creati da un organizzatore specifico.
     *
             * @param organizzatoreId ID dell’organizzatore
     * @return lista di stringhe contenenti gli hackathon gestiti dall’organizzatore
     */

    public List<String> getHackathonByOrganizzatoreId(int organizzatoreId) {
        List<String> result = new ArrayList<>();
        try {
            List<String> rawList = hackathonDAO.getHackathonByOrganizzatoreId(organizzatoreId);

            if (rawList == null || rawList.isEmpty()) {
                result.add("Nessun hackathon trovato per l'organizzatore ID: " + organizzatoreId);
                return result;
            }

            for (String row : rawList) {
                if (row == null) continue;
                row = row.trim();


                Integer id = null;
                String nome = null;

                try {

                    if (row.contains(" - ")) {
                        String[] parts = row.split(" - ", 2);
                        id = Integer.parseInt(parts[0].trim());
                        nome = parts[1].trim();
                    }

                    else if (row.contains(";")) {
                        String[] parts = row.split(";", 2);
                        nome = parts[0].trim();

                    }

                    else if (row.contains("ID:") && row.contains("Nome:")) {
                        String[] parts = row.split(",\\s*");
                        for (String p : parts) {
                            if (p.startsWith("ID:")) {
                                try { id = Integer.parseInt(p.split(":")[1].trim()); } catch (Exception ignored) {}
                            } else if (p.startsWith("Nome:")) {
                                nome = p.split(":", 2)[1].trim();
                            }
                        }
                    }

                    else {
                        nome = row;
                    }
                } catch (Exception e) {

                    continue;
                }

                if (nome == null || nome.isEmpty()) continue;


                if (id != null && id > 0) {
                    result.add(id + " - " + nome);
                } else {
                    result.add(nome);
                }
            }

        } catch (SQLException e) {
            result.add("Errore nel caricamento hackathon: " + e.getMessage());
        }
        return result;
    }

    /**
     * Recupera e pubblica la classifica di un hackathon.
     *
     *
     * @param hackathonId ID dell’hackathon
     * @return lista di stringhe con la classifica oppure un messaggio di errore
     */
    public List<String> getClassificaHackathon(int hackathonId) {
        try {

            LocalDate dataFine = hackathonDAO.getDataFineById(hackathonId);


            if (dataFine == null) {
                return List.of("Errore: data di fine non impostata per l'hackathon.");
            }

            if (dataFine.isAfter(LocalDate.now())) {
                return List.of("Classifica disponibile solo dopo la conclusione dell'hackathon (data fine: " + dataFine + ").");
            }

            List<String> raw = hackathonDAO.getClassifica(hackathonId);

            if (raw == null || raw.isEmpty()) {
                return List.of("Nessuna valutazione disponibile per questo hackathon.");
            }

            List<String> out = new ArrayList<>();
            out.add("Classifica Hackathon ID: " + hackathonId);

            int posizione = 1;
            for (String row : raw) {
                if (row == null || row.trim().isEmpty()) continue;

                String[] parts = row.split(";", 3);
                String teamIdStr = parts.length > 0 ? parts[0].trim() : "";
                String teamName = parts.length > 1 ? parts[1].trim() : ("Team_" + teamIdStr);
                String avgStr = parts.length > 2 ? parts[2].trim() : "";
                String avgFormatted = "N/A";

                if (!avgStr.isEmpty()) {
                    try {
                        double avg = Double.parseDouble(avgStr.replace(',', '.')); // tollera virgola
                        avgFormatted = String.format("%.2f", avg);
                    } catch (NumberFormatException ignored) { /* rimane N/A */ }
                }


                StringBuilder sb = new StringBuilder();
                sb.append(posizione).append(". ");
                sb.append(teamName);
                if (!teamIdStr.isEmpty()) sb.append(" (ID: ").append(teamIdStr).append(")");
                sb.append(" - Punteggio medio: ").append(avgFormatted);

                out.add(sb.toString());
                posizione++;
            }


            return out;

        } catch (SQLException e) {
            return List.of("Errore durante il recupero della classifica: " + e.getMessage());
        } catch (Exception e) {
            return List.of("Errore durante il calcolo della classifica: " + e.getMessage());
        }
    }

    /**
     * Recupera la lista degli hackathon con registrazioni aperte,
     * pensata per popolare interfacce grafiche (es. combo box).
     *
     * @return lista di stringhe con hackathon disponibili,
     *         oppure un messaggio di errore
     */
    public List<String> getHackathonsWithOpenRegistrationsCombo() {
        try {
            return hackathonDAO.findByRegistrazioniAperte(true);
        } catch (SQLException e) {
            return List.of("Errore nel caricamento hackathon: " + e.getMessage());
        }
    }
    /**
     * Recupera l'ID dell’organizzatore associato a un hackathon.
     *
     * @param hackathonId ID dell’hackathon
     * @return ID dell’organizzatore se trovato, altrimenti -1
     */
    public int getOrganizzatoreIdByHackathon(int hackathonId) {
        try {
            return hackathonDAO.getOrganizzatoreIdByHackathon(hackathonId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Errore durante il recupero organizzatoreId per hackathonId=" + hackathonId, e);
            return -1;
        }
    }

    /**
     * Assegna un problema a un team, creato da un giudice.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Recupera i dati del giudice e del team dal database</li>
     *     <li>Verifica la loro esistenza</li>
     *     <li>Invoca la logica di dominio per l’assegnazione del problema</li>
     *     <li>Salva l’associazione nel database tramite {@code problemaDAO}</li>
     * </ul>
     *
     * @param titolo titolo del problema
     * @param descrizione descrizione del problema
     * @param teamId ID del team a cui assegnare il problema
     * @param giudiceId ID del giudice che assegna il problema
     * @param problemaId ID del problema (se già esistente)
     * @return messaggio che indica l’esito dell’operazione
     */
    public String assignProblemaToTeam(String titolo, String descrizione, Integer teamId, int giudiceId, Integer problemaId) {
        try {
            String giudiceNome = utenteDAO.getNomeGiudiceById(giudiceId);
            String giudiceCognome = utenteDAO.getCognomeGiudiceById(giudiceId);
            String giudiceEmail = utenteDAO.getEmailGiudiceById(giudiceId);
            String nomeTeam = teamDAO.getNomeTeamById(teamId);

            if (giudiceNome == null || giudiceCognome == null || giudiceEmail == null) return "Errore: Giudice non trovato.";
            if (nomeTeam == null) return "Errore: Team non trovato.";

            Giudice giudice = new Giudice(giudiceId, giudiceNome, giudiceCognome,giudiceEmail);
            Team team = new Team(teamId, nomeTeam);

            giudice.assegnaProblemaATeam(titolo, descrizione, team);

            boolean success = problemaDAO.assignProblemaToTeam(titolo,descrizione,teamId,giudiceId,problemaId);

            if (!success) {
                return "Errore: impossibile salvare il problema nel DB.";
            }

            return "Problema creato e assegnato al team '" + team.getNome() + "' con successo.";

        } catch (InvalidDataException | RegistrazioneScadutaException e) {
            return "Errore nella validazione: " + e.getMessage();
        } catch (SQLException e) {
            return "Errore durante il salvataggio nel DB: " + e.getMessage();
        }
    }

    /**
     * Salva un nuovo team all’interno di un hackathon esistente.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Verifica che l’hackathon esista</li>
     *     <li>Recupera i dati dell’organizzatore associato</li>
     *     <li>Crea l’oggetto {@link Team} e lo aggiunge all’hackathon</li>
     *     <li>Registra il team nel database</li>
     * </ul>
     *
     * @param nome nome del team
     * @param hackathonId ID dell’hackathon a cui associare il team
     * @return messaggio che indica l’esito dell’operazione
     */
    public String saveTeam(String nome, int hackathonId) {
        try {

            String nomeHackathon = hackathonDAO.getNomeById(hackathonId);
            if (nomeHackathon == null) {
                return "Errore: Hackathon con ID " + hackathonId + " non trovato.";
            }

            int organizzatoreId = hackathonDAO.getOrganizzatoreIdByHackathon(hackathonId);

            String nomeOrg = utenteDAO.getNomeOrgById(organizzatoreId);
            String cognomeOrg = utenteDAO.getCognomeOrgById(organizzatoreId);
            String emailOrg = utenteDAO.getEmailOrgById(organizzatoreId);

            Organizzatore organizzatore = new Organizzatore(organizzatoreId, nomeOrg, cognomeOrg,emailOrg);
            Hackathon hackathon = new Hackathon(nomeHackathon, organizzatore);
            Team team = new Team(0, nome, hackathon);

            boolean aggiunto = hackathon.aggiungiTeam(team);

            if (!aggiunto) {
                return "Errore: Il team '" + nome + "' è già presente nell'hackathon '" + nomeHackathon + "'.";
            }

            teamDAO.save(nome, hackathonId);

            return "Team '" + team.getNome() + "' salvato con successo nell'hackathon '" + nomeHackathon + "'.";

        } catch (InvalidDataException e) {
            return "Errore di validazione: " + e.getMessage();
        } catch (SQLException e) {
            return "Errore durante il salvataggio del team: " + e.getMessage();
        }
    }

    /**
     * Elimina un team esistente.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Verifica l’esistenza del team tramite il suo ID</li>
     *     <li>Crea l’oggetto {@link Team} per rappresentarlo</li>
     *     <li>Rimuove il team dal database tramite {@code teamDAO}</li>
     * </ul>
     *
     * @param id ID del team da eliminare
     * @return messaggio che indica l’esito dell’operazione
     */
    public String deleteTeam(int id) {
        try {

            String nomeTeam = teamDAO.getNomeTeamById(id);

            if (nomeTeam == null) {
                return "Errore: Team con ID " + id + " non trovato.";
            }

            Team team = new Team(id, nomeTeam);

            teamDAO.delete(id);

            return "Team '" + team.getNome() + "' eliminato con successo.";

        } catch (InvalidDataException e) {
            return "Errore di validazione Organizzatore: " + e.getMessage();
        }catch (SQLException e) {
            return "Errore durante l'eliminazione del team: " + e.getMessage();
        }
    }


    /**
     * Recupera tutti i team associati a un hackathon specifico.
     *
     * <p>I team vengono aggiunti all’oggetto {@link Hackathon} e restituiti
     * come lista di stringhe contenenti i nomi.</p>
     *
     * @param hackathonId ID dell’hackathon
     * @return lista dei nomi dei team o un messaggio di errore
     */
    public List<String> getTeamsByHackathonId(int hackathonId) {
        try {

            String nomeHackathon = hackathonDAO.getNomeById(hackathonId);

            if (nomeHackathon == null) {
                return List.of("Hackathon non trovato con ID: " + hackathonId);
            }

            Hackathon hackathon = new Hackathon(nomeHackathon);

            List<String> nomiTeams = teamDAO.findByHackathonId(hackathonId);

            for (String nomeTeam : nomiTeams) {
                Team team = new Team(0, nomeTeam);
                hackathon.aggiungiTeam(team);
            }

            return hackathon.getTeams()
                    .stream()
                    .map(Team::getNome)
                    .toList();

        } catch (Exception e) {
            return List.of("Errore durante la ricerca dei team: " + e.getMessage());
        }
    }

    /**
     * Recupera i team di un hackathon che non hanno ancora raggiunto il numero massimo di membri.
     *
     * <p>Per ogni team viene verificato che il numero di concorrenti sia inferiore a 6.</p>
     *
     * @param hackathonId ID dell’hackathon
     * @return lista dei nomi dei team non pieni o un messaggio di errore
     */
        public List<String> getTeamsNotFull(int hackathonId) {
            try {
                String nomeHackathon = hackathonDAO.getNomeById(hackathonId);

                if (nomeHackathon == null) {
                    return List.of("Hackathon non trovato con ID: " + hackathonId);
                }

                Hackathon hackathon = new Hackathon(nomeHackathon);

                List<String> nomiTeams = teamDAO.findTeamsNotFull(hackathonId);

                for (String nomeTeam : nomiTeams) {
                    Team team = new Team(0, nomeTeam);
                    hackathon.aggiungiTeam(team);
                }

                return hackathon.getTeams().stream()
                        .filter(team -> team.getConcorrenti().size() < 6)
                        .map(Team::getNome)
                        .toList();

            } catch (Exception e) {
                return List.of("Errore durante la ricerca dei team non pieni: " + e.getMessage());
            }
        }

    /**
     * Recupera l’ID del team a cui appartiene un utente.
     *
     * @param userId ID dell’utente
     * @return ID del team se trovato, altrimenti -1
     */
        public int getTeamIdByUser ( int userId){
            try {
                return teamDAO.getTeamIdByUser(userId);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore durante il recupero teamId per userId=" + userId, e);
                return -1;
            }
        }

    /**
     * Recupera l’ID dell’hackathon a cui appartiene un team.
     *
     * @param teamId ID del team
     * @return ID dell’hackathon se trovato, altrimenti -1
     */
        public int getHackathonIdByTeam ( int teamId){
            try {
                return teamDAO.getHackathonIdByTeam(teamId);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore durante il recupero hackathonId per teamId=" + teamId, e); // oppure log
                return -1;
            }
        }

        /**
     * Registra un nuovo utente nel sistema.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Verifica il tipo di utente ({@code concorrente}, {@code giudice}, {@code organizzatore})</li>
     *     <li>Istanzia il relativo oggetto di dominio ({@link Concorrente}, {@link Giudice}, {@link Organizzatore})</li>
     *     <li>Effettua la validazione dei dati</li>
     *     <li>Salva l’utente nel database tramite {@code utenteDAO}</li>
     * </ul>
     *
     * @param nome nome dell’utente
     * @param cognome cognome dell’utente
     * @param email indirizzo email
     * @param password password in chiaro (verrà salvata in forma hashata)
     * @param dataRegistrazione data di registrazione (se null viene usata la data corrente)
     * @param tipoUtente tipo di utente ({@code concorrente}, {@code giudice}, {@code organizzatore})
     * @param hackathonId ID dell’hackathon di appartenenza (se presente)
     * @param teamId ID del team di appartenenza (se presente)
     * @return messaggio che indica l’esito della registrazione
     */

    public String saveUtente(String nome, String cognome, String email, String password,
                             LocalDate dataRegistrazione, String tipoUtente,
                             Integer hackathonId, Integer teamId) {
        try {
            String nomeHackathon = null;
            if (hackathonId != null) {

                nomeHackathon = hackathonDAO.getNomeById(hackathonId);
                if (nomeHackathon == null) {
                    return "Errore: Hackathon con ID " + hackathonId + " non trovato.";
                }
            }

            if (hackathonId != null && !"organizzatore".equalsIgnoreCase(tipoUtente)) {
                boolean foundOpen = false;
                try {

                    List<String> openHackathons = hackathonDAO.findByRegistrazioniAperte(true);

                    if (openHackathons != null) {
                        for (String row : openHackathons) {
                            int parsedId = extractIdFromHackathonRow(row);
                            if (parsedId == hackathonId) {
                                foundOpen = true;
                                break;
                            }

                            if (parsedId == -1) {

                                String parsedName = extractNameFromRawHackathonRow(row);
                                if (parsedName != null && parsedName.equalsIgnoreCase(nomeHackathon)) {
                                    foundOpen = true;
                                    break;
                                }
                            }
                        }
                    }
                } catch (SQLException e) {


                    foundOpen = false;
                }

                if (!foundOpen) {
                    return "Errore di validazione utente: Le registrazioni per l'hackathon '" + nomeHackathon + "' sono chiuse";
                }
            }

            Utente utente;
            try {
                if ("concorrente".equalsIgnoreCase(tipoUtente)) {
                    utente = new Concorrente(0, nome, cognome, email, password,
                            dataRegistrazione != null ? dataRegistrazione : LocalDate.now(),
                            null);
                } else if ("giudice".equalsIgnoreCase(tipoUtente)) {
                    utente = new Giudice(0, nome, cognome, email, password,
                            dataRegistrazione != null ? dataRegistrazione : LocalDate.now(),
                            null);
                } else if ("organizzatore".equalsIgnoreCase(tipoUtente)) {
                    utente = new Organizzatore(0, nome, cognome, email, password,
                            dataRegistrazione != null ? dataRegistrazione : LocalDate.now());
                } else {
                    return "Errore: tipo utente non valido: " + tipoUtente;
                }
            } catch (Exception e) {
                return "Errore di validazione utente: " + e.getMessage();
            }


            utenteDAO.save(nome, cognome, utente.getEmail(),utente.getPasswordHash(), utente.getDataRegistrazione(),
                    tipoUtente, hackathonId, teamId);

            return "Utente '" + utente.getNome() + "' registrato con successo!"
                    + (nomeHackathon != null ? " Hackathon: '" + nomeHackathon + "'" : "");

        } catch (SQLException e) {
            return "Errore durante il salvataggio dell'utente: " + e.getMessage();
        }
    }

    /* Helper: prova a estrarre un ID da una riga raw dell'hackathon.
       Ritorna id se trovato, altrimenti -1. */
    private int extractIdFromHackathonRow(String row) {
        if (row == null) return -1;
        row = row.trim();
        try {

            if (row.contains("ID:")) {
                String[] parts = row.split(",");
                for (String p : parts) {
                    p = p.trim();
                    if (p.startsWith("ID:")) {
                        String after = p.substring(3).trim();
                        return Integer.parseInt(after);
                    }
                }
            }

            if (row.contains(" - ")) {
                String first = row.split(" - ")[0].trim();
                return Integer.parseInt(first);
            }

        } catch (NumberFormatException ignored) { }
        return -1;
    }

    /* Helper: prova ad estrarre il nome dall'attuale formato "Nome;..." oppure "42 - Nome"
       Ritorna nome, o null se non riconosciuto. */
    private String extractNameFromRawHackathonRow(String row) {
        if (row == null) return null;
        try {
            if (row.contains(";")) {
                String[] parts = row.split(";");
                return parts[0].trim();
            }
            if (row.contains(" - ")) {
                String[] parts = row.split(" - ", 2);
                if (parts.length > 1) return parts[1].trim();
            }

            if (row.contains("Nome:")) {
                String[] parts = row.split(",");
                for (String p : parts) {
                    p = p.trim();
                    if (p.startsWith("Nome:")) {
                        return p.split(":", 2)[1].trim();
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Aggiunge un utente concorrente a un team.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Recupera i dati del team e del concorrente dal database</li>
     *     <li>Istanzia gli oggetti {@link Team} e {@link Concorrente}</li>
     *     <li>Aggiunge l’utente al team sia a livello di database sia a livello di modello</li>
     * </ul>
     *
     * @param userId ID dell’utente concorrente
     * @param teamId ID del team
     * @return messaggio che indica l’esito dell’operazione
     */
        public String addUtenteToTeam(int userId, int teamId) {
            try {

                String nomeTeam = teamDAO.getNomeTeamById(teamId);
                String nomeConcorrente = utenteDAO.getNomeConcById(userId);
                String cognomeConcorrente = utenteDAO.getCognomeConcById(userId);
                String emailConcorrente=utenteDAO.getEmailConcById(userId);

                Team team = new Team(teamId, nomeTeam);
                Concorrente concorrente = new Concorrente(userId, nomeConcorrente, cognomeConcorrente, emailConcorrente);

                utenteDAO.addUserToTeam(userId, teamId);


                team.aggiungiConcorrente(concorrente);


                return String.format("Utente %s %s aggiunto al team %s con successo!",
                        nomeConcorrente, cognomeConcorrente, nomeTeam);

            } catch (SQLException e) {
                return "Errore durante l'aggiunta dell'utente al team: " + e.getMessage();
            } catch (InvalidDataException | TeamFullException | RegistrazioneScadutaException e) {
                return "Errore di validazione: " + e.getMessage();
            }
        }

    /**
     * Rimuove un concorrente da un team.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Recupera i dati dell’utente e del team</li>
     *     <li>Istanzia i corrispondenti oggetti di dominio</li>
     *     <li>Rimuove l’associazione dal database tramite {@code utenteDAO}</li>
     *     <li>Rimuove il concorrente dal team lato modello</li>
     * </ul>
     *
     * @param userId ID dell’utente concorrente
     * @param teamId ID del team
     * @return messaggio che indica se l’utente è stato rimosso con successo o se non apparteneva al team
     */

    public String removeUtenteFromTeam(int userId, int teamId) {
        try {

            int currentTeamId = utenteDAO.getTeamIdByUser(userId); // deve ritornare -1/0 se non appartiene
            if (currentTeamId == -1 || currentTeamId == 0) {
                return "L'utente non appartiene ad alcun team.";
            }

            if (currentTeamId != teamId) {
                return "L'utente non appartiene al team con ID " + teamId + " (è nel team " + currentTeamId + ").";
            }

            String nomeConc = utenteDAO.getNomeConcById(userId);
            String cognomeConc = utenteDAO.getCognomeConcById(userId);
            String nomeTeam = teamDAO.getNomeTeamById(teamId);

            boolean removed = utenteDAO.removeUserFromTeam(userId);

            if (removed) {
                return String.format("Utente %s %s rimosso con successo dal team '%s' (ID %d).",
                        nomeConc, cognomeConc, nomeTeam != null ? nomeTeam : ("ID " + teamId), teamId);
            } else {

                return "Errore: impossibile rimuovere l'utente dal team (operazione DB non completata).";
            }

        } catch (SQLException e) {
            return "Errore durante la rimozione dell'utente dal team: " + e.getMessage();
        } catch (Exception e) {
            return "Errore: " + e.getMessage();
        }
    }

    /**
     * Recupera tutti gli utenti di un determinato tipo per un dato hackathon.
     *
     * @param hackathonId ID dell’hackathon
     * @param tipoUtente tipo di utente da cercare ({@code concorrente}, {@code giudice}, {@code organizzatore})
     * @return lista di utenti trovati oppure un messaggio di errore
     */
            public List<String> getUtentiByTipoUtenteForHackathon ( int hackathonId, String tipoUtente){
            try {
                return utenteDAO.findByTipoUtenteForHackathon(hackathonId, tipoUtente);
            } catch (SQLException e) {
                return List.of("Errore durante la ricerca degli utenti: " + e.getMessage());
            }
        }

    /**
     * Recupera tutti i concorrenti associati a un team in un hackathon.
     *
     * <p>Gli utenti vengono caricati dal database e aggiunti a un oggetto {@link Team}.</p>
     *
     * @param teamId ID del team
     * @param hackathonId ID dell’hackathon
     * @return lista dei nomi e cognomi dei concorrenti oppure un messaggio di errore
     */
        public List<String> getAllConcorrentiForTeam(int teamId, int hackathonId) {
            try {

                List<String> concorrentiDAO = utenteDAO.getAllConcorrentiForTeam(teamId, hackathonId);

                String nomeTeam = teamDAO.getNomeTeamById(teamId);
                Team team = new Team(teamId, nomeTeam != null ? nomeTeam : "Team_" + teamId);

                for (String fullName : concorrentiDAO) {
                    String[] parts = fullName.split(" ", 2);
                    String nome = parts.length > 0 ? parts[0] : "";
                    String cognome = parts.length > 1 ? parts[1] : "";

                    Concorrente c = new Concorrente(0, nome, cognome);
                    team.aggiungiConcorrente(c);
                }

                return team.getConcorrenti().stream()
                        .map(c -> c.getNome() + " " + c.getCognome())
                        .toList();

            } catch (Exception e) {
                return List.of("Errore durante la ricerca dei concorrenti per il team: " + e.getMessage());
            }
        }


    /**
     * Esegue il login di un utente verificando email e password.
     *
     * @param email email dell’utente
     * @param password password dell’utente in chiaro
     * @return l’ID dell’utente se le credenziali sono corrette, altrimenti -1
     */
        public int login (String email, String password){
            try {
                return utenteDAO.checkLoginAndGetId(email, password);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore durante il login per email={}" + email, e);
                return -1;
            }
        }

        /**
     * Recupera il tipo di un utente dato il suo ID.
     *
     * @param userId ID dell’utente
     * @return tipo dell’utente ({@code concorrente}, {@code giudice}, {@code organizzatore}) oppure null in caso di errore
     */
        public String getUserType ( int userId){
            try {
                return utenteDAO.getUserTypeById(userId);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore durante il recupero del tipo utente per userId=" + userId, e);
                return null;
            }
        }


    /**
     * Recupera l’ID dell’hackathon a cui è associato un utente.
     *
     * @param userId ID dell’utente
     * @return ID dell’hackathon se trovato, altrimenti -1
     */
        public int getHackathonIdByUser ( int userId){
            try {
                return utenteDAO.getHackathonIdByUser(userId);
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Errore durante il recupero hackathonId per userId=" + userId, e);
                return -1;
            }
        }

        /**
     * Salva una valutazione assegnata da un giudice a un team.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Verifica l’esistenza del team e del giudice</li>
     *     <li>Istanzia gli oggetti di dominio {@link Team}, {@link Giudice} e {@link Valutazione}</li>
     *     <li>Chiama il metodo {@link Giudice#valutaTeam(Valutazione)} per validare l’associazione</li>
     *     <li>Registra la valutazione nel database tramite {@code valutazioneDAO}</li>
     * </ul>
     *
     * @param teamId ID del team valutato
     * @param giudiceId ID del giudice che assegna la valutazione
     * @param punteggio punteggio assegnato (valore numerico)
     * @param feedback commento o osservazione del giudice
     * @return messaggio che indica l’esito del salvataggio

     */

    public String saveValutazione(int teamId, int giudiceId, int punteggio, String feedback) {

        try {
            String nomeTeam = teamDAO.getNomeTeamById(teamId);
            if (nomeTeam == null) {
                return "Errore: team non trovato.";
            }

            String giudiceNome = utenteDAO.getNomeGiudiceById(giudiceId);
            String giudiceCognome = utenteDAO.getCognomeGiudiceById(giudiceId);
            String giudiceEmail = utenteDAO.getEmailGiudiceById(giudiceId);
            if (giudiceNome == null || giudiceCognome == null || giudiceEmail == null) {
                return "Errore: giudice non trovato.";
            }

            List<Integer> documentoIds = documentoDAO.getIdsByTeamId(teamId);

            if (documentoIds == null || documentoIds.isEmpty()) {
                return "Errore: Il team '" + teamId + ";" + nomeTeam + "' non ha ancora aggiunto alcun documento e non può essere valutato.";
            }

            Team team = new Team(teamId, nomeTeam);
            boolean hasAnyAgg = false;
            for (Integer docId : documentoIds) {
                if (docId == null) continue;

                String titolo = documentoDAO.getTitoloById(docId);
                Documento doc = new Documento(docId, titolo);

                boolean docHasAgg = aggiornamentoDAO.existsByDocumentoId(docId);

                if (docHasAgg) {
                    hasAnyAgg = true;

                    try {

                        Aggiornamento fakeAgg = new Aggiornamento(team, doc, "agg-placeholder");

                        doc.aggiungiAggiornamento(fakeAgg);
                        team.aggiungiDocumento(doc);
                    }  catch (Exception ignored) {}
                } else {
                    try {
                        team.aggiungiDocumento(doc);
                    } catch (Exception ignored) {}
                }
            }

            if (!hasAnyAgg) {
                return "Errore: Il team '" + teamId + ";" + nomeTeam + "' non ha ancora aggiunto alcun aggiornamento e non può essere valutato.";
            }


            Giudice giudice;
            try {
                giudice = new Giudice(giudiceId, giudiceNome, giudiceCognome, giudiceEmail);
            } catch (model.exceptions.InvalidDataException e) {
                return "Errore nella creazione del giudice: " + e.getMessage();
            }


            Valutazione valutazione;
            try {
                valutazione = new Valutazione(team, giudice, punteggio, feedback);
                giudice.valutaTeam(valutazione); // azione di business nel model
            } catch (model.exceptions.InvalidDataException e) {
                return "Errore: " + e.getMessage();
            }


            try {
                valutazioneDAO.save(teamId, giudiceId, punteggio, feedback);
            } catch (SQLException e) {
                return "Errore durante il salvataggio della valutazione: " + e.getMessage();
            }

            return "Valutazione salvata con successo per il team '" + nomeTeam + "' dal giudice '" + giudiceNome + " " + giudiceCognome + "'.";
        } catch (SQLException e) {
            return "Errore DB: " + e.getMessage();
        } catch (Exception e) {

            return "Errore inatteso: " + e.getMessage();
        }
    }

    /**
     * Recupera tutte le valutazioni associate a un determinato team.
     *
     * <p>Il metodo:
     * <ul>
     *     <li>Ottiene le valutazioni dal database in formato raw tramite {@code valutazioneDAO}</li>
     *     <li>Istanzia oggetti {@link Valutazione} per ogni record</li>
     *     <li>Collega le valutazioni al team corrispondente</li>
     *     <li>Restituisce un elenco testuale con nome del giudice, punteggio e feedback</li>
     * </ul>
     *
     * @param teamId ID del team
     * @return lista di stringhe che descrivono le valutazioni, oppure un messaggio di errore
     */

    public List<String> getValutazioniByTeam(int teamId) {
        List<String> output = new ArrayList<>();
        try {
            List<String> rawRows = valutazioneDAO.getAllByTeam(teamId);
            if (rawRows == null || rawRows.isEmpty()) {
                output.add("Nessuna valutazione trovata per il team con ID " + teamId);
                return output;
            }

            System.out.println("[DEBUG getValutazioniByTeam] rawRows size=" + rawRows.size());
            for (String r : rawRows) System.out.println("[DEBUG rawRow] \"" + r + "\"");

            String nomeTeam = "Team_" + teamId;
            try {
                String tn = teamDAO.getNomeTeamById(teamId);
                if (tn != null && !tn.isBlank()) nomeTeam = tn;
            } catch (Exception ignore) {
            }

            for (String raw : rawRows) {
                if (raw == null || raw.trim().isEmpty()) continue;

                String normalized = raw.trim();

                if (normalized.toLowerCase().contains("feedback:")) {

                    int idx = normalized.toLowerCase().lastIndexOf("feedback:");

                    String after = normalized.substring(idx + "feedback:".length()).trim();
                    String before = normalized.substring(0, idx).trim();

                    normalized = before + " - " + after;
                } else {

                    String lower = normalized.toLowerCase();
                    if ((lower.contains("giudice id") || lower.contains("punteggio")) &&
                            (lower.startsWith("id") || lower.contains("id:") || lower.contains("giudice id"))) {

                        normalized = normalized.replaceAll("(?i)\\bID\\b", "")
                                .replaceAll("(?i)Giudice ID", "Giudice ID")
                                .replaceAll("(?i)Punteggio", "Punteggio")
                                .replaceAll("(?i)Feedback", "Feedback")
                                .replaceAll("[:]+", ":")
                                .replaceAll("\\s{2,}", " ").trim();
                    }
                }

                Integer valutazioneId = null;
                Integer giudiceId = null;
                Integer punteggio = null;
                String feedback = "";

                if (normalized.toLowerCase().contains("feedback:")) {
                    int idx = normalized.toLowerCase().lastIndexOf("feedback:");
                    feedback = normalized.substring(idx + "feedback:".length()).trim();
                    normalized = normalized.substring(0, idx).trim();
                }

                java.util.regex.Matcher mAll = java.util.regex.Pattern.compile("\\d+").matcher(normalized);
                List<Integer> nums = new ArrayList<>();
                while (mAll.find()) {
                    try {
                        nums.add(Integer.parseInt(mAll.group()));
                    } catch (NumberFormatException ignored) {
                    }
                }

                if (!nums.isEmpty()) {
                    if (nums.size() >= 3) {
                        valutazioneId = nums.get(0);
                        giudiceId = nums.get(1);
                        punteggio = nums.get(2);
                    } else if (nums.size() == 2) {
                        giudiceId = nums.get(0);
                        punteggio = nums.get(1);
                    } else
                        giudiceId = nums.getFirst();

                }

                if (feedback.isBlank()) {

                    String tmp = normalized;
                    if (valutazioneId != null) tmp = tmp.replaceFirst("\\b" + valutazioneId + "\\b", "");
                    if (giudiceId != null) tmp = tmp.replaceFirst("\\b" + giudiceId + "\\b", "");
                    if (punteggio != null) tmp = tmp.replaceFirst("\\b" + punteggio + "\\b", "");
                    feedback = tmp.replaceAll("[-:,]+", " ").trim();
                    if (feedback.isEmpty() && feedback.matches("^\\W*$")) feedback = "";
                }

                String giudiceNome = null;
                String giudiceCognome = null;
                if (giudiceId != null && giudiceId > 0) {
                    try {
                        giudiceNome = utenteDAO.getNomeGiudiceById(giudiceId);
                        giudiceCognome = utenteDAO.getCognomeGiudiceById(giudiceId);
                    } catch (Exception ex) {
                        System.out.println("[DEBUG] utenteDAO.getNomeGiudiceById errore per id=" + giudiceId + ": " + ex.getMessage());
                    }
                }

                String giudiceDisplay;
                if (giudiceNome != null && !giudiceNome.isBlank()) {
                    giudiceDisplay = giudiceNome + (giudiceCognome != null ? " " + giudiceCognome : "");
                } else if (giudiceId != null && giudiceId > 0) {
                    giudiceDisplay = "Giudice id:" + giudiceId;
                } else {
                    giudiceDisplay = "Giudice sconosciuto";
                }

                String punteggioDisplay = (punteggio != null) ? String.valueOf(punteggio) : "N/A";
                String feedbackDisplay = (!feedback.isBlank()) ? feedback : "-";

                String line = String.format("Giudice: %s - Punteggio: %s - Feedback: %s",
                        giudiceDisplay, punteggioDisplay, feedbackDisplay);

                System.out.println("[DEBUG parsed] valutazioneId=" + valutazioneId + " giudiceId=" + giudiceId +
                        " punteggio=" + punteggio + " feedback='" + feedback + "' -> " + line);

                output.add(line);
            }

            return output;
        } catch (Exception e) {

            return List.of("Errore durante la visualizzazione delle valutazioni: " + e.getMessage());
        }
    }

}
