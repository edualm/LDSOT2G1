package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.avaje.ebean.SqlUpdate;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Ficheiro;
import models.Projecto;
import org.apache.commons.io.FileUtils;
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
import java.util.List;

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
                    f.save();
                }
                else{
                    return internalServerError();
                }


                return ok(Json.toJson(f));
            }
            catch(Exception e)
            {
                return internalServerError();
            }
        } else
        {
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
        }
    }

    public Result getFileByID(Long id) {
        // TODO: 26/12/15 User auth check!

        ObjectNode response = Json.newObject();


        if(id == 0)
            return notFound(generic.render("Not Found!", "Project not found.", true));

        try {
            Ficheiro p = ficheiros.where().eq("id",Long.valueOf(id)).findUnique();

            if (p == null) return notFound(generic.render("Not Found!", "File not found.", true));

            File f = new java.io.File("files/"+p.nome);
            FileUtils.writeByteArrayToFile(f,p.ficheiro);

            return  ok(f);
        }
        catch (Exception e)
        {
            response.put("result",e.getMessage());
            return badRequest(response);
        }
    }

    public Result removeFile(Long id){
        ObjectNode response = Json.newObject();

        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();

        if (AuthManager.authCheck(session(), dynamicForm)) {

            try {


                Ficheiro p = ficheiros.where().eq("id" , id.intValue()).findUnique();

                if(p == null)  return notFound(generic.render("Not Found!", "File not found.", true));

                if(p.projecto_id.user_id.equals(AuthManager.currentUsername(session().get("jwt")))) {

                    String dml = "delete from ficheiro where id = :id";
                    SqlUpdate update = Ebean.createSqlUpdate(dml)
                            .setParameter("id", id.intValue());
                    int rows = update.execute();

                    response.put("result", "File deleted");

                    return ok(response);
                }
                return forbidden("You are not authorized");
            }
            catch(Exception e)
            {
                response.put("exception", e.getMessage());
                return internalServerError(response);
            }
        } else
        {
            return redirect(AuthManager.AuthServer_URI + "?callback=" + AuthManager.getServerURL(request()));
        }
    }

    public Result getAllFiles(){
        return ok(Json.toJson(ficheiros.all()));
    }
}
