--
-- PostgreSQL database dump
--

-- Started on 2013-01-30 16:01:54 EST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 1823 (class 1262 OID 16385)
-- Name: mosaic; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE mosaic WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.UTF-8' LC_CTYPE = 'en_US.UTF-8';


\connect mosaic

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 147 (class 1259 OID 16470)
-- Dependencies: 3
-- Name: accounts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE accounts (
    id bigint NOT NULL,
    identity text NOT NULL,
    firstname text,
    lastname text,
    email text,
    gender text,
    language text
);


--
-- TOC entry 146 (class 1259 OID 16468)
-- Dependencies: 3 147
-- Name: accounts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE accounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1826 (class 0 OID 0)
-- Dependencies: 146
-- Name: accounts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE accounts_id_seq OWNED BY accounts.id;


--
-- TOC entry 141 (class 1259 OID 16422)
-- Dependencies: 1797 1798 1799 3
-- Name: messages; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE messages (
    id bigint NOT NULL,
    body text NOT NULL,
    created timestamp without time zone NOT NULL,
    latitude integer DEFAULT 0 NOT NULL,
    longitude integer DEFAULT 0 NOT NULL,
    radius integer DEFAULT 0 NOT NULL,
    expiry timestamp without time zone NOT NULL
);


--
-- TOC entry 140 (class 1259 OID 16420)
-- Dependencies: 3 141
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1827 (class 0 OID 0)
-- Dependencies: 140
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;


--
-- TOC entry 149 (class 1259 OID 16484)
-- Dependencies: 3
-- Name: nonces; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE nonces (
    id bigint NOT NULL,
    nonce text NOT NULL
);


--
-- TOC entry 148 (class 1259 OID 16482)
-- Dependencies: 3 149
-- Name: nonces_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE nonces_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1828 (class 0 OID 0)
-- Dependencies: 148
-- Name: nonces_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE nonces_id_seq OWNED BY nonces.id;


--
-- TOC entry 145 (class 1259 OID 16453)
-- Dependencies: 1803 1804 3
-- Name: visits; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE visits (
    id bigint NOT NULL,
    message bigint NOT NULL,
    account bigint NOT NULL,
    count integer DEFAULT 0 NOT NULL,
    reported boolean DEFAULT false NOT NULL
);


--
-- TOC entry 144 (class 1259 OID 16451)
-- Dependencies: 3 145
-- Name: visits_account_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE visits_account_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1829 (class 0 OID 0)
-- Dependencies: 144
-- Name: visits_account_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE visits_account_seq OWNED BY visits.account;


--
-- TOC entry 142 (class 1259 OID 16447)
-- Dependencies: 145 3
-- Name: visits_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE visits_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1830 (class 0 OID 0)
-- Dependencies: 142
-- Name: visits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE visits_id_seq OWNED BY visits.id;


--
-- TOC entry 143 (class 1259 OID 16449)
-- Dependencies: 3 145
-- Name: visits_message_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE visits_message_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1831 (class 0 OID 0)
-- Dependencies: 143
-- Name: visits_message_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE visits_message_seq OWNED BY visits.message;


--
-- TOC entry 1805 (class 2604 OID 16473)
-- Dependencies: 146 147 147
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY accounts ALTER COLUMN id SET DEFAULT nextval('accounts_id_seq'::regclass);


--
-- TOC entry 1796 (class 2604 OID 16425)
-- Dependencies: 140 141 141
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);


--
-- TOC entry 1806 (class 2604 OID 16487)
-- Dependencies: 149 148 149
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY nonces ALTER COLUMN id SET DEFAULT nextval('nonces_id_seq'::regclass);


--
-- TOC entry 1800 (class 2604 OID 16456)
-- Dependencies: 142 145 145
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY visits ALTER COLUMN id SET DEFAULT nextval('visits_id_seq'::regclass);


--
-- TOC entry 1801 (class 2604 OID 16457)
-- Dependencies: 145 143 145
-- Name: message; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY visits ALTER COLUMN message SET DEFAULT nextval('visits_message_seq'::regclass);


--
-- TOC entry 1802 (class 2604 OID 16458)
-- Dependencies: 145 144 145
-- Name: account; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY visits ALTER COLUMN account SET DEFAULT nextval('visits_account_seq'::regclass);


--
-- TOC entry 1812 (class 2606 OID 16475)
-- Dependencies: 147 147
-- Name: pk_accounts; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY accounts
    ADD CONSTRAINT pk_accounts PRIMARY KEY (id);


--
-- TOC entry 1808 (class 2606 OID 16433)
-- Dependencies: 141 141
-- Name: pk_messages; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT pk_messages PRIMARY KEY (id);


--
-- TOC entry 1816 (class 2606 OID 16492)
-- Dependencies: 149 149
-- Name: pk_nonces; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY nonces
    ADD CONSTRAINT pk_nonces PRIMARY KEY (id);


--
-- TOC entry 1810 (class 2606 OID 16462)
-- Dependencies: 145 145
-- Name: pk_visits; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY visits
    ADD CONSTRAINT pk_visits PRIMARY KEY (id);


--
-- TOC entry 1814 (class 2606 OID 16499)
-- Dependencies: 147 147
-- Name: unique_identity; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY accounts
    ADD CONSTRAINT unique_identity UNIQUE (identity);


--
-- TOC entry 1818 (class 2606 OID 16497)
-- Dependencies: 149 149
-- Name: unique_nonce; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY nonces
    ADD CONSTRAINT unique_nonce UNIQUE (nonce);


--
-- TOC entry 1820 (class 2606 OID 16477)
-- Dependencies: 1811 147 145
-- Name: fk_accounts; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY visits
    ADD CONSTRAINT fk_accounts FOREIGN KEY (account) REFERENCES accounts(id);


--
-- TOC entry 1819 (class 2606 OID 16463)
-- Dependencies: 141 1807 145
-- Name: fk_messages; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY visits
    ADD CONSTRAINT fk_messages FOREIGN KEY (message) REFERENCES messages(id);


--
-- TOC entry 1825 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-01-30 16:01:54 EST

--
-- PostgreSQL database dump complete
--

