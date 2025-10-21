
package model;

import model.exceptions.InvalidDataException;
import model.exceptions.RegistrazioneScadutaException;

import model.utils.ValidationUtils;
import security.PasswordSecurity;

import java.time.LocalDate;


/**
 * Classe base che rappresenta un utente del sistema Hackathon.
 * <p>
 * Ogni utente è identificato da un {@code id} univoco e possiede dati anagrafici
 * (nome, cognome, email), una password memorizzata in forma di hash,
 * una data di registrazione e un eventuale {@link Hackathon} scelto.
 * </p>
 * <p>
 * La classe è pensata per essere estesa da {@link Concorrente}, {@link Giudice} e {@link Organizzatore},
 * che aggiungono comportamenti e attributi specifici.
 * </p>
 */
public class Utente {
    /** Identificativo univoco dell'utente nel sistema (immutabile). */
    protected final int id;
    /** Nome proprio dell'utente. */
    protected String nome;
    /** Cognome dell'utente. Non può essere nullo o vuoto. */
    protected String cognome;
    /** Indirizzo email univoco e valido dell'utente, utilizzato anche per il login. */
    protected String email;
    /** Hash sicuro della password dell'utente, generato tramite {@link PasswordSecurity}. */
    protected String passwordHash;
    /** Data della registrazione dell'utente nel sistema. */
    protected LocalDate dataRegistrazione;
    /**
     * Hackathon scelto dall'utente.
     * <ul>
     *   <li>Per {@link Concorrente} e {@link Giudice} indica l’evento a cui partecipano.</li>
     *   <li>Per {@link Organizzatore} è sempre {@code null}.</li>
     * </ul>
     */
    protected Hackathon hackathonScelto;

    /**
     * Costruisce un nuovo utente con i dati specificati.
     *
     * @param id                identificativo univoco dell'utente
     * @param nome              nome dell'utente
     * @param cognome           cognome dell'utente
     * @param email             indirizzo email valido dell'utente
     * @param plainPassword     password in chiaro, che verrà convertita in hash
     * @param dataRegistrazione data di registrazione; se {@code null} viene impostata a oggi
     * @param hackathonScelto   hackathon scelto dall’utente (non ammesso per gli {@link Organizzatore})
     * @throws InvalidDataException se uno qualsiasi dei dati forniti non è valido
     * @throws RegistrazioneScadutaException se le registrazioni per l’hackathon sono chiuse
     */
    public Utente( int id,String nome, String cognome, String email,
                   String plainPassword, LocalDate dataRegistrazione,
                   Hackathon hackathonScelto) throws InvalidDataException,RegistrazioneScadutaException {
        this.id = id;
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        setPassword(plainPassword);
        this.dataRegistrazione = dataRegistrazione != null ? dataRegistrazione : LocalDate.now();
        setHackathonScelto(hackathonScelto);
    }

    /**
     * Costruttore che può essere ereditato da Organizzatore (hackathonScelto è null perchè l'organizzatore crea hackathon,non ci si iscrive.
     *
     * @param id                identificativo univoco dell'utente
     * @param nome              nome dell'utente
     * @param cognome           cognome dell'utente
     * @param email             indirizzo email valido dell'utente
     * @param plainPassword     password in chiaro, che verrà convertita in hash
     * @param dataRegistrazione data di registrazione; se {@code null} viene impostata a oggi
     * @throws InvalidDataException se uno qualsiasi dei dati forniti non è valido
     */
    public Utente(int id, String nome, String cognome, String email,
                  String plainPassword, LocalDate dataRegistrazione) throws InvalidDataException {
        this.id = id;
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        setPassword(plainPassword);
        this.dataRegistrazione = dataRegistrazione != null ? dataRegistrazione : LocalDate.now();
        this.hackathonScelto = null; // Organizzatore non ha hackathon
    }

    /**
     * Costruttore protetto usato dalle sottoclassi quando serve una
     * rappresentazione "leggera" dell'utente (ad esempio per visualizzazioni
     * o per creare oggetti in memoria senza la password).
     *
     * @param id identificativo univoco dell'utente
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     *@param email email dell'utente
     * @throws InvalidDataException se uno dei dati non è valido
     */
    protected Utente(int id, String nome, String cognome, String email) throws InvalidDataException {
        this.id = id;
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
        this.passwordHash = null;
        this.dataRegistrazione = null;
        this.hackathonScelto = null;
    }


    /**
     * Restituisce l'identificativo univoco dell'utente.
     *
     * @return identificativo univoco dell'utente
     */
    public int getId() { return id; }
    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome dell'utente
     */
    public String getNome() { return nome; }
    /**
     * Restituisce il cognome dell'utente.
     *
     * @return cognome dell'utente
     */
    public String getCognome() { return cognome; }
    /**
     * Restituisce l'email dell'utente.
     *
     * @return e-mail dell'utente
     */
    public String getEmail() { return email; }
    /**
     * Restituisce la password hashata dell'utente.
     *
     * @return password hashata dell'utente
     */
    public String getPasswordHash() { return passwordHash; }
    /**
     * Restituisce la data di registrazione dell'utente dell'utente.
     *
     * @return data di registrazione dell'utente
     */
    public LocalDate getDataRegistrazione() { return dataRegistrazione; }
    /**
     * Restituisce l'hackathon scelto dall'utente.
     *
     * @return hackathon scelto dall'utente, o {@code null} se non impostato
     */
    public Hackathon getHackathonScelto() { return hackathonScelto; }



    /**
     * Imposta il nome dell'utente.
     *
     * @param nome nome
     * @throws InvalidDataException se il nome è nullo o vuoto
     */
    public void setNome(String nome) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(nome, "Nome");
        this.nome = nome.trim();
    }
    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome cognome
     * @throws InvalidDataException se il cognome è nullo o vuoto
     */
    public void setCognome(String cognome) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(cognome, "Cognome");
        this.cognome = cognome.trim();
    }
    /**
     * Imposta l'email dell'utente.
     *
     * @param email nuovo indirizzo email
     * @throws InvalidDataException se l'email non è valida
     */
    public void setEmail(String email) throws InvalidDataException {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new InvalidDataException("Email non valida: " + email);
        }
        this.email = email.trim().toLowerCase();
    }
    /**
     * Imposta la password dell'utente, salvandola in forma di hash.
     *
     * @param plainPassword password in chiaro
     * @throws InvalidDataException se la password è nulla o vuota
     */
    public void setPassword(String plainPassword) throws InvalidDataException {
        ValidationUtils.validateNotEmpty(plainPassword, "Password");
        this.passwordHash = PasswordSecurity.hashPassword(plainPassword);
    }




    /**
     * Imposta la data di registrazione.
     *
     * @param dataRegistrazione nuova data
     * @throws InvalidDataException se la data è nulla o futura
     */
    public void setDataRegistrazione(LocalDate dataRegistrazione) throws InvalidDataException {
        if (dataRegistrazione == null || dataRegistrazione.isAfter(LocalDate.now())) {
            throw new InvalidDataException("Data di registrazione non valida");
        }
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * Imposta l'hackathon scelto dall'utente.
     *
     * @param hackathonScelto hackathon da associare
     * @throws InvalidDataException se l'utente è un {@link Organizzatore}
     * @throws RegistrazioneScadutaException se le registrazioni dell'hackathon sono chiuse
     */
    public void setHackathonScelto(Hackathon hackathonScelto)
            throws InvalidDataException, RegistrazioneScadutaException {
        if (this instanceof Organizzatore) {
            throw new InvalidDataException("Un organizzatore non può avere un hackathon scelto.");
        }
        if (hackathonScelto != null && !hackathonScelto.isStatoRegistrazioni()) {
            throw new RegistrazioneScadutaException(
                    "Le registrazioni per l'hackathon '" + hackathonScelto.getNome() + "' sono chiuse."
            );
        }
        this.hackathonScelto = hackathonScelto;
    }


    /**
     * Restituisce una rappresentazione testuale dell'utente,
     * con nome, cognome, email e hackathon associato.
     *
     * @return stringa rappresentativa dell'utente
     */
    @Override
    public String toString() {
        return String.format("Utente{nome='%s', cognome='%s', email='%s', dataRegistrazione=%s, hackathon='%s'}",
                nome, cognome, email, dataRegistrazione,
                hackathonScelto != null ? hackathonScelto.getNome() : "Nessuno");
    }
}

