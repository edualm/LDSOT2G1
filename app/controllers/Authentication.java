package controllers;

import com.avaje.ebean.Ebean;
import models.Sessions;
import play.data.DynamicForm;
import play.mvc.Http;
import utilities.AuthManager;

import java.util.List;

/**
 * Created by MegaEduX on 26/12/15.
 */

public class Authentication {
    static public boolean authCheck(Http.Session session, DynamicForm form) {
        try {
            if (session.get("jwt") != null) {
                List<Sessions> query = Ebean.find(Sessions.class).where().eq("token", session.get("jwt")).findList();

                if (query.size() > 0)
                    return true;
                else
                    session.clear();
            }

            if (form.get("jwt") != null) {
                if (AuthManager.currentUsername(form.get("jwt")) != null) {
                    session.put("jwt", form.get("jwt"));

                    Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                    cookie.save();

                    return true;
                }

                return false;
            }

            return false;
        } catch(Exception e) {
            System.out.println("Exception on authCheck(): " + e.getMessage());

            return false;
        }
    }
}
