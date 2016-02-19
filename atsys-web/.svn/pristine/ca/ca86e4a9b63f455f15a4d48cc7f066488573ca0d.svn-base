package com.sixlabs.atsys.domain.utils;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    @NotNull
    public static String toMD5(@NotNull final String value) {
        return hash(value, "MD5");
    }

    /**
     * Aplica o hash SHA1 à string fornecida.
     *
     * @param value A string de entrada.
     * @return O hash SHA1 da string de entrada.
     */
    @NotNull
    public static String toSHA1(@NotNull final String value) {
        return hash(value, "SHA1");
    }

    /**
     * Aplica o hash SHA-256 à string fornecida.
     *
     * @param value A string de entrada.
     * @return O hash SHA-256 da string de entrada.
     */
    @NotNull
    public static String toSHA256(@NotNull final String value) {
        return hash(value, "SHA-256");
    }

    /**
     * Obtém o hash da string.
     *
     * @param value A string de entrada.
     * @param algo  O nome do algoritmo de hash.
     * @return O hash da string de entrada.
     */
    @NotNull
    public static String hash(@NotNull final String value, @NotNull final String algo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            md.update(value.getBytes());
            return toHex(md.digest());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna a representação em string hexadecimal do array de bytes.
     *
     * @param bytes O array de bytes.
     * @return A string em hexadecimal, que representa o array de bytes.
     */
    private static String toHex(byte[] bytes) {
        final StringBuilder s = new StringBuilder();
        for (byte aByte : bytes) {
            int parteAlta = ((aByte >> 4) & 0xf) << 4;
            int parteBaixa = aByte & 0xf;
            if (parteAlta == 0) {
                s.append('0');
            }
            s.append(Integer.toHexString(parteAlta | parteBaixa));
        }
        return s.toString();
    }

}
