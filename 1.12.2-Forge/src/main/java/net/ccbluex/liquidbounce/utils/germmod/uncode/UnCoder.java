package net.ccbluex.liquidbounce.utils.germmod.uncode;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class UnCoder {
    public static String deCode2(String string, String string2) {
        return new Base32().encodeAsString(desDeCode(string, string2.getBytes())).replaceAll("=", "");
    }

    private static Key getDeCodeKey(String string) {
        KeyGenerator keyGenerator = null;
        SecureRandom secureRandom = null;
        try {
            keyGenerator = KeyGenerator.getInstance("DES");
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
        }
        secureRandom.setSeed(string.getBytes());
        keyGenerator.init(secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    public static byte[] desDeCode(String string, byte[] byArray) {
        byte[] byArray2 = null;
        try {
            Key key = getDeCodeKey(string);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, key);
            byArray2 = cipher.doFinal(byArray);
            return byArray2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return byArray2;
        }
        finally {
            Object var3_6 = null;
        }
    }

    public static char[] bondArray(char[] ... cArray) {
        int n = 0;
        for (char[] cArray2 : cArray) {
            n += cArray2.length;
        }
        char[] cArray3 = new char[n];
        int n2 = 0;
        for (char[] cArray4 : cArray) {
            System.arraycopy(cArray4, 0, cArray3, n2, cArray4.length);
            n2 += cArray4.length;
        }
        return cArray3;
    }

    public static String deCode(String string, String string2) {
        return Base64.encodeBase64URLSafeString(desDeCode(string, string2.getBytes()));
    }


    public static String getID(String string) {
        String string2 = deCode("1qaz2wsx3edc4ds6g4f4g65a7ujm8ik,9ol.0p;/", string);
        return deCode2("!QAZ@WSX#EDC$RFV%TGB^YHN&UJM*IK<(OL>)P:?", string2);
    }
}
