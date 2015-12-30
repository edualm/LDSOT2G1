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
    
    //  Finder
    
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);
    public static Model.Finder<Long, Comentario> comentarios = new Model.Finder(Long.class, Comentario.class);
    public static Model.Finder<Long, Sessions> sessions = new Model.Finder(String.class, Sessions.class);
    
    public Result addComentario() {
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            ObjectNode response = Json.newObject();
            
            String msg = form.get("mensagem");
            String projecto = form.get("projecto");
            
            Projecto p = projectos.byId(Long.valueOf(projecto));
            String user_id = AuthManager.currentUsername(session("jwt"));
            Comentario c = new Comentario(msg, user_id, p);
            c.save();
            
            response.put("result", "success");
            return ok(response);
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
    }
    
    public Result editComment() {
        DynamicForm form = new DynamicForm().bindFromRequest();
        
        if (AuthManager.authCheck(session(), form)) {
            ObjectNode response = Json.newObject();
            
            String msg = form.get("msg");
            String id = form.get("id");
            String user = AuthManager.currentUsername(session("jwt"));
            
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
        } else
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
    }
}