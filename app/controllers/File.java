package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Ficheiro;
import models.Projecto;
import play.data.DynamicForm;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.nio.charset.Charset;

/**
 * Created by daniele on 06/11/2015.
 */
public class File extends Controller {

    //Finder
    public static Model.Finder<Long, Ficheiro> ficheiros = new Model.Finder(Long.class, Ficheiro.class);

    public Result addFile() {
        DynamicForm form = new DynamicForm().bindFromRequest();

        try {
            String nome = form.get("nome");
            String ficheiro = form.get("ficheiro");

            //parse string into byte array
            byte[] ficheiro_byte = ficheiro.getBytes(Charset.forName("UTF-8"));

            Ficheiro f = new Ficheiro(nome, ficheiro_byte);
            f.save();

            return ok(Json.toJson(f));

        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }

    public  Result getAllFiles(){
        return ok(Json.toJson(ficheiros.all()));
    }
}
