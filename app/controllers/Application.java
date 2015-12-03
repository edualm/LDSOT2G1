package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import models.Sessions;
import play.*;
import play.data.DynamicForm;
import play.mvc.*;

import utilities.AuthManager;
import views.html.*;

import java.util.List;

public class Application extends Controller {

    public static Model.Finder<Long, Sessions> sessions = new Model.Finder(String.class, Sessions.class);


    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result logout() {
        try {
            String sess = session("jwt");

            if(sess != null){
                if(AuthManager.currentUsername(sess) != null){
                    List<Sessions> query = sessions.query().where().and(Expr.eq("token",sess),(Expr.eq("username", AuthManager.currentUsername(sess)))).findList();
                    if(query.size() > 0){
                        query.get(0).delete();
                        session().clear();
                        return ok(index.render("Logged out"));
                    }
                }
            }

            session().clear();
            return ok(index.render("Logged out"));
        }catch (Exception e){
                session().clear();
            return ok(index.render("Logged out"));
        }
    }

}
