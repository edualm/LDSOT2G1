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
-- Table structure for sessions
-- ----------------------------
CREATE TABLE "public"."sessions" (
 "username" varchar(255) COLLATE "default" NOT NULL,
 "token" text COLLATE "default" NOT NULL,
 "expires" TIMESTAMP NOT NULL
)
WITH (OIDS=FALSE)

;



-- ----------------------------
-- Table structure for comentario
-- ----------------------------
ALTER TABLE "public"."comentario" ALTER COLUMN user_id TYPE  varchar(255);



-- ----------------------------
-- Table structure for projecto
-- ----------------------------
ALTER TABLE "public"."projecto" ALTER COLUMN user_id TYPE  varchar(255);
-- ----------------------------


-- ----------------------------
-- Table structure for versaoprojecto
ALTER TABLE "public"."versaoprojecto" ALTER COLUMN user_id TYPE  varchar(255);


-- ----------------------------
-- Table structure for projecto
-- ----------------------------
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
DROP TABLE IF EXISTS "public"."session";
DROP TRIGGER trigger_delete_old_rows ON "public"."sessions";
DROP FUNCTION delete_old_rows();