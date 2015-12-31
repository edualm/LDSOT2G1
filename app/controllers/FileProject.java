package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Ficheiro;
import models.Projecto;
import org.apache.commons.io.IOUtils;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utilities.AuthManager;
import views.html.generic;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 */
public class FileProject extends Controller {

    //Finder
    public static Model.Finder<Long, models.Ficheiro> ficheiros = new Model.Finder(Long.class, models.Ficheiro.class);
    public static Model.Finder<Long, Projecto> projectos = new Model.Finder(Long.class, Projecto.class);


    public Result addFile() {
        ObjectNode response = Json.newObject();

        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();

        if (AuthManager.authCheck(session(), dynamicForm)) {

            String id = dynamicForm.get("projecto");
            Http.MultipartFormData form = request().body().asMultipartFormData();

            try {

                Projecto p = projectos.byId(Long.valueOf(id));

                if(p == null)  return notFound(generic.render("Not Found!", "Project not found.", true));

                Ficheiro f = null;

                Http.MultipartFormData.FilePart ficheiro = form.getFile("file");
                byte[] bytes = null;

                if (ficheiro != null) {
                    File imgFile = ficheiro.getFile();

                    bytes = IOUtils.toByteArray(new FileInputStream(imgFile));
                    f = new Ficheiro(ficheiro.getFilename(),bytes,p);
                }
                else{
                    return internalServerError();
                }

                response.put("result", "success");
                return ok(response);
            }
            catch(Exception e)
            {
                return internalServerError();
            }
        } else
        {
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.Server_URI);
        }
    }

    public Result getFileById(Long id) {
        // TODO: 26/12/15 User auth check!

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
