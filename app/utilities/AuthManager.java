package utilities;

import play.mvc.Http;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class AuthManager {
    public static boolean isLoggedIn(Http.Cookies allCookies) {
        Http.Cookie authCookie = allCookies.get("jwt");

        if (authCookie == null)
            return false;

        return (JWTValidator.acceptToken(authCookie.value()));
    }

    public static String currentUsername(Http.Cookies allCookies) {
        Http.Cookie authCookie = allCookies.get("jwt");

        if (authCookie == null)
            return null;

        return (JWTValidator.getUsernameFromToken(authCookie.value()));
    }
}
