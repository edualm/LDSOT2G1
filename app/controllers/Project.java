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
        ObjectNode response = Json.newObject();
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            if(session("jwt") != null)
            {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0)
                {
                    String nome = form.get("title");
                    String descricao = form.get("description");
                    String user = query.get(0).username;
                    //String user = "username";


                    Projecto p = new Projecto(nome,descricao,user);
                   VersaoProjecto vs = new VersaoProjecto(descricao, p, AuthManager.currentUsername(session("jwt")));
                    //VersaoProjecto vs = new VersaoProjecto(descricao, p, "username");
                    p.save();
                    vs.save();
                    response.put("result", "success");


                    return ok(response);
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
            else
            {
                if(form.get("jwt") != null){
                    //Verificar se o JWT recebido ? valido
                    if(AuthManager.currentUsername(form.get("jwt")) != null)
                    {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        cookie.save();

                        String nome = form.get("title");
                        String descricao = form.get("description");
                        String user = cookie.username;


                        Projecto p = new Projecto(nome,descricao,user);
                        VersaoProjecto vs = new VersaoProjecto(descricao, p, AuthManager.currentUsername(session("jwt")));
                        p.save();
                        vs.save();
                        response.put("result", "success");


                        return ok(response);

                    }
                    else
                    {
                        response.put("result","Invalid JWT");
                        return badRequest(response);
                    }
                }
                else
                {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }

    }

    public Result editarNomeProjecto(){

        ObjectNode response = Json.newObject();

        try {
            DynamicForm form = new DynamicForm().bindFromRequest();


            if(session("jwt") != null)
            {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0)
                {
                    String id = form.get("id");
                    String nome = form.get("nome");
                    String descricao = form.get("descricao");
                    String user = query.get(0).username;
                    Projecto p = projectos.byId(Long.valueOf(id));

                    if (p.user_id.equals(user)){
                        p.nome = nome;
                        p.descricao = descricao;
                        p.update();
                        response.put("result", "success");

                        return ok(response);
                    }
                    else
                    {
                        response.put("result", "error");
                        response.put("excecao", "Not authorized");
                        return unauthorized(response);
                    }
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
            else
            {
                if(form.get("jwt") != null){
                    //Verificar se o JWT recebido ? valido
                    if(AuthManager.currentUsername(form.get("jwt")) != null)
                    {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        cookie.save();

                        String id = form.get("id");
                        String nome = form.get("nome");
                        String descricao = form.get("descricao");
                        String user = cookie.username;

                        Projecto p = projectos.byId(Long.valueOf(id));

                        if (p.user_id.equals(user)){
                            p.nome = nome;
                            p.descricao = descricao;
                            p.update();
                            response.put("result", "success");

                            return ok(response);
                        }
                        else
                        {
                            response.put("result", "error");
                            response.put("excecao", "Not authorized");
                            return unauthorized(response);
                        }
                    }
                    else
                    {
                        response.put("result","Invalid JWT");
                        return badRequest(response);
                    }
                }
                else
                {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());

            return badRequest(json);
        }
    }

    public Result editarConteudoProjecto(){
        ObjectNode response = Json.newObject();

        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            if(session("jwt") != null)
            {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0)
                {

                    String id = form.get("id");
                    String nome = form.get("nome");
                    String conteudo = form.get("conteudo");
                    String user = query.get(0).username;

                    Projecto p = projectos.byId(Long.valueOf(id));

                    if (p.user_id.equals(user)) {
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
                                        response.put("result", "success");
                                        return ok(response);

                                    }
                                }
                            }
                        }

                        if (!ran) {
                            response.put("result", "error");
                            response.put("excecao", "Componente nao existente");
                            return badRequest(response);
                        }
                    }

                    response.put("result", "error");
                    response.put("excecao", "Not authorized");

                    return unauthorized(response);
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
            else
            {
                if(form.get("jwt") != null){
                    //Verificar se o JWT recebido ? valido
                    if(AuthManager.currentUsername(form.get("jwt")) != null)
                    {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        cookie.save();

                        String id = form.get("id");
                        String nome = form.get("nome");
                        String conteudo = form.get("conteudo");
                        String user = cookie.username;

                        Projecto p = projectos.byId(Long.valueOf(id));

                        if (p.user_id.equals(user)) {
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
                                            response.put("result", "success");
                                            return ok(response);

                                        }
                                    }
                                }
                            }

                            if (!ran) {
                                response.put("result", "error");
                                response.put("excecao", "Componente nao existente");
                                return badRequest(response);
                            }
                        }

                        response.put("result", "error");
                        response.put("excecao", "Not authorized");

                        return unauthorized(response);

                    }
                    else
                    {
                        response.put("result","Invalid JWT");
                        return badRequest(response);
                    }
                }
                else
                {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }







        }
        catch (Exception e){
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());

            return badRequest(json);
        }
    }

    public Result adicionarComponenteProjecto(){

        ObjectNode response = Json.newObject();
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            if(session("jwt") != null)
            {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0)
                {
                    System.out.println("User recognized: "+ AuthManager.currentUsername(session("jwt")));
                    String id = form.get("id");
                    String componente = form.get("componente");
                    String user = query.get(0).username;


                    Projecto p = projectos.byId(Long.valueOf(id));

                    if (p.user_id.equals(user)) {
                        //Os tipos sao estaticos (unicos)
                        VersaoProjecto lastVS = p.versoesProjecto.get(p.versoesProjecto.size() -1 );


                        Tipo t = tipos.where().eq("nome", componente).findUnique();
                        Componente c = new Componente("",t);
                        c.versaoprojectos.add(lastVS);
                        c.save();

                        response.put("result", "success");

                        return ok(response);

                    }
                    else {
                        response.put("result", "error");
                        response.put("excecao", "Not authorized");

                        return unauthorized(response);
                    }
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
            else
            {
                if(form.get("jwt") != null){
                    //Verificar se o JWT recebido ? valido
                    if(AuthManager.currentUsername(form.get("jwt")) != null)
                    {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        cookie.save();

                        System.out.println("Cookie saved!");
                        String id = form.get("id");
                        String componente = form.get("componente");
                        String user = cookie.username;


                        Projecto p = projectos.byId(Long.valueOf(id));

                        if (p.user_id.equals(user)) {
                            //Os tipos sao estaticos (unicos)
                            VersaoProjecto lastVS = p.versoesProjecto.get(p.versoesProjecto.size() -1 );


                            Tipo t = tipos.where().eq("nome", componente).findUnique();
                            Componente c = new Componente("",t);
                            c.versaoprojectos.add(lastVS);
                            c.save();

                            response.put("result", "success");

                            return ok(response);

                        }
                        else {
                            response.put("result", "error");
                            response.put("excecao", "Not authorized");

                            return unauthorized(response);
                        }

                    }
                    else
                    {
                        response.put("result","Invalid JWT");
                        return badRequest(response);
                    }
                }
                else
                {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
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

    public  Result getAllProjectos() {
        ObjectNode response = Json.newObject();
        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            if(session("jwt") != null)
            {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0)
                {
                    System.out.println("User recognized: "+ AuthManager.currentUsername(session("jwt")));
                    return ok(Json.toJson(projectos.orderBy("id").findList()));
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI + "projectos");
                }
            }
            else
            {
                if(form.get("jwt") != null){
                    //Verificar se o JWT recebido ? valido
                    if(AuthManager.currentUsername(form.get("jwt")) != null)
                    {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        System.out.print("Cookie on BD: "+cookie.username+ "\n Cookie data: "+ cookie.expires.toString());
                        cookie.save();

                        System.out.println("Cookie saved!");
                        return ok(Json.toJson(projectos.orderBy("id").findList()));

                    }
                    else
                    {
                        response.put("result","Invalid JWT");
                        return badRequest(response);
                    }
                }
                else
                {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI + "projectos");
                }
            }
        }
            catch(Exception e){
                response.put("exception", e.getMessage());
                return badRequest(response);
            }
    }

    public  Result removerProjecto(Long id) {
        ObjectNode response = Json.newObject();

        if (id == 0 || id < 0)
            return badRequest("Wrong Project ID");

        try {

            DynamicForm form = new DynamicForm().bindFromRequest();
			
			if(session("jwt") != null)
            {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0)
                {
                    System.out.println("User recognized: "+ AuthManager.currentUsername(session("jwt")));
                    Projecto q = projectos.byId(id);
                    String user = query.get(0).username;


                    if(q.user_id.equals(user)){
                        q.delete();

                        response.put("result", "success");
                        return ok(response);
                    }
                    else{
                        response.put("result", "Not authorized");
                        return unauthorized(response);
                    }
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
            else
            {
                if( form.get("jwt") != null){
                    //Verificar se o JWT recebido ? valido
                    if(AuthManager.currentUsername(form.get("jwt")) != null)
                    {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        System.out.print("Cookie on BD: "+cookie.username+ "\n Cookie data: "+ cookie.expires.toString());
                        cookie.save();
                        String username = cookie.username;

                        System.out.println("Cookie saved!");
                        Projecto q = projectos.byId(id);

                        if(q.user_id.equals(username)){
                            q.delete();

                            response.put("result", "success");
                            return ok(response);
                        }
                        else{
                            response.put("result", "Not authorized");
                            return unauthorized(response);
                        }

                    }
                    else
                    {
                        response.put("result","Invalid JWT");
                        return badRequest(response);
                    }
                }
                else
                {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
                }
            }
        }
		catch(Exception e){
			response.put("exception", e.getMessage());
			return badRequest(response);
		}
    }
}
