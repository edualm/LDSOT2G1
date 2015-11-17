package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Ficheiro;
import org.apache.commons.io.IOUtils;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 */
public class FileProject extends Controller {

    //Finder
    public static Model.Finder<Long, models.Ficheiro> ficheiros = new Model.Finder(Long.class, models.Ficheiro.class);

    public Result addFile() {
        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();
        Http.MultipartFormData form = request().body().asMultipartFormData();

        try {
            String nome = dynamicForm.get("nome");
            System.out.println("A");
            String imgPathToSave = "images/" + nome;
            System.out.println("B");
            Http.MultipartFormData.FilePart picture = form.getFile("picture");
            System.out.println("C");
            //parse string into byte array
               // String contentType = picture.getContentType();
                File imgFile = picture.getFile();
            System.out.println("D");
                byte[] bytes = IOUtils.toByteArray(new FileInputStream(imgFile));
            System.out.println("E");
                Ficheiro ficheiro =  new Ficheiro(nome, bytes);
            System.out.println("F");
                ficheiro.save();
            System.out.println("G");
                return ok(Json.toJson(ficheiro));

        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }

    public Result getFileById(Long id){

        ObjectNode response = Json.newObject();


        if(id == 0)
            return badRequest("Wrong File ID");

        try {
            Ficheiro query = ficheiros.byId(id);
            return  ok(Json.toJson(query));
        }
        catch (Exception e)
        {
            response.put("result",e.getMessage());
            return badRequest(response);
        }
    }

    public Result getAllFiles(){
        return ok(Json.toJson(ficheiros.all()));
    }
}
