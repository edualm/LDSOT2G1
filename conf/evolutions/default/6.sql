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
-- Foreign Key structure for table "public"."ficheiro"
-- ----------------------------
ALTER TABLE "public"."ficheiro" ADD "projecto_id" int4;
ALTER TABLE "public"."ficheiro" ADD CONSTRAINT "ficheiro_projecto_id_fkey" FOREIGN KEY ("projecto_id") REFERENCES "public"."projecto" (id) ON DELETE NO ACTION ON UPDATE NO ACTION;

# --- !Downs
ALTER TABLE  "public"."ficheiro" DROP "projecto_id";
ALTER TABLE "public"."ficheiro" DROP CONSTRAINT "ficheiro_projecto_id_fkey";

