package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import java.util.List;
import models.Projecto;
import play.mvc.Result;
import play.data.DynamicForm;
import play.mvc.Controller;
import utilities.AuthManager;
import views.html.search;

/**
 * Created by MegaEduX on 23/12/15.
 */

public class Search extends Controller {
    public Result searchResults() {
        DynamicForm form = new DynamicForm().bindFromRequest();

        String query = form.get("query");

        if (query == null)
            return internalServerError();

        List<Projecto> res = Ebean.find(Projecto.class).where().or(Expr.icontains("nome", query), Expr.icontains("descricao", query)).findList();

        return ok(search.render(res, AuthManager.authCheck(session(), form)));
    }
}
