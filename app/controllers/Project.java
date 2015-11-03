package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Projecto;
import models.VersaoProjecto;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import com.fasterxml.jackson.databind.JsonNode;


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
        VersaoProjecto vs = new VersaoProjecto("versao_", p, "1");
        vs.save();

        Projecto call = projectos.select("id").where().eq("user_id", p.user_id).findUnique();

        return ok(Json.toJson(call));

    }
    public Result getProjectoById(Long id){

        ObjectNode response = Json.newObject();


        if(id == 0)
            return badRequest("Wrong Project ID");

        try {
            Projecto query = projectos.byId(id);
            return  ok(Json.toJson(query));
        }
        catch (Exception e)
        {
            response.put("result",e.getMessage());
            return badRequest(response);
        }
    }

    public  Result getAllProjectos(){
        return ok(Json.toJson(projectos.all()));
    }
}
