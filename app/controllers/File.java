package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Ficheiro;
import models.Projecto;
import play.data.DynamicForm;
import play.libs.Json;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.nio.charset.Charset;

/**
 *
 */
public class File extends Controller {

    //Finder
    public static Model.Finder<Long, Ficheiro> ficheiros = new Model.Finder(Long.class, Ficheiro.class);

    public Result addFile() {
        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();
        Http.MultipartFormData form = request().body().asMultipartFormData();

        try {
            String nome = dynamicForm.get("nome");
            Http.MultipartFormData.FilePart picture = form.getFile("picture");

            //parse string into byte array
                String contentType = picture.getContentType();
                Ficheiro file =  new Ficheiro(nome, picture.getFile());
                file.save();
                return ok(Json.toJson(file));

        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }

    public Result getAllFiles(){
        return ok(Json.toJson(ficheiros.all()));
    }
}
