package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Tag;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by user on 19-11-2015.
 */
public class FileTag extends Controller {

    //Finder
    public static Model.Finder<Long, models.Tag> tags = new Model.Finder(Long.class, models.Tag.class);

    public Result addTag() {

    }

    public Result getTagById(Long id){

        ObjectNode response = Json.newObject();


        if(id == 0)
            return badRequest("Wrong File ID");

        try {
            Tag query = tags.byId(id);
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

}


