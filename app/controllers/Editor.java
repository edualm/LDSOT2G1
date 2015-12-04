package controllers;

import models.*;

import play.mvc.Controller;
import play.mvc.Result;

import utilities.AuthManager;
import views.html.editor;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MegaEduX on 03/12/15.
 */
public class Editor extends Controller {
    public Result showTestPage() {
        return ok(editor.render("foo", "bar", "baz", null, null));
    }

    public Result editProject(Long id) {
        if (session("jwt") != null) {
            //Utilizador tem cookie, verificar se ainda n expirou
            List<Sessions> query = Project.sessions.query().where().eq("token", session("jwt")).findList();
            if (query.size() > 0) {
                Projecto p = Project.projectos.byId(id);

                String user = query.get(0).username;

                if (p.user_id.equals(user)) {
                    System.out.println("editProject(): Auth success!");

                    ArrayList<Integer> versionIds = new ArrayList<>();

                    for (VersaoProjecto vp : p.versoesProjecto) {
                        versionIds.add(versionIds.size() + 1);
                    }

                    return ok(editor.render(p.nome, p.descricao, AuthManager.currentUsername(session("jwt")), p.versoesProjecto.get(p.versoesProjecto.size() - 1).componentes, versionIds));
                }
            }
        }

        return forbidden();
    }
}
