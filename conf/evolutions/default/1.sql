/*
Navicat PGSQL Data Transfer

Source Server         : pr4
Source Server Version : 90405
Source Host           : ec2-107-21-221-59.compute-1.amazonaws.com:5432
Source Database       : d77j52sak69uor
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90405
File Encoding         : 65001

Date: 2015-11-18 11:58:57
*/

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

-- ----------------------------
-- Table structure for comentario
-- ----------------------------
CREATE TABLE "public"."comentario" (
"id" int8 DEFAULT nextval('"Comentario_id_seq"'::regclass) NOT NULL,
"data" date NOT NULL,
"mensagem" varchar(255) COLLATE "default" NOT NULL,
"user_id" int4 NOT NULL,
"projecto_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for componente
-- ----------------------------
CREATE TABLE "public"."componente" (
"id" int4 DEFAULT nextval('"Componente_id_seq"'::regclass) NOT NULL,
"conteudo" text COLLATE "default" NOT NULL,
"tipo_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ficheiro
-- ----------------------------
CREATE TABLE "public"."ficheiro" (
"id" int4 DEFAULT nextval('"Ficheiro_id_seq"'::regclass) NOT NULL,
"nome" varchar(255) COLLATE "default" NOT NULL,
"ficheiro" bytea NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ficheiro_tag
-- ----------------------------
CREATE TABLE "public"."ficheiro_tag" (
"tag_id" int4 NOT NULL,
"ficheiro_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for ligacao
-- ----------------------------
CREATE TABLE "public"."ligacao" (
"id" int4 DEFAULT nextval('ligacao_id_seq'::regclass) NOT NULL,
"titulo" varchar(128) COLLATE "default" NOT NULL,
"link" varchar(128) COLLATE "default" NOT NULL,
"descricao" varchar(256) COLLATE "default",
"versaoprojecto_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for projecto
-- ----------------------------
CREATE TABLE "public"."projecto" (
"id" int2 DEFAULT nextval('"Projecto_id_seq"'::regclass) NOT NULL,
"nome" varchar(255) COLLATE "default" NOT NULL,
"descricao" varchar(255) COLLATE "default",
"user_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
CREATE TABLE "public"."tag" (
"id" int4 DEFAULT nextval('"Tag_id_seq"'::regclass) NOT NULL,
"nome" varchar(255) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for tipo
-- ----------------------------
CREATE TABLE "public"."tipo" (
"id" int4 NOT NULL,
"nome" varchar(255) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for versaoprojecto
-- ----------------------------
CREATE TABLE "public"."versaoprojecto" (
"id" int4 DEFAULT nextval('"VersaoProjecto_id_seq"'::regclass) NOT NULL,
"descricao" text COLLATE "default" NOT NULL,
"user_id" int4 NOT NULL,
"projecto_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for versaoprojecto_componente
-- ----------------------------
DROP TABLE IF EXISTS "public"."versaoprojecto_componente";
CREATE TABLE "public"."versaoprojecto_componente" (
"versaoprojecto_id" int4 NOT NULL,
"componente_id" int4 NOT NULL
)
WITH (OIDS=FALSE)

;

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

-- ----------------------------
-- Uniques structure for table comentario
-- ----------------------------
ALTER TABLE "public"."comentario" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table comentario
-- ----------------------------
ALTER TABLE "public"."comentario" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table componente
-- ----------------------------
ALTER TABLE "public"."componente" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table componente
-- ----------------------------
ALTER TABLE "public"."componente" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ficheiro
-- ----------------------------
ALTER TABLE "public"."ficheiro" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ligacao
-- ----------------------------
ALTER TABLE "public"."ligacao" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table projecto
-- ----------------------------
ALTER TABLE "public"."projecto" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table tag
-- ----------------------------
ALTER TABLE "public"."tag" ADD UNIQUE ("nome");
ALTER TABLE "public"."tag" ADD UNIQUE ("id");

-- ----------------------------
-- Primary Key structure for table tag
-- ----------------------------
ALTER TABLE "public"."tag" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tipo
-- ----------------------------
ALTER TABLE "public"."tipo" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table versaoprojecto
-- ----------------------------
ALTER TABLE "public"."versaoprojecto" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Key structure for table "public"."comentario"
-- ----------------------------
ALTER TABLE "public"."comentario" ADD FOREIGN KEY ("projecto_id") REFERENCES "public"."projecto" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."componente"
-- ----------------------------
ALTER TABLE "public"."componente" ADD FOREIGN KEY ("tipo_id") REFERENCES "public"."tipo" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."ficheiro_tag"
-- ----------------------------
ALTER TABLE "public"."ficheiro_tag" ADD FOREIGN KEY ("ficheiro_id") REFERENCES "public"."ficheiro" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."ficheiro_tag" ADD FOREIGN KEY ("tag_id") REFERENCES "public"."tag" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."ligacao"
-- ----------------------------
ALTER TABLE "public"."ligacao" ADD FOREIGN KEY ("versaoprojecto_id") REFERENCES "public"."versaoprojecto" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."versaoprojecto"
-- ----------------------------
ALTER TABLE "public"."versaoprojecto" ADD FOREIGN KEY ("projecto_id") REFERENCES "public"."projecto" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."versaoprojecto_componente"
-- ----------------------------
ALTER TABLE "public"."versaoprojecto_componente" ADD FOREIGN KEY ("versaoprojecto_id") REFERENCES "public"."versaoprojecto" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;
ALTER TABLE "public"."versaoprojecto_componente" ADD FOREIGN KEY ("componente_id") REFERENCES "public"."componente" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;


# --- !Downs
DROP SEQUENCE IF EXISTS "public"."Comentario_id_seq";
DROP SEQUENCE IF EXISTS "public"."Componente_id_seq";
DROP SEQUENCE IF EXISTS "public"."Ficheiro_id_seq";
DROP SEQUENCE IF EXISTS "public"."ligacao_id_seq";
DROP SEQUENCE IF EXISTS "public"."Projecto_id_seq";
DROP SEQUENCE IF EXISTS "public"."Tag_id_seq";
DROP SEQUENCE IF EXISTS "public"."VersaoProjecto_id_seq";
DROP TABLE IF EXISTS "public"."comentario";
DROP TABLE IF EXISTS "public"."componente";
DROP TABLE IF EXISTS "public"."ficheiro";
DROP TABLE IF EXISTS "public"."ficheiro_tag";
DROP TABLE IF EXISTS "public"."ligacao";
DROP TABLE IF EXISTS "public"."tag";
DROP TABLE IF EXISTS "public"."tipo";
DROP TABLE IF EXISTS "public"."versaoprojecto";
DROP TABLE IF EXISTS "public"."versaoprojecto_componente";