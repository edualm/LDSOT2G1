package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Projecto;
import models.Tag;
import play.data.DynamicForm;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by user on 19-11-2015.
 */

public class ProjectTag extends Controller {

    //Finder
    public static Model.Finder<Long, models.Tag> tags = new Model.Finder(Long.class, models.Tag.class);

    public Result addTag() {
        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();

        try {
            String name = dynamicForm.get("nome");

            if (name != null) {
                models.Tag query = models.Tag.getTagNamed(name);

                if (query == null) {
                    models.Tag t =  new models.Tag(name);
                    t.save();

                    return ok(Json.toJson(t));
                }
            }

            ObjectNode json = Json.newObject();
            json.put("result", "ProjectTag already exists");
            return ok(json);

        } catch (Exception e) {
            ObjectNode json = Json.newObject();
            json.put("result", "error");
            json.put("excecao", e.getMessage());
            return badRequest(json);
        }
    }

    public static Result getTagById(Long id){

        ObjectNode response = Json.newObject();

        if(id == 0)
            return badRequest("Wrong File ID");

        try {
            models.Tag query = tags.byId(id);
            return  ok(Json.toJson(query));
        }
        catch (Exception e)
        {
            response.put("result",e.getMessage());
            return badRequest(response);
        }
    }

    public Result getAllTags(){
        return ok(Json.toJson(tags.all()));
    }

    public Result getTagsForProject(Long id) {
        DynamicForm dynamicForm = new DynamicForm().bindFromRequest();

        List<Projecto> projects = Ebean.find(Projecto.class).where().eq("id", id).findList();

        for (Projecto p : projects) {
            return ok(Json.toJson(p.tags));
        }

        return notFound();
    }

    public Result getOrCreateTagNamed(String name) {
        Tag t = Tag.getTagNamed(name);

        if (t == null) {
            t = new Tag(name);

            t.save();
        }

        return ok(t.id.toString());
    }

}