package controllers;

import models.*;

import play.mvc.Controller;
import play.mvc.Result;

import utilities.AuthManager;
import views.html.editor;
import views.html.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MegaEduX on 03/12/15.
 */

public class Editor extends Controller {
    public Result showTestPage() {
        return ok(editor.render("foo", "bar", "baz", null, null, null, null));
    }

    public Result editProject(Long id) {
        if (session("jwt") != null) {
            //Utilizador tem cookie, verificar se ainda n expirou
            List<Sessions> query = Project.sessions.query().where().eq("token", session("jwt")).findList();
            if (query.size() > 0) {
                Projecto p = Project.projectos.byId(id);

                if (p == null) {
                    return notFound(generic.render("Not Found!", "Project not found."));
                }

                String user = query.get(0).username;

                if (p.user_id.equals(user)) {
                    System.out.println("editProject(): Auth success!");

                    ArrayList<Tipo> missingTipos = new ArrayList<>();

                    List<Componente> currentComponents = p.versoesProjecto.get(p.versoesProjecto.size() - 1).componentes;

                    for (Tipo t: Tipo.getTipos()) {
                        boolean contains = false;

                        for (Componente c : currentComponents) {
                            if (c.tipo_id.equals(t)) {
                                contains = true;

                                break;
                            }
                        }

                        if (!contains)
                            missingTipos.add(t);
                    }

                    //  Debug code inc...

                    for (Componente c : p.versoesProjecto.get(p.versoesProjecto.size() - 1).componentes) {
                        System.out.println(c.id);
                        System.out.println(c.conteudo);
                    }

                    return ok(editor.render(p.nome,
                            p.descricao,
                            AuthManager.currentUsername(session("jwt")),
                            p.versoesProjecto.get(p.versoesProjecto.size() - 1).componentes,
                            missingTipos,
                            p.versoesProjecto.get(p.versoesProjecto.size() - 1),
                            p.versoesProjecto));
                }
            }
        }

        return forbidden();
    }
}
