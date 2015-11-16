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
        ObjectNode json = Json.newObject();
        json.put("result", "success");


        return ok(json);
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }

    }

    public Result editarNomeProjecto(){
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            String id = form.get("id");
            String nome = form.get("nome");
            String descricao = form.get("descricao");
            String user = form .get("userid");

            Projecto p = projectos.byId(Long.valueOf(id));
            ObjectNode json = Json.newObject();

            if (p.user_id == Integer.parseInt(user)){
                p.nome = nome;
                p.descricao = descricao;
                json.put("result", "success");

                return ok(json);
            }

            json.put("result", "error");
            json.put("excecao", "Not authorized");

            return badRequest(json);

        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());

            return badRequest(json);
        }
    }


    public VersaoProjecto criarVersaoProjecto(String descricao, Projecto p, String user){
        VersaoProjecto vs = new VersaoProjecto(descricao, p, "1");
        vs.save();

        return vs;
    }

    public Result TestVersionamentoProjecto(){
        try {


            DynamicForm form = new DynamicForm().bindFromRequest();
            String projecto = form.get("projecto");
            String descricao = form.get("desc");

            Projecto p = projectos.byId(Long.valueOf(projecto));
            VersaoProjecto vs = criarVersaoProjecto(descricao,p,"1");


            //Componente de Fisica
            Tipo fisica = tipos.where().eq("nome","Fisica").findUnique();
            Componente c1 = new Componente("Conteudo da componente em MARKDOWN Fisica",fisica);


            //Componente de Programacao
            Tipo prog = tipos.where().eq("nome","Programacao").findUnique();
            Componente c2 = new Componente("Conteudo da componente em MARKDOWN Programacao",prog);

            //Componente de Electrotecnia
            Tipo eletro = tipos.where().eq("nome","Eletrotecnica").findUnique();
            Componente c3 = new Componente("Conteudo da componente em MARKDOWW Electrotecnia",eletro);

            c1.versaoprojectos.add(vs);
            c1.save();
            c2.versaoprojectos.add(vs);
            c2.save();
            c3.versaoprojectos.add(vs);
            c3.save();

            vs.componentes.add(c1);
            vs.componentes.add(c2);
            vs.componentes.add(c3);
            vs.save();

            ObjectNode json = Json.newObject();
            json.put("result", "success");



            return ok(json);
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
