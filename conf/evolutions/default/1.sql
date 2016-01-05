# --- !Ups

CREATE TABLE comentario
(
 id BIGINT PRIMARY KEY NOT NULL,
 data TIMESTAMP NOT NULL,
 mensagem VARCHAR(255) NOT NULL,
 user_id VARCHAR(255) NOT NULL,
 projecto_id INTEGER NOT NULL
);
CREATE TABLE componente
(
 id INTEGER PRIMARY KEY NOT NULL,
 conteudo TEXT NOT NULL,
 tipo_id INTEGER NOT NULL
);
CREATE TABLE ficheiro
(
 id INTEGER PRIMARY KEY NOT NULL,
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
 id INTEGER PRIMARY KEY NOT NULL,
 titulo VARCHAR(128) NOT NULL,
 link VARCHAR(128) NOT NULL,
 descricao VARCHAR(256),
 versaoprojecto_id INTEGER NOT NULL
);
CREATE TABLE projecto
(
 id SMALLINT PRIMARY KEY NOT NULL,
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
 id INTEGER PRIMARY KEY NOT NULL,
 nome VARCHAR(255) NOT NULL
);
CREATE TABLE tipo
(
 id INTEGER PRIMARY KEY NOT NULL,
 nome VARCHAR(255) NOT NULL
);
CREATE TABLE versaoprojecto
(
 id INTEGER PRIMARY KEY NOT NULL,
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
ALTER TABLE ficheiro ADD FOREIGN KEY ("#21") REFERENCES projecto (id);
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
ALTER TABLE versaoprojecto_componente ADD FOREIGN KEY (componente_id) REFERENCES componente (id)CREATE FUNCTION delete_old_rows() RETURNS TRIGGER;

# --- !Downs

