# --- !Ups

-- ----------------------------
-- Table structure for projecto
-- ----------------------------
ALTER TABLE "public"."projecto" ADD imagem bytea;

# --- !Downs
ALTER TABLE "public"."projecto" DROP imagem;