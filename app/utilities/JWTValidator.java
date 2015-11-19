package utilities;

import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.*;

/**
 * Created by MegaEduX on 03/11/15.
 */

public class JWTValidator {
    private static class NoSavedKeyPairException extends Exception {}

    private static final String kPublicKey = "jwt-publicKey";
    private static final String kPrivateKey = "jwt-privateKey";

    private static RsaJsonWebKey jsonWebKey = null;

    static public final String ServerName = "jwt-auth-server";
    static public final Integer MinimumPasswordLength = 8;
    static public final String KeyLink = "https://audiencia-zero-auth.herokuapp.com/rsa/base64";


    public static boolean acceptToken(String jwt) {
    try
    {

        URL url = new URL(KeyLink);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();

        // Decode data on other side, by processing encoded data
        byte[] valueDecoded= Base64.getDecoder().decode(is.toString());
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(valueDecoded));
        RsaJsonWebKey rsaJwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(publicKey);



        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer(ServerName) // whom the JWT needs to have been issued by
                .setExpectedAudience(ServerName) // to whom the JWT is intended for
                    .setVerificationKey(publicKey) // verify the signature with the public key
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
            URL url = new URL(KeyLink);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();



            // Decode data on other side, by processing encoded data
            byte[] valueDecoded= Base64.getDecoder().decode(is.toString());
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(valueDecoded));
            RsaJsonWebKey rsaJwk = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(publicKey);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime() // the JWT must have an expiration time
                    .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                    .setRequireSubject() // the JWT must have a subject claim
                    .setExpectedIssuer(ServerName) // whom the JWT needs to have been issued by
                    .setExpectedAudience(ServerName) // to whom the JWT is intended for
                    .setVerificationKey(publicKey) // verify the signature with the public key
                    .build(); // create the JwtConsumer instance

            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! " + jwtClaims);

            return (String) jwtClaims.getClaimValue("username");
        } catch (Exception e) {
            return null;
        }
    }
}
