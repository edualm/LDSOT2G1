package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Model;
import models.Projecto;
import models.Sessions;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;
import views.html.index;

import java.util.List;

public class Application extends Controller {

    public static Model.Finder<Long, Sessions> sessions = new Model.Finder(String.class, Sessions.class);


    public Result index() {

        List<Projecto> p = Project.projectos.where().order("id desc").setMaxRows(10).findList();

        return ok(index.render("Your new application is ready.", AuthManager.authCheck(session(), new DynamicForm().bindFromRequest()), p));
    }

    public Result logout() {

        List<Projecto> p = Project.projectos.all();
        try {
            String sess = session("jwt");

            if(sess != null){
                if(AuthManager.currentUsername(sess) != null){
                    List<Sessions> query = sessions.query().where().and(Expr.eq("token",sess),(Expr.eq("username", AuthManager.currentUsername(sess)))).findList();
                    if(query.size() > 0){
                        query.get(0).delete();
                        session().clear();
                        return ok(index.render("Logged out.", AuthManager.authCheck(session(), new DynamicForm().bindFromRequest()), p));
                    }
                }
            }

            session().clear();

            return ok(index.render("Logged out", AuthManager.authCheck(session(), new DynamicForm().bindFromRequest()), p));
        } catch (Exception e){
            session().clear();
            return ok(index.render("Logged out", AuthManager.authCheck(session(), new DynamicForm().bindFromRequest()), p));
        }
    }
}