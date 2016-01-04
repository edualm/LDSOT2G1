package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;

import java.util.ArrayList;
import java.util.List;
import models.Projecto;
import models.Tag;
import models.VersaoProjecto;
import play.mvc.Result;
import play.data.DynamicForm;
import play.mvc.Controller;
import utilities.AuthManager;
import views.html.search;

public class Search extends Controller {
    public Result searchResults() {
        DynamicForm form = new DynamicForm().bindFromRequest();

        String query = form.get("query");

        if (query == null)
            return internalServerError();

        List<Projecto> res = Ebean.find(Projecto.class).where().or(Expr.icontains("nome", query), Expr.icontains("descricao", query)).findList();

        List<Tag> tag = Ebean.find(Tag.class).where().eq("nome", query).findList();

        return ok(search.render(query, res, tag, AuthManager.authCheck(session(), form)));
    }
}
