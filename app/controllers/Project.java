package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Componente;
import models.Projecto;
import models.Tipo;
import models.VersaoProjecto;
import play.api.i18n.Messages;
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
    public static Model.Finder<Long, Tipo> tipos = new Model.Finder(Long.class, Tipo.class);

    public Result CriarProjecto(){
        try {
        DynamicForm form = new DynamicForm().bindFromRequest();

        String nome = form.get("nome");
        String descricao = form.get("descricao");
        String user = form.get("user");


        Projecto p = new Projecto(nome,descricao,user);
        VersaoProjecto vs = new VersaoProjecto(descricao, p, "1");
        p.save();
        vs.save();


        return ok(Json.toJson(p));
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }

    }

    public Result TestVersionamentoProjecto(){
        try {


            DynamicForm form = new DynamicForm().bindFromRequest();
            String projecto = form.get("projecto");
            String descricao = form.get("desc");

            Projecto p = projectos.byId(Long.valueOf(projecto));
            VersaoProjecto vs = new VersaoProjecto(descricao, p, "1");
            vs.save();


            //Componente de Fisica
            Tipo fisica = tipos.where().eq("nome","Fisica").findUnique();
            Componente c1 = new Componente("Conteudo da componente em MARKDOWN",fisica);

            c1.save();

            //Componente de Programacao
            Tipo prog = tipos.where().eq("nome","Programacao").findUnique();
            Componente c2 = new Componente("Conteudo da componente em MARKDOWN",prog);

            c2.save();

            //Componente de Electrotecnia
            Tipo eletro = tipos.where().eq("nome","Eletrotecnica").findUnique();
            Componente c3 = new Componente("Conteudo da componente em MARKDOWW",eletro);

            c3.save();

            vs.componentes.add(c1);
            vs.componentes.add(c2);
            vs.componentes.add(c3);

            vs.update();



            return ok(Json.toJson(vs));
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }





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
