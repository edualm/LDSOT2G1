package utilities;

import org.apache.commons.io.IOUtils;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class JWTValidator {
    static public final String ServerName = "Audiência Zero SSO";   //  debug only!!!
    static public final String KeyLink = "https://audiencia-zero-auth.herokuapp.com/rsa/base64";

    static private PublicKey kPublicKey = null;

    private static PublicKey getPublicKey() {
        System.out.println("Attempting to get public key...");

        if (kPublicKey == null) {
            try {
                URL url = new URL(KeyLink);
                String b64 = IOUtils.toString(url, Charset.defaultCharset());

                System.out.println("PubKey B64: " + b64);

                byte[] pubKey = Base64.getDecoder().decode(b64);

                kPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
            } catch (Exception e) {
                System.out.println("getPublicKey() -> " + e.getMessage());
            }
        } else {
            System.out.println("PubKey was already set: " + kPublicKey);
        }

        return kPublicKey;
    }

    public static boolean acceptToken(String jwt) {
        try {
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer(ServerName) // whom the JWT needs to have been issued by
                .setExpectedAudience(ServerName) // to whom the JWT is intended for
                .setVerificationKey(getPublicKey()) // verify the signature with the public key
                .build(); // create the JwtConsumer instance

            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);

            System.out.println("JWT validation succeeded! " + jwtClaims);

            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT! " + e.getMessage());

            return false;
        }
    }

    public static String getUsernameFromToken(String jwt) {
        try {
            System.out.println("Here also.");

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setExpectedIssuer(ServerName) // whom the JWT needs to have been issued by
                    .setExpectedAudience(ServerName) // to whom the JWT is intended for
                    .setVerificationKey(getPublicKey()) // verify the signature with the public key
                    .build(); // create the JwtConsumer instance

            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! " + jwtClaims);

            return (String) jwtClaims.getClaimValue("username");
        } catch (Exception e) {
            System.out.println("getUsernameFromToken() -> Exception: " + e.getMessage());

            return null;
        }
    }
}
