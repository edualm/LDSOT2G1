package controllers.api;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.Project;
import models.Projecto;
import models.Tag;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utilities.AuthManager;

import java.util.List;

/**
 * Created by user on 19-11-2015.
 */

public class ProjectTag extends Controller {

    //Finder
    public static Model.Finder<Long, models.Tag> tags = new Model.Finder(Long.class, models.Tag.class);

    public Result addTag() {
        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();
        ObjectNode response = new Json().newObject();
        try {
            String name = dynamicForm.get("nome");

            if (AuthManager.authCheck(session(), dynamicForm)) {

                if (name != null) {
                    models.Tag query = models.Tag.getTagNamed(name);

                    if (query == null) {
                        models.Tag t = new models.Tag(name);
                        t.save();

                        response.put("result", Json.toJson(t));
                        return ok(response);
                    }
                }
                response.put("exception", "Tag already exists");
                return internalServerError(response);
            }

            response.put("result", "Authorization missing or wrong");
            return forbidden(response);

        } catch (Exception e) {
            response.put("exception", e.getMessage());
            return internalServerError(response);
        }
    }

    public static Result getTagById(Long id){

        ObjectNode response = Json.newObject();

        if(id == 0) {
            response.put("exception:","File doesnt exists.");
            return internalServerError(response);
        }
        try {
            models.Tag query = tags.byId(id);
            response.put("result", Json.toJson(query));
            return  ok(response);
        }
        catch (Exception e)
        {
            response.put("exception",e.getMessage());
            return internalServerError(response);
        }
    }

    public Result getAllTags(){
        ObjectNode response = new Json().newObject();
        response.put("result", Json.toJson(tags.all()));
        return ok(response);
    }

    public Result getTagsForProject(Long id) {
        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();
        ObjectNode response = new Json().newObject();
        try {
            Projecto p = Ebean.find(Projecto.class).where().eq("id", id).findUnique();

            if (p == null) {
                response.put("exception:", "Project doesnt exists.");
                return internalServerError(response);
            }

            response.put("result",Json.toJson(p.tags));
            return ok(response);

        }catch (Exception e){
            response.put("exception",e.getMessage());
            return internalServerError(response);
        }
    }


}