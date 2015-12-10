package utilities;


import play.mvc.Http;

import java.util.List;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class AuthManager {
    public static String AuthServer_URI = "https://audiencia-zero-auth.herokuapp.com/login";
    //  public static String Server_URI = "https://audiencia-zero.herokuapp.com/";

    static public String getServerURL(Http.Request request) {
        return "https://" + getServerURI(request) + "/";
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



}
