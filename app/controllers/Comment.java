package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Comentario;
import models.Projecto;
import models.Sessions;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by Joao on 29/10/2015.
 */
public class Comment extends Controller{

    //Finder
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);
    public static Model.Finder<Long, Comentario> comentarios = new Model.Finder(Long.class, Comentario.class);
    public static Model.Finder<Long, Sessions> sessions = new Model.Finder(String.class, Sessions.class);


    public Result addComentario() {
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
                    String msg = form.get("mensagem");
                    String projecto = form.get("projecto");


                    Projecto p = projectos.byId(Long.valueOf(projecto));
                    String user_id = AuthManager.currentUsername(session("jwt"));
                    Comentario c = new Comentario(msg, user_id, p);
                    c.save();

                    response.put("result", "success");
                    return ok(response);
                }
                else
                {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
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

                        String msg = form.get("mensagem");
                        String projecto = form.get("projecto");


                        Projecto p = projectos.byId(Long.valueOf(projecto));
                        String user_id = AuthManager.currentUsername(session("jwt"));
                        Comentario c = new Comentario(msg, user_id, p);
                        c.save();

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
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
                }
            }
        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }

    public Result editComment(){
        ObjectNode response = Json.newObject();

        try {
            DynamicForm form = new DynamicForm().bindFromRequest();

            if (session("jwt") != null) {
                //Utilizador tem cookie, verificar se ainda n expirou
                List<Sessions> query = sessions.query().where().eq("token", session("jwt")).findList();
                if (query.size() > 0) {
                    String msg = form.get("msg");
                    String id = form.get("id");
                    String user = query.get(0).username;

                    Date todayDate = new Date();

                    Comentario c = comentarios.byId(Long.valueOf(id));

                    if (c.user_id.equals(user)) {
                        c.mensagem = msg;
                        c.data = new Timestamp(new Date().getTime());
                        c.update();

                        response.put("result", "success");
                        return ok(response);

                    } else {
                        response.put("result", "Not authorized");
                        return unauthorized(response);
                    }
                } else {
                    session().clear();
                    System.out.println("Cookie expired, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
                }
            } else {
                if (form.get("jwt") != null) {
                    //Verificar se o JWT recebido ? valido
                    if (AuthManager.currentUsername(form.get("jwt")) != null) {
                        session("jwt", form.get("jwt"));
                        Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                        System.out.print("Cookie on BD: " + cookie.username + "\n Cookie data: " + cookie.expires.toString());
                        cookie.save();

                        String msg = form.get("msg");
                        String id = form.get("id");
                        String username = cookie.username;

                        Date todayDate = new Date();

                        Comentario c = comentarios.byId(Long.valueOf(id));

                        if (c.user_id.equals(username)) {
                            c.mensagem = msg;
                            c.data = new Timestamp(new Date().getTime());
                            c.update();

                            response.put("result", "success");
                            return ok(response);
                        } else {
                            response.put("result", "Not authorized");
                            return unauthorized(response);
                        }


                    } else {
                        response.put("result", "Invalid JWT");
                        return badRequest(response);
                    }
                } else {
                    System.out.println("Not logged in, redirecting to auth server ...");
                    return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
                }
            }
        }
        catch (Exception e) {
            response.put("result", "error");
            response.put("excecao", e.getMessage());
            return badRequest(response);
        }
    }
}
