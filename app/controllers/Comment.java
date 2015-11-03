package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comentario;
import models.Projecto;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Date;

/**
 * Created by Joao on 29/10/2015.
 */
public class Comment extends Controller{

    //Finder
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);
    public static Model.Finder<Long, Comentario> comentarios = new Model.Finder(Long.class, Comentario.class);

    public Result addComentario() {
        DynamicForm form = new DynamicForm().bindFromRequest();


        try {
            String msg = form.get("mensagem");
            String projecto = form.get("projecto");

            Projecto p = projectos.byId(Long.valueOf(projecto));
            Date data = new Date();
            Integer user_id = 1;
            Comentario c = new Comentario(data, msg, user_id, p);
            c.save();

            return ok(Json.toJson(c));


        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "Comment not added.");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }

    public Result editComment(){
        DynamicForm form = new DynamicForm().bindFromRequest();

        try {
            String msg = form.get("msg");
            String id = form.get("id");

            Date todayDate = new Date();

            Comentario c = comentarios.byId(Long.valueOf(id));
            c.mensagem = msg;
            c.data = todayDate;

            return ok(Json.toJson(c));


        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "Comment could not be edited.");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }
}
