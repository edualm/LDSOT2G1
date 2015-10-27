package controllers;

import com.avaje.ebean.Model;
import models.Projecto;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by Joao on 27/10/2015.
 */
public class Project extends Controller {


    //Finder
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);

    public Result CriarProjecto(){
        DynamicForm form = new DynamicForm().bindFromRequest();

        String nome = form.get("nome");
        String descricao = form.get("descricao");
        String user = form.get("user");


        Projecto p = new Projecto(nome,descricao,user);
        p.save();

        Projecto call = projectos.select("id").where().eq("user_id", p.user_id).findUnique();

        return ok(Json.toJson(call));

    }
    public Result getProjectoById(Long id){

        Projecto query = projectos.byId(id);

        return ok(Json.toJson(query));
    }

    public  Result getAllProjectos(){
        return ok(Json.toJson(projectos.all()));
    }
}
