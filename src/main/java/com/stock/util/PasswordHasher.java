package com.stock.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Classe pour le hachage et la vérification des mots de passe
 */
public class PasswordHasher {

    public static String hashPassword(String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());

        // Combiner salt et hash
        byte[] saltAndHash = new byte[salt.length + hashedPassword.length];
        System.arraycopy(salt, 0, saltAndHash, 0, salt.length);
        System.arraycopy(hashedPassword, 0, saltAndHash, salt.length, hashedPassword.length);

        return Base64.getEncoder().encodeToString(saltAndHash);
    }

    public static boolean verifyPassword(String password, String hash) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(hash);
        byte[] salt = new byte[16];
        System.arraycopy(decoded, 0, salt, 0, salt.length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());

        byte[] extractedHash = new byte[decoded.length - salt.length];
        System.arraycopy(decoded, salt.length, extractedHash, 0, extractedHash.length);

        return MessageDigest.isEqual(hashedPassword, extractedHash);
    }
}

