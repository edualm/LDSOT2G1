package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.api.i18n.Messages;
import play.api.libs.json.JsPath;
import play.api.libs.ws.ssl.SystemConfiguration;
import play.api.mvc.LegacyI18nSupport;
import play.api.mvc.Security;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utilities.AuthManager;


import java.io.Console;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joao on 27/10/2015.
 */
public class Project extends Controller {

    //Finder
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);
    public static Model.Finder<Long, Tipo> tipos = new Model.Finder(Long.class, Tipo.class);
    public static Model.Finder<Long, Sessions> sessions = new Model.Finder(String.class, Sessions.class);

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
                p.update();
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

    public Result editarConteudoProjecto(){
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            String id = form.get("id");
            String nome = form.get("nome");
            String conteudo = form.get("conteudo");
            String user = form .get("userid");

            Projecto p = projectos.byId(Long.valueOf(id));
            ObjectNode json = Json.newObject();

            if (p.user_id == Integer.parseInt(user)) {
                VersaoProjecto oldVS = p.versoesProjecto.get(p.versoesProjecto.size() - 1);
                List<Componente> componentes = oldVS.componentes;

                String tipo = null;

                if (nome.equals("fis"))
                    tipo = "Fisica";
                else if (nome.equals("prog"))
                    tipo = "Programacao";
                else if (nome.equals("elec"))
                    tipo = "Eletrotecnica";

                boolean ran = false;

                for (Componente c : componentes){
                    System.out.println("Componente: " + c.tipo_id.nome);
                    System.out.println("Componente API: " + tipo);
                    if (c.tipo_id.nome.equals(tipo)) {
                        ran = true;
                        System.out.println("Found the component name");

                        System.out.println("Creating the New VersaoProjecto...");
                        VersaoProjecto newVS = new VersaoProjecto(oldVS.descricao, oldVS.projecto_id, oldVS.user_id.toString());
                        newVS.componentes = new ArrayList<Componente>(oldVS.componentes);
                        System.out.println("Removing old component...");
                        newVS.componentes.remove(c);
                        Componente cNew = new Componente("",c.tipo_id);
                        cNew.save();
                        System.out.println("Adding new Component to Versao projeto");
                        newVS.componentes.add(cNew);

                        for (Componente newC : newVS.componentes) {
                            System.out.println("ComponenteNew: " + newC.tipo_id.nome);
                            if (newC.tipo_id.nome.equals(tipo)) {
                                System.out.println("Found the  New component name");
                                newC.conteudo = conteudo;
                                newC.update();
                                newVS.save();
                                json.put("result", "success");
                                return ok(json);

                            }
                        }
                    }
            }

                if (!ran) {
                    json.put("result", "error");
                    json.put("excecao", "Componente nao existente");
                    return badRequest(json);
                }
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

    public Result adicionarComponenteProjecto(){

        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            String id = form.get("id");
            String componente = form.get("componente");
            String user = form.get("userid");


            Projecto p = projectos.byId(Long.valueOf(id));
            ObjectNode json = Json.newObject();

            if (p.user_id == Integer.parseInt(user)) {
                //Os tipos sao estaticos (unicos)
                VersaoProjecto lastVS = p.versoesProjecto.get(p.versoesProjecto.size() -1 );


                Tipo t = tipos.where().eq("nome", componente).findUnique();
                Componente c = new Componente("",t);
                c.versaoprojectos.add(lastVS);
                c.save();

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

    //Adicionar repositorio
    //Remover projeto



    public  Result getAllProjectos() {
        ObjectNode response = Json.newObject();
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();
            String jwt = form.get("jwt");

            if (jwt == null){
                System.out.println("Not logged in, redirecting to auth server ...");
                return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI + "projectos");
            }
            System.out.println("Received JWT: " + jwt);

            String userLoggedIn = AuthManager.currentUsername(jwt);


            if (userLoggedIn == null){
                response.put("result","Invalid JWT");
                return badRequest(response);
            }

            //Check if its on session
            List<Sessions> query = sessions.query().where().and(Expr.eq("username", userLoggedIn), Expr.eq("token", jwt)).findList();

            if(query.size() == 1){
                System.out.println("Logged in username: "+ userLoggedIn);

                return ok(Json.toJson(projectos.orderBy("id").findList()));
            }
            response.put("result", "Session expired. Login again");

            return badRequest(response);
        }
            catch(Exception e){
                response.put("exception", e.getMessage());
                return badRequest(response);
            }
    }
}
