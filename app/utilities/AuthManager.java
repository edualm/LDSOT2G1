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
    public static String AuthServer_URI = "https://audiencia-zero-auth.herokuapp.com/login";
    public static String Server_URI = "https://audiencia-zero.herokuapp.com/";

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
            System.out.println("!!!");

            if (form.get("jwt") != null) {
                System.out.println("Here! 1");
                if (AuthManager.currentUsername(form.get("jwt")) != null) {
                    System.out.println("Here! 2");
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

                System.out.println("2 !!!");

                if (query.size() > 0)
                    return true;
                else
                    session.clear();
            }

            System.out.println("OMG !!!");

            return false;
        } catch(Exception e) {
            System.out.println("Exception on authCheck(): " + e.getMessage());

            return false;
        }
    }
}
