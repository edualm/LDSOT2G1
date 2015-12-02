package utilities;

import play.mvc.Http;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class AuthManager {
    public static String AuthServer_URI = "https://audiencia-zero-auth.herokuapp.com/login";
    public static String Server_URI = "https://audiencia-zero-pr-10.herokuapp.com/";

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
