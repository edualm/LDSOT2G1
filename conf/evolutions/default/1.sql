# --- !Ups


-- ----------------------------
-- Sequence structure for Comentario_id_seq
-- ----------------------------
CREATE SEQUENCE "public"."Comentario_id_seq"
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 8
CACHE 1;
SELECT setval('"public"."Comentario_id_seq"', 8, true);

-- ----------------------------
-- Sequence structure for Componente_id_seq
-- ----------------------------
CREATE SEQUENCE "public"."Componente_id_seq"
INCREMENT 1
MINVALUE 0
MAXVALUE 9223372036854775807
START 56
CACHE 1;
SELECT setval('"public"."Componente_id_seq"', 56, true);

-- ----------------------------
-- Sequence structure for Ficheiro_id_seq
-- ----------------------------
CREATE SEQUENCE "public"."Ficheiro_id_seq"
INCREMENT 1
MINVALUE 0
MAXVALUE 9223372036854775807
START 5
CACHE 1;
SELECT setval('"public"."Ficheiro_id_seq"', 5, true);

-- ----------------------------
-- Sequence structure for ligacao_id_seq
-- ----------------------------
CREATE SEQUENCE "public"."ligacao_id_seq"
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for Projecto_id_seq
-- ----------------------------
CREATE SEQUENCE "public"."Projecto_id_seq"
INCREMENT 1
MINVALUE 0
MAXVALUE 9223372036854775807
START 12
CACHE 1;
SELECT setval('"public"."Projecto_id_seq"', 12, true);

-- ----------------------------
-- Sequence structure for Tag_id_seq
-- ----------------------------
CREATE SEQUENCE "public"."Tag_id_seq"
INCREMENT 1
MINVALUE 0
MAXVALUE 9223372036854775807
START 0
CACHE 1;

-- ----------------------------
-- Sequence structure for VersaoProjecto_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."VersaoProjecto_id_seq";
CREATE SEQUENCE "public"."VersaoProjecto_id_seq"
INCREMENT 1
MINVALUE 0
MAXVALUE 9223372036854775807
START 23
CACHE 1;
SELECT setval('"public"."VersaoProjecto_id_seq"', 23, true);



CREATE TABLE comentario
(
 id BIGINT DEFAULT nextval('"Comentario_id_seq"'::regclass) NOT NULL,
 data TIMESTAMP NOT NULL,
 mensagem VARCHAR(255) NOT NULL,
 user_id VARCHAR(255) NOT NULL,
 projecto_id INTEGER NOT NULL
);
CREATE TABLE componente
(
 id INTEGER DEFAULT nextval('"Componente_id_seq"'::regclass) NOT NULL,
 conteudo TEXT NOT NULL,
 tipo_id INTEGER NOT NULL
);
CREATE TABLE ficheiro
(
 id INTEGER DEFAULT nextval('"Ficheiro_id_seq"'::regclass) NOT NULL,
 nome VARCHAR(255) NOT NULL,
 ficheiro BYTEA NOT NULL,
 projecto_id INTEGER
);
CREATE TABLE ficheiro_tag
(
 tag_id INTEGER NOT NULL,
 ficheiro_id INTEGER NOT NULL
);
CREATE TABLE ligacao
(
 id INTEGER DEFAULT nextval('ligacao_id_seq'::regclass) NOT NULL,
 titulo VARCHAR(128) NOT NULL,
 link VARCHAR(128) NOT NULL,
 descricao VARCHAR(256),
 versaoprojecto_id INTEGER NOT NULL
);
CREATE TABLE projecto
(
 id SMALLINT DEFAULT nextval('"Projecto_id_seq"'::regclass) NOT NULL,
 nome VARCHAR(255) NOT NULL,
 descricao VARCHAR(255),
 user_id VARCHAR(255) NOT NULL,
 imagem BYTEA
);
CREATE TABLE projecto_tag
(
 projecto BIGINT,
 tag BIGINT
);
CREATE TABLE sessions
(
 username VARCHAR(255) NOT NULL,
 token TEXT NOT NULL,
 expires TIMESTAMP NOT NULL
);
CREATE TABLE tag
(
 id INTEGER DEFAULT nextval('"Tag_id_seq"'::regclass) NOT NULL,
 nome VARCHAR(255) NOT NULL
);
CREATE TABLE tipo
(
 id INTEGER PRIMARY KEY NOT NULL,
 nome VARCHAR(255) NOT NULL
);
CREATE TABLE versaoprojecto
(
 id INTEGER DEFAULT nextval('"VersaoProjecto_id_seq"'::regclass) NOT NULL,
 descricao TEXT NOT NULL,
 user_id VARCHAR(255) NOT NULL,
 projecto_id INTEGER NOT NULL,
 data TIMESTAMP
);
CREATE TABLE versaoprojecto_componente
(
 versaoprojecto_id INTEGER NOT NULL,
 componente_id INTEGER NOT NULL
);

ALTER TABLE comentario ADD FOREIGN KEY (projecto_id) REFERENCES projecto (id);

CREATE UNIQUE INDEX comentario_id_key ON comentario (id);

ALTER TABLE componente ADD FOREIGN KEY (tipo_id) REFERENCES tipo (id);

CREATE UNIQUE INDEX componente_id_key ON componente (id);

ALTER TABLE ficheiro ADD FOREIGN KEY (projecto_id) REFERENCES projecto (id);
ALTER TABLE ficheiro_tag ADD FOREIGN KEY (tag_id) REFERENCES tag (id);
ALTER TABLE ficheiro_tag ADD FOREIGN KEY (ficheiro_id) REFERENCES ficheiro (id);
ALTER TABLE ligacao ADD FOREIGN KEY (versaoprojecto_id) REFERENCES versaoprojecto (id);

CREATE UNIQUE INDEX projecto_id_key ON projecto (id);

ALTER TABLE projecto_tag ADD FOREIGN KEY (projecto) REFERENCES projecto (id);
ALTER TABLE projecto_tag ADD FOREIGN KEY (tag) REFERENCES tag (id);

CREATE UNIQUE INDEX projecto_tag_pkey ON projecto_tag (projecto, tag);
CREATE UNIQUE INDEX projecto_tag_unique ON projecto_tag (projecto, tag);
CREATE UNIQUE INDEX tag_id_key ON tag (id);
CREATE UNIQUE INDEX tag_nome_key ON tag (nome);

ALTER TABLE versaoprojecto ADD FOREIGN KEY (projecto_id) REFERENCES projecto (id);
ALTER TABLE versaoprojecto_componente ADD FOREIGN KEY (versaoprojecto_id) REFERENCES versaoprojecto (id);
ALTER TABLE versaoprojecto_componente ADD FOREIGN KEY (componente_id) REFERENCES componente (id);


-- ----------------------------
-- Alter Sequences Owned By
-- ----------------------------
ALTER SEQUENCE "public"."Comentario_id_seq" OWNED BY "comentario"."id";
ALTER SEQUENCE "public"."Componente_id_seq" OWNED BY "componente"."id";
ALTER SEQUENCE "public"."Ficheiro_id_seq" OWNED BY "ficheiro"."id";
ALTER SEQUENCE "public"."ligacao_id_seq" OWNED BY "ligacao"."id";
ALTER SEQUENCE "public"."Projecto_id_seq" OWNED BY "projecto"."id";
ALTER SEQUENCE "public"."Tag_id_seq" OWNED BY "tag"."id";
ALTER SEQUENCE "public"."VersaoProjecto_id_seq" OWNED BY "versaoprojecto"."id";


CREATE FUNCTION delete_old_rows() RETURNS trigger
LANGUAGE plpgsql
AS
$$
BEGIN
 DELETE FROM "public"."sessions" WHERE expires < (NOW() - INTERVAL '2 hour');;
RETURN NULL;;
END;;
$$;

CREATE TRIGGER trigger_delete_old_rows
AFTER INSERT OR DELETE ON "public"."sessions"
FOR EACH ROW
EXECUTE PROCEDURE delete_old_rows();

# --- !Downs

