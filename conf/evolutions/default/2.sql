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

-- -----------------------------
-- Foreign Key structure for table "public"."comentario"
-- ----------------------------

ALTER TABLE "public"."comentario" ADD FOREIGN KEY ("projecto_id") REFERENCES "public"."projecto" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

-- ----------------------------
-- Foreign Key structure for table "public"."versaoprojecto"
-- ----------------------------

ALTER TABLE "public"."versaoprojecto" ADD FOREIGN KEY ("projecto_id") REFERENCES "public"."projecto" ("id") ON DELETE CASCADE ON UPDATE NO ACTION;

# --- !Downs
ALTER TABLE "public"."comentario" DROP CONSTRAINT "comentario_projecto_id_fkey";
ALTER TABLE "public"."versaoprojecto" DROP CONSTRAINT "versaoprojecto_projecto_id_fkey";