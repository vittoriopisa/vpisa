-- we don't know how to generate root <with-no-name> (class Root) :(

comment on database postgres is 'default administrative connection database';

grant connect, create, temporary on database progetto_hackathon to vpisa;

create table hackathon
(
    id                  serial
        primary key,
    nome                varchar(255) not null,
    descrizione         text,
    luogo               varchar(255) not null,
    data_inizio         date         not null,
    data_fine           date         not null,
    stato_registrazioni boolean default false,
    organizzatore_id    integer
);

alter table hackathon
    owner to postgres;

create table team
(
    id           serial
        primary key,
    nome         varchar(255) not null,
    hackathon_id integer
        references hackathon
            on delete cascade,
    unique (nome, hackathon_id)
);

alter table team
    owner to postgres;

create index idx_team_hackathon
    on team (hackathon_id);

create table utente
(
    id                 serial
        primary key,
    nome               varchar(100) not null,
    cognome            varchar(100) not null,
    email              varchar(255) not null
        unique,
    password_hash      varchar(100) not null,
    data_registrazione date,
    tipo_utente        varchar(20)  not null
        constraint tipo_utente_check
            check (lower((tipo_utente)::text) = ANY
                   (ARRAY ['concorrente'::text, 'giudice'::text, 'organizzatore'::text])),
    hackathon_id       integer
                                    references hackathon
                                        on delete set null,
    team_id            integer
                                    references team
                                        on delete set null,
    updated_at         timestamp default CURRENT_TIMESTAMP,
    constraint chk_team_id_for_concorrenti
        check ((((tipo_utente)::text = 'concorrente'::text) AND (team_id IS NULL)) OR
               (((tipo_utente)::text = 'concorrente'::text) AND (team_id IS NOT NULL)) OR
               (((tipo_utente)::text <> 'concorrente'::text) AND (team_id IS NULL)))
);

alter table utente
    owner to postgres;

alter table hackathon
    add constraint fk_hackathon_organizzatore
        foreign key (organizzatore_id) references utente
            on delete set null;

create table problema
(
    id          serial
        primary key,
    titolo      varchar(255) not null,
    descrizione text         not null,
    team_id     integer
        constraint un_problema_per_team
            unique
                             references team
                                 on delete set null,
    giudice_id  integer      not null
        references utente
            on delete set null
);

alter table problema
    owner to postgres;

create index idx_utente_email
    on utente (email);

create index idx_utente_tipo
    on utente (tipo_utente);

create index idx_utente_hackathon
    on utente (hackathon_id);

create index idx_utente_team
    on utente (team_id);

create table documento
(
    id             serial
        primary key,
    titolo         varchar(255) not null,
    descrizione    text,
    data_creazione date,
    formato        varchar(50),
    dimensione     numeric(10, 2),
    tipo           varchar(100),
    team_id        integer
        references team
            on delete cascade
);

alter table documento
    owner to postgres;

create index idx_documento_team
    on documento (team_id);

create table valutazione
(
    id         serial
        primary key,
    team_id    integer
        references team
            on delete cascade,
    giudice_id integer
        references utente
            on delete cascade,
    punteggio  integer not null
        constraint valutazione_punteggio_check
            check ((punteggio >= 0) AND (punteggio <= 10))
        constraint punteggio_check
            check ((punteggio >= 1) AND (punteggio <= 10)),
    feedback   text
);

alter table valutazione
    owner to postgres;

create index idx_valutazione_team
    on valutazione (team_id);

create index idx_valutazione_giudice
    on valutazione (giudice_id);

create table commento
(
    id           serial
        primary key,
    documento_id integer
        references documento
            on delete cascade,
    giudice_id   integer
        references utente
            on delete cascade,
    testo        text not null
);

alter table commento
    owner to postgres;

create index idx_commento_documento
    on commento (documento_id);

create table aggiornamento
(
    id           serial
        primary key,
    team_id      integer
        references team
            on delete cascade,
    documento_id integer
                      references documento
                          on delete set null,
    contenuto    text not null
);

alter table aggiornamento
    owner to postgres;

create index idx_aggiornamento_team
    on aggiornamento (team_id);

create view utenti_giudici(id) as
SELECT id
FROM utente
WHERE tipo_utente::text = 'giudice'::text;

alter table utenti_giudici
    owner to postgres;

create function aggiorna_stato_registrazione() returns trigger
    language plpgsql
as
$$
BEGIN
    IF NEW.data_inizio <= CURRENT_DATE + INTERVAL '2 days' THEN
        NEW.stato_registrazione := FALSE;
    ELSE
        NEW.stato_registrazione := TRUE;
    END IF;
    RETURN NEW;
END;
$$;

alter function aggiorna_stato_registrazione() owner to postgres;

create function update_updated_at_column() returns trigger
    language plpgsql
as
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

alter function update_updated_at_column() owner to postgres;

create function check_giudice_id() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che l'utente con il giudice_id abbia tipo_utente = 'giudice'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.giudice_id
          AND tipo_utente = 'giudice'
    ) THEN
        RAISE EXCEPTION 'giudice_id deve riferirsi a un utente con tipo_utente = ''giudice''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_giudice_id() owner to postgres;

create function check_organizzatore_id() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che l'utente con l'organizzatore_id abbia tipo_utente = 'organizzatore'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.organizzatore_id
          AND tipo_utente = 'organizzatore'
    ) THEN
        RAISE EXCEPTION 'organizzatore_id deve riferirsi a un utente con tipo_utente = ''organizzatore''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_organizzatore_id() owner to postgres;

create function check_giudice_id_problema() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che l'utente con il giudice_id abbia tipo_utente = 'giudice'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.giudice_id
          AND tipo_utente = 'giudice'
    ) THEN
        RAISE EXCEPTION 'giudice_id deve riferirsi a un utente con tipo_utente = ''giudice''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_giudice_id_problema() owner to postgres;

create function check_giudice_id_valutazione() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che l'utente con il giudice_id abbia tipo_utente = 'giudice'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.giudice_id
          AND tipo_utente = 'giudice'
    ) THEN
        RAISE EXCEPTION 'giudice_id deve riferirsi a un utente con tipo_utente = ''giudice''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_giudice_id_valutazione() owner to postgres;

create function check_team_id_concorrente() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Se l'utente non è di tipo 'concorrente' e il team_id non è nullo, solleva un'eccezione
    IF NEW.tipo_utente <> 'concorrente' AND NEW.team_id IS NOT NULL THEN
        RAISE EXCEPTION 'Il campo team_id può essere non nullo solo per utenti di tipo "concorrente"';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_team_id_concorrente() owner to postgres;

create function check_team_concorrenti_limit() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Contiamo il numero di concorrenti nel team
    DECLARE
        num_concorrenti INT;
    BEGIN
        SELECT COUNT(*)
        INTO num_concorrenti
        FROM utente
        WHERE team_id = NEW.team_id
          AND tipo_utente = 'concorrente';

        -- Se il numero di concorrenti è maggiore o uguale a 6, solleva un'eccezione
        IF num_concorrenti >= 6 THEN
            RAISE EXCEPTION 'Un team può avere al massimo 6 concorrenti';
        END IF;

        RETURN NEW;
    END;
END;
$$;

alter function check_team_concorrenti_limit() owner to postgres;

create function check_stato_registrazioni() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica se la data_inizio è meno di 2 giorni dalla data corrente
    IF NEW.data_inizio < CURRENT_DATE + INTERVAL '2 days' THEN
        -- Imposta stato_registrazioni a false
        NEW.stato_registrazioni := false;
    END IF;

    RETURN NEW;
END;
$$;

alter function check_stato_registrazioni() owner to postgres;

create function check_team_id() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Se `tipo_utente` è 'concorrente', `team_id` deve essere non nullo
    IF NEW.tipo_utente = 'concorrente' AND NEW.team_id IS NULL THEN
        RAISE EXCEPTION 'team_id non può essere NULL per gli utenti di tipo "concorrente"';
        -- Se `tipo_utente` non è 'concorrente', `team_id` deve essere NULL
    ELSIF NEW.tipo_utente != 'concorrente' AND NEW.team_id IS NOT NULL THEN
        RAISE EXCEPTION 'team_id deve essere NULL per gli utenti che non sono "concorrente"';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_team_id() owner to postgres;

create function check_giudice_id_in_commento() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che il giudice_id esista e abbia tipo_utente = 'giudice'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.giudice_id
          AND tipo_utente = 'giudice'
    ) AND NEW.giudice_id IS NOT NULL THEN
        RAISE EXCEPTION 'giudice_id deve riferirsi a un utente con tipo_utente = ''giudice''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_giudice_id_in_commento() owner to postgres;

create trigger check_giudice_id_before_insert_or_update
    before insert or update
    on commento
    for each row
execute procedure check_giudice_id_in_commento();

create function check_organizzatore_id_in_hackathon() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che l'organizzatore_id esista e abbia tipo_utente = 'organizzatore'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.organizzatore_id
          AND tipo_utente = 'organizzatore'
    ) AND NEW.organizzatore_id IS NOT NULL THEN
        RAISE EXCEPTION 'organizzatore_id deve riferirsi a un utente con tipo_utente = ''organizzatore''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_organizzatore_id_in_hackathon() owner to postgres;

create trigger check_organizzatore_id_before_insert_or_update
    before insert or update
    on hackathon
    for each row
execute procedure check_organizzatore_id_in_hackathon();

create function check_giudice_id_in_problema() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che il giudice_id esista e abbia tipo_utente = 'giudice'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.giudice_id
          AND tipo_utente = 'giudice'
    ) AND NEW.giudice_id IS NOT NULL THEN
        RAISE EXCEPTION 'giudice_id deve riferirsi a un utente con tipo_utente = ''giudice''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_giudice_id_in_problema() owner to postgres;

create trigger check_giudice_id_before_insert_or_update
    before insert or update
    on problema
    for each row
execute procedure check_giudice_id_in_problema();

create function check_giudice_id_in_valutazione() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica che il giudice_id esista e abbia tipo_utente = 'giudice'
    IF NOT EXISTS (
        SELECT 1
        FROM utente
        WHERE id = NEW.giudice_id
          AND tipo_utente = 'giudice'
    ) AND NEW.giudice_id IS NOT NULL THEN
        RAISE EXCEPTION 'giudice_id deve riferirsi a un utente con tipo_utente = ''giudice''';
    END IF;
    RETURN NEW;
END;
$$;

alter function check_giudice_id_in_valutazione() owner to postgres;

create trigger check_giudice_id_before_insert_or_update
    before insert or update
    on valutazione
    for each row
execute procedure check_giudice_id_in_valutazione();

create function check_max_concorrenti_in_team() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica il numero di concorrenti nel team assegnato
    IF NEW.tipo_utente = 'concorrente' THEN
        -- Conta quanti concorrenti ci sono già nel team
        IF (SELECT COUNT(*) FROM utente WHERE team_id = NEW.team_id AND tipo_utente = 'concorrente') >= 6 THEN
            RAISE EXCEPTION 'Un team non può avere più di 6 concorrenti.';
        END IF;
    END IF;
    RETURN NEW;
END;
$$;

alter function check_max_concorrenti_in_team() owner to postgres;

create trigger check_max_concorrenti_before_insert_or_update
    before insert or update
    on utente
    for each row
execute procedure check_max_concorrenti_in_team();

create function check_stato_registrazioni_hackathon() returns trigger
    language plpgsql
as
$$
BEGIN
    -- Verifica se la data di inizio è meno di 2 giorni dalla data corrente
    IF NEW.data_inizio <= CURRENT_DATE + INTERVAL '2 days' AND NEW.data_inizio > CURRENT_DATE THEN
        NEW.stato_registrazioni := false;
        -- Se la data di inizio è già passata, impostiamo lo stato su false
    ELSIF NEW.data_inizio <= CURRENT_DATE THEN
        NEW.stato_registrazioni := false;
    END IF;

    RETURN NEW;
END;
$$;

alter function check_stato_registrazioni_hackathon() owner to postgres;

create trigger check_stato_registrazioni_before_insert_or_update
    before insert or update
    on hackathon
    for each row
execute procedure check_stato_registrazioni_hackathon();

