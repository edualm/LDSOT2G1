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
-- Foreign Key structure for table "comentario" with ON DELETE CASCADE
-- ----------------------------

ALTER TABLE comentario DROP CONSTRAINT comentario_projecto_id_fkey;
	
ALTER TABLE comentario
    ADD CONSTRAINT comentario_projecto_id_fkey
    FOREIGN KEY (projecto_id) REFERENCES projecto
	ON DELETE CASCADE 

-- ----------------------------
-- Foreign Key structure for table "versaoprojecto" with ON DELETE CASCADE
-- ----------------------------

ALTER TABLE versaoprojecto DROP CONSTRAINT versaoprojecto_projecto_id_fkey;

ALTER TABLE versaoprojecto
    ADD CONSTRAINT versaoprojecto_projecto_id_fkey
    FOREIGN KEY (projecto_id) REFERENCES projecto(id)
    ON DELETE CASCADE

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