package com.sixlabs.atsys.domain.utils;

import com.sixlabs.atsys.domain.ApplicationException;
import com.rits.cloning.Cloner;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);

    private static final Cloner CLONER = new Cloner();

    static {
        // TODO: Incluir as classes do hibernate/eclipseLink.
        //CLONER.dontClone(UnitOfWorkQueryValueHolder.class);
    }

    private ObjectUtils() {
    }

    /**
     * Retorna uma cópia completa do objeto.
     *
     * @param objeto O objeto a ser copiado.
     * @param <E>    O tipo do objeto.
     * @return A cópia do objeto.
     */
    public static <E> E copy(E objeto) {
        return objeto != null ? CLONER.deepClone(objeto) : null;
    }

    public static <T> boolean equals(T first, T second) {
        return first != null && first.equals(second);
    }

    public static <T> boolean changed(T from, T to) {
        return !equals(from, to);
    }

    public static <T> String toString(T object) {
        if (object != null) return object.toString();
        return null;
    }

    public static <T> String toString(Callable<T> callable) {
        return call(() -> {
            T value = callable.call();
            if (value != null) return value.toString();
            return null;
        });
    }

    public static <T> Optional<T> optionalOf(Object obj, Class<T> clazz) {
        return obj != null && obj.getClass().isAssignableFrom(clazz) ? Optional.of((T) obj) : Optional.empty();
    }

    public static Function<String, Integer> stringToInteger() {
        return codeStr -> {
            if (codeStr != null) return Integer.valueOf(codeStr);
            else return null;
        };
    }

    public static Function<String, Long> stringToLong() {
        return codeStr -> {
            if (codeStr != null) return Long.valueOf(codeStr);
            else return null;
        };
    }

    public static String toBase64(Object object) {
        String encoded = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
            encoded = new String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()));

        } catch (Exception e) {
            processException(e, LOG);
        }
        return encoded;
    }

    public static <T> T fromBase64(String string, Class<T> clazz) {
        byte[] bytes = Base64.getDecoder().decode(string.getBytes());
        T object = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            object = (T) objectInputStream.readObject();

        } catch (Exception e) {
            processException(e, LOG);
        }
        return object;
    }

    public static <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v ->
                        futures.stream().
                                map(CompletableFuture::join).
                                collect(Collectors.<T>toList())
        );
    }

    public static <T> CompletableFuture<List<T>> allOf(Stream<CompletableFuture<T>> futures) {
        return allOf(futures.collect(Collectors.toList()));
    }

    public static void run(Logger log, Executable block) {
        try {
            block.execute();

        } catch (Throwable e) {
            processException(e, log);
        }
    }

    /**
     * Executa um bloco de código. As seguintes regras de tratamento de exceções são aplicadas:
     * <ul>
     * <li>Exceções derivadas de {@link RuntimeException} são relançadas na sua forma original.</li>
     * <li>Exceções derivadas de {@link Exception} são relançadas como {@link RuntimeException}.</li>
     * </ul>
     *
     * @param block O bloco de código a ser executado.
     */
    public static void run(Executable block) {
        try {
            block.execute();

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T call(Logger log, Callable<T> block) {
        try {
            return block.call();

        } catch (Throwable e) {
            processException(e, log);
        }
        return null;
    }

    /**
     * Executa um bloco de código e retorna o resultado. As seguintes regras de tratamento de exceções são aplicadas:
     * <ul>
     * <li>Exceções derivadas de {@link RuntimeException} são relançadas na sua forma original.</li>
     * <li>Exceções derivadas de {@link Exception} são relançadas como {@link RuntimeException}.</li>
     * </ul>
     *
     * @param block O bloco de código a ser executado.
     * @param <T>   O tipo do objeto de retorno do bloco de código.
     * @return O valor de retorno do bloco de código.
     */
    public static <T> T call(Callable<T> block) {
        try {
            return block.call();

        } catch (RuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean tryCatch(Logger log, Executable block, Executable always) {
        try {
            block.execute();
            return true;
        } catch (Throwable e) {
            processException(e, log);
            return false;
        } finally {
            run(log, always);
        }
    }

    /**
     * Executa o bloco de código, dependendo do estado de 'canRun'. Este método não levanta exceção.
     *
     * @param log    O log a ser registrado.
     * @param canRun Indica se pode executar o bloco de código ou não.
     * @param block  O bloco de código.
     */
    public static void run(Logger log, AtomicBoolean canRun, Executable block) {
        try {
            if (canRun.compareAndSet(true, false)) {
                block.execute();
            }

        } catch (Throwable e) {
            processException(e, log);

        } finally {
            canRun.compareAndSet(false, true);
        }
    }

    public static void processException(Throwable e, Logger log) {
        Optional<String> msg = ObjectUtils.unwrap(e);
        if (msg.isPresent()) log.warn(msg.get());
        else logError("Erro inesperado.", e, log);
    }

    public static void logError(String message, Throwable e, Logger log) {
        final String exMsg = e.getMessage();
        if (exMsg != null) log.error(message + " " + exMsg);
        else log.error(message, e);
    }

    public static Optional<String> unwrap(Throwable t) {
        if (t == null) return Optional.empty();
        if (t instanceof ApplicationException) return Optional.ofNullable(t.getMessage());
        return unwrap(t.getCause());
    }

    public static String toString(Properties properties) {
        StringWriter sw = new StringWriter();
        try {
            if (properties != null)
                properties.store(sw, "Propriedades no formato 'chave=valor'");

        } catch (Exception e) {
            processException(e, LOG);
        }
        return sw.toString();
    }

    public static Properties toProperties(String value) throws IOException {
        Properties props = new Properties();
        if (value != null) {
            props.load(new StringReader(value));
            return props;
        }
        return props;
    }

    /**
     * Converte uma string em um objeto Json.
     *
     * @param value A string.
     * @return O objeto Json.
     */
    public static JsonObject toJson(String value) {
        return Json.createReader(new StringReader(value)).readObject();
    }

    /**
     * Converte uma string em um contrutor de objetos Json. É útil para converter de string para Json, e
     * adicionar campos extras ao objeto Json antes da contrução.
     *
     * @param value A string.
     * @return O contrutor de objetos Json.
     */
    public static JsonObjectBuilder toBuilder(String value) {
        final JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        toJson(value).forEach(jsonBuilder::add);
        return jsonBuilder;
    }

    /**
     * Retorna a representação em string hexadecimal do array de bytes.
     *
     * @param bytes O array de bytes.
     * @return A string em hexadecimal, que representa o array de bytes.
     */
    public static String toHex(byte[] bytes) {
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
}
