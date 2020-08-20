import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class SysHelper {
    /***
     * @param data: query string
     * @param secret: systemAccessSecret
     */
    public static String encryptHMAC(String data, String secret) throws GeneralSecurityException, IOException {
        System.out.println("Encrypting: " + data);
        byte[] bytes;
        SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        bytes = mac.doFinal(data.getBytes("UTF-8"));
        return bytesToHexString(bytes);
    }

    public static String bytesToHexString(byte[] bytes) {
        //NOTE: Signature must be in upper case
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexBuilder.append(String.format("%02x", b).toUpperCase());
        }
        return hexBuilder.toString();
    }
}
