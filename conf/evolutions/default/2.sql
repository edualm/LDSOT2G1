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
-- Table structure for comentario
-- ----------------------------
CREATE TABLE "public"."sessions" (
 "username" varchar(255) COLLATE "default" NOT NULL,
 "token" varchar(255) COLLATE "default" NOT NULL,
 "expires" date NOT NULL
)
WITH (OIDS=FALSE)

;


# --- !Downs

DROP TABLE IF EXISTS "public"."session";