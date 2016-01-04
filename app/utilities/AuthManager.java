package utilities;


import com.avaje.ebean.Ebean;
import models.Sessions;
import play.data.DynamicForm;
import play.mvc.Http;

import java.util.List;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class AuthManager {
    public static String AuthServer_URI = "http://audiencia-zero-auth.herokuapp.com/login";

    static public String getServerURL(Http.Request request) {
        return "http://" + getServerURI(request);
    }
    static public String getServerURI(Http.Request request) {
        return request.headers().get("Host")[0];
    }

    public static boolean isLoggedIn(Http.Cookies allCookies) {
        Http.Cookie authCookie = allCookies.get("jwt");

        if (authCookie == null)
            return false;

        return (JWTValidator.acceptToken(authCookie.value()));
    }

    public static String currentUsername(String jwt) {
        System.out.println("We got here.");

        return (JWTValidator.getUsernameFromToken(jwt));
    }

    static public boolean authCheck(Http.Session session, DynamicForm form) {
        try {
            if (form.get("jwt") != null) {
                if (AuthManager.currentUsername(form.get("jwt")) != null) {
                    session.put("jwt", form.get("jwt"));

                    Sessions cookie = new Sessions(AuthManager.currentUsername(form.get("jwt")), form.get("jwt"));
                    cookie.save();

                    System.out.println(AuthManager.currentUsername(form.get("jwt")));
                    System.out.println(form.get("jwt"));

                    return true;
                }

                return false;
            }

            if (session.get("jwt") != null) {
                List<Sessions> query = Ebean.find(Sessions.class).where().eq("token", session.get("jwt")).findList();

                if (query.size() > 0)
                    return true;
                else
                    session.clear();
            }

            return false;
        } catch(Exception e) {
            System.out.println("Exception on authCheck(): " + e.getMessage());

            return false;
        }
    }
}
