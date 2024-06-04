package irlink.otptest.util;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;


public class OTPUtil {
    private static final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public static String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    public static String getQRBarcodeURL(String user, String host, GoogleAuthenticatorKey secret) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(host, user, secret);
    }

    public static boolean validateOTP(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}
